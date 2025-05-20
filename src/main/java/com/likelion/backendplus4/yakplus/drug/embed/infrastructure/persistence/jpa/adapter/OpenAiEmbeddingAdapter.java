package com.likelion.backendplus4.yakplus.drug.embed.infrastructure.persistence.jpa.adapter;

import com.likelion.backendplus4.yakplus.drug.embed.infrastructure.batch.step.dto.DrugVectorDto;
import com.likelion.backendplus4.yakplus.common.logging.util.LogLevel;
import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.drug.embed.infrastructure.persistence.entity.DrugGptEmbedEntity;
import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.embed.infrastructure.persistence.jpa.repository.OpenAiEmbedJpaRepository;
import com.likelion.backendplus4.yakplus.drug.index.application.port.out.EmbeddingPort;
import com.likelion.backendplus4.yakplus.drug.index.exception.IndexException;
import com.likelion.backendplus4.yakplus.drug.index.exception.error.IndexErrorCode;
import com.likelion.backendplus4.yakplus.drug.index.support.EmbeddingUtil.DrugEntityMapper;
import com.likelion.backendplus4.yakplus.drug.index.support.EmbeddingUtil.EmbedEntityBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.likelion.backendplus4.yakplus.common.logging.util.LogUtil.log;

@Repository
@RequiredArgsConstructor
public class OpenAiEmbeddingAdapter implements EmbeddingPort {
    private final OpenAiEmbedJpaRepository govDrugGptEmbedJpaRepository;
    private final OpenAiApi openAiApi;
    private static final String EMBEDDING_MODEL_NAME = "text-embedding-3-small"; // yml에서 받아오기


    /**
     * 주어진 텍스트를 OpenAI 임베딩 모델에 전달하여 임베딩 벡터를 생성하고 반환합니다.
     *
     * @param text 임베딩을 생성할 입력 문자열
     * @return OpenAI 모델이 생성한 임베딩 벡터 (float 배열)
     * @author 이해창
     * @since 2025-04-25
     * @modified
     * 25.05.03 - 가독성 개선을 위한 리팩토링
     */
    @Override
    public float[] getEmbedding(String text) {
        OpenAiEmbeddingModel openAiEmbeddingModel = getOpenAiEmbeddingModel();
        EmbeddingResponse embeddingResponse = openAiEmbeddingModel.embedForResponse(List.of(text));
        return embeddingResponse.getResults().getFirst().getOutput();
    }

    /**
     * 전달된 DrugVectorDto 리스트를 DrugGptEmbedEntity로 변환하여
     * JPA 저장소에 일괄 저장하고 즉시 플러시(flush)합니다.
     *
     * <p>각 DTO는 EmbedEntityBuilder.buildEmbedEntity를 통해
     * DrugGptEmbedEntity 타입의 임베딩 엔티티로 변환됩니다.</p>
     *
     * @param dtos 저장할 약품 임베딩 정보가 담긴 DTO 리스트
     * @author 함예정
     * @since 2025-04-25
     */
    @Override
    public void saveEmbedding(List<DrugVectorDto> dtos) {
        govDrugGptEmbedJpaRepository.saveAll(
                dtos.stream()
                        .map(dto -> EmbedEntityBuilder.buildEmbedEntity(dto, DrugGptEmbedEntity.class))
                        .toList()
        );
        govDrugGptEmbedJpaRepository.flush();
    }

    /**
     * Pageable 정보를 이용해 원시 약품 데이터와 임베딩 데이터를 페이징 처리하여 조회하고,
     * Drug 도메인 객체 리스트로 변환하여 반환합니다.
     *
     * <p>조회된 데이터가 없을 경우 IndexException을 발생시킵니다.</p>
     *
     * @param pageable 페이지 번호, 크기, 정렬 정보를 포함하는 Pageable 객체
     * @return 조회된 Drug 도메인 객체 리스트
     * @throws IndexException 원시 데이터 및 임베딩 데이터를 조회하지 못했거나 조회된 데이터가 없을 경우
     * @author 이해창
     * @since 2025-05-03
     */
    @Override
    public List<Drug> loadEmbeddingsByPage(Pageable pageable) {
        List<Object[]> rows = fetchRawAndEmbedPage(pageable);
        log("loadEmbeddingsByPage - " + pageable.getPageNumber() + "페이지 에서 받아온 drug 객체 제작 대상 데이터 수: " + rows.size());
        if (rows.isEmpty()) {
            log(LogLevel.ERROR, "loadEmbeddingsByPage - Drug 도메인 객체 생성 대상 데이터 없음");
            throw new IndexException(IndexErrorCode.RAW_DATA_FETCH_ERROR);
        }
        List<Drug> drugs = rows.stream().map(this::combineRawAndEmbed).toList();
        log("loadEmbeddingsByPage - Drug 도메인 객체 생성 완료");
        return drugs;
    }

    /**
     * OpenAI 임베딩 모델을 EMBED 모드로 초기화하여 반환합니다.
     *
     * @return EMBED 모드로 구성된 OpenAiEmbeddingModel 인스턴스
     * @author 이해창
     * @since 2025-05-03
     */
    private OpenAiEmbeddingModel getOpenAiEmbeddingModel() {
        OpenAiEmbeddingModel openAiEmbeddingModel = new OpenAiEmbeddingModel(
                this.openAiApi,
                MetadataMode.EMBED,
                OpenAiEmbeddingOptions.builder()
                        .model(EMBEDDING_MODEL_NAME)
                        .build(),
                RetryUtils.DEFAULT_RETRY_TEMPLATE);
        return openAiEmbeddingModel;
    }

    /**
     * Pageable 정보를 기반으로 원시 약품 데이터와 임베딩 데이터를 조인하여 조회합니다.
     *
     * @param pageable 페이지 번호, 크기, 정렬 정보를 포함한 Pageable 객체
     * @return Object[] 배열 리스트; [0]에는 DrugRawDataEntity, [1]에는 DrugGptEmbedEntity
     * @author 이해창
     * @since 2025-05-03
     */
    private List<Object[]> fetchRawAndEmbedPage(Pageable pageable) {
        return govDrugGptEmbedJpaRepository.findRawAndEmbed(pageable);
    }

    /**
     * Object 배열로 전달된 원시 데이터 엔티티와 임베딩 엔티티를 결합하여
     * Drug 도메인 객체로 변환합니다.
     *
     * @param pair Object 배열; index 0은 DrugRawDataEntity, index 1은 DrugGptEmbedEntity
     * @return 결합된 데이터를 담은 Drug 도메인 객체
     * @author 이해창
     * @since 2025-05-03
     */
    private Drug combineRawAndEmbed(Object[] pair) {
        DrugRawDataEntity raw = (DrugRawDataEntity) pair[0];
        DrugGptEmbedEntity embed = (DrugGptEmbedEntity) pair[1];
        return DrugEntityMapper.toDomainFromEntity(raw, embed);
    }

}
