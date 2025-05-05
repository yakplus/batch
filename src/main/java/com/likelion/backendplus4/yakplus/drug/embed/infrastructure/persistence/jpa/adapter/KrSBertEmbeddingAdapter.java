package com.likelion.backendplus4.yakplus.drug.embed.infrastructure.persistence.jpa.adapter;

import com.likelion.backendplus4.yakplus.drug.embed.infrastructure.batch.step.dto.DrugVectorDto;
import com.likelion.backendplus4.yakplus.common.logging.util.LogLevel;
import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.drug.embed.infrastructure.persistence.entity.DrugKrSBertEmbedEntity;
import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.api.util.UriCompBuilder;
import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.persistence.dto.EmbeddingRequestText;
import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.embed.infrastructure.persistence.jpa.repository.KrSBertEmbedJpaRepository;
import com.likelion.backendplus4.yakplus.drug.index.application.port.out.EmbeddingPort;
import com.likelion.backendplus4.yakplus.drug.index.exception.IndexException;
import com.likelion.backendplus4.yakplus.drug.index.exception.error.IndexErrorCode;
import com.likelion.backendplus4.yakplus.drug.index.support.EmbeddingUtil.DrugEntityMapper;
import com.likelion.backendplus4.yakplus.drug.index.support.EmbeddingUtil.EmbedEntityBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static com.likelion.backendplus4.yakplus.common.logging.util.LogUtil.log;

@Repository
@RequiredArgsConstructor
public class KrSBertEmbeddingAdapter implements EmbeddingPort {
    private final KrSBertEmbedJpaRepository krSBertEmbedJpaRepository;
    private final UriCompBuilder apiUriCompBuilder;
    private final RestTemplate restTemplate;

    /**
     * 주어진 텍스트에 대해 외부 KR-SBERT 임베딩 API를 호출하여 임베딩 벡터를 생성하고 반환합니다.
     *
     * @param text 임베딩을 생성할 원본 텍스트
     * @return 생성된 임베딩 벡터 (float 배열)
     * @author 함예정
     * @since 2025-04-25
     */
    @Override
    public float[] getEmbedding(String text) {
        URI embeddingURI = getEmbeddingURI();
        return getEmbeddingVector(embeddingURI, text);
    }

    /**
     * 전달된 DrugVectorDto 리스트를 DrugKrSBertEmbedEntity로 변환하여
     * JPA 저장소에 일괄 저장하고 즉시 플러시(flush)합니다.
     *
     * @param dtos 저장할 약품 임베딩 정보가 담긴 DTO 리스트
     * @author 함예정
     * @since 2025-04-25
     * @modified
     * 2025-05-02 - 배치 적용을 위한 입력값 변경
     */
    @Override
    public void saveEmbedding(List<DrugVectorDto> dtos) {
        krSBertEmbedJpaRepository.saveAll(
                dtos.stream()
                        .map(dto -> EmbedEntityBuilder.buildEmbedEntity(dto, DrugKrSBertEmbedEntity.class))
                        .toList()
        );
        krSBertEmbedJpaRepository.flush();
    }

    /**
     * Pageable 정보를 이용해 원시 약품 데이터와 KR-SBERT 임베딩 데이터를 조인하여
     * 페이징 처리된 Drug 도메인 객체 리스트를 조회합니다.
     *
     * @param pageable 페이지 번호, 크기, 정렬 정보를 포함하는 Pageable 객체
     * @return 조회된 Drug 도메인 객체 리스트
     * @throws IndexException 조회된 데이터가 없거나 조회 중 오류가 발생한 경우
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
     * KR-SBERT 임베딩 API 호출에 사용할 URI를 생성하여 반환합니다.
     *
     * @return Embedding API 호출용 URI
     * @author 함예정
     * @since 2025-04-25
     */
    private URI getEmbeddingURI() {
        return apiUriCompBuilder.getUriForKrSbertEmbeding();
    }

    /**
     * 지정된 URI에 텍스트를 포함한 HTTP 요청을 전송하여 임베딩 벡터를 받아옵니다.
     *
     * @param embedUri 임베딩 API 호출 대상 URI
     * @param text     임베딩할 원본 텍스트
     * @return API 응답으로 받은 임베딩 벡터 (float 배열)
     * @author 함예정
     * @since 2025-04-25
     */
    private float[] getEmbeddingVector(URI embedUri, String text) {
        EmbeddingRequestText embeddingRequestText = new EmbeddingRequestText();
        embeddingRequestText.setText(text);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EmbeddingRequestText> request = new HttpEntity<>(embeddingRequestText, headers);
        return restTemplate.postForObject(embedUri, request, float[].class);
    }

    /**
     * Pageable 정보를 기반으로 원시 약품 데이터와 임베딩 데이터를 조인하여 조회합니다.
     *
     * @param pageable 페이지 번호, 크기, 정렬 정보를 포함하는 Pageable 객체
     * @return Object[] 배열 리스트; index 0은 DrugRawDataEntity, index 1은 DrugKrSBertEmbedEntity
     * @author 이해창
     * @since 2025-05-03
     */
    private List<Object[]> fetchRawAndEmbedPage(Pageable pageable) {
        return krSBertEmbedJpaRepository.findRawAndEmbed(pageable);
    }

    /**
     * Object 배열로 전달된 원시 데이터 엔티티와 임베딩 엔티티를 결합하여
     * Drug 도메인 객체로 변환합니다.
     *
     * @param pair Object 배열; index 0은 DrugRawDataEntity, index 1은 DrugKrSBertEmbedEntity
     * @return 결합된 데이터를 담은 Drug 도메인 객체
     * @author 이해창
     * @since 2025-05-03
     */
    private Drug combineRawAndEmbed(Object[] pair) {
        DrugRawDataEntity raw = (DrugRawDataEntity) pair[0];
        DrugKrSBertEmbedEntity embed = (DrugKrSBertEmbedEntity) pair[1];
        return DrugEntityMapper.toDomainFromEntity(raw, embed);
    }

}
