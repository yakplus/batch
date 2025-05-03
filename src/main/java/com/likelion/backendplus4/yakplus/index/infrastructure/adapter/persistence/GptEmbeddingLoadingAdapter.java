package com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.embed.dto.DrugVectorDto;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugGptEmbedEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugGptEmbedJpaRepository;
import com.likelion.backendplus4.yakplus.index.application.port.out.EmbeddingLoadingPort;
import com.likelion.backendplus4.yakplus.index.exception.IndexException;
import com.likelion.backendplus4.yakplus.index.exception.error.IndexErrorCode;
import com.likelion.backendplus4.yakplus.index.support.EmbeddingUtil.EmbedEntityBuilder;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GptEmbeddingLoadingAdapter implements EmbeddingLoadingPort {
	private final GovDrugGptEmbedJpaRepository govDrugGptEmbedJpaRepository;
	private final OpenAiApi openAiApi;

	@Override
	public List<Drug> loadEmbeddingsByPage(Pageable pageable) {
		List<Drug> drugs = new ArrayList<>();
		List<Object[]> rows = govDrugGptEmbedJpaRepository.findRawAndEmbed(pageable);
		log("loadEmbeddingsByPage - " + pageable.getPageNumber() + "페이지 에서 받아온 drug 객체 제작 대상 데이터 수: " + rows.size());
		if (rows.isEmpty()) {
			log(LogLevel.ERROR, "loadEmbeddingsByPage - Drug 도메인 객체 생성 대상 데이터 없음");
			throw new IndexException(IndexErrorCode.RAW_DATA_FETCH_ERROR);
		}
		for (Object[] arr : rows) {
			DrugRawDataEntity raw = (DrugRawDataEntity)arr[0];
			DrugGptEmbedEntity embed = (DrugGptEmbedEntity)arr[1];
			drugs.add(toDomainFromEntity(raw, embed));
		}
		log("loadEmbeddingsByPage - Drug 도메인 객체 생성 완료");
		return drugs;
	}

	@Override
	public float[] getEmbedding(String text) {
		OpenAiEmbeddingModel openAiEmbeddingModel = new OpenAiEmbeddingModel(
			this.openAiApi,
			MetadataMode.EMBED,
			OpenAiEmbeddingOptions.builder()
				.model("text-embedding-3-small")
				.build(),
			RetryUtils.DEFAULT_RETRY_TEMPLATE);
		EmbeddingResponse embeddingResponse = openAiEmbeddingModel
			.embedForResponse(List.of(text));
		Embedding embedding = embeddingResponse.getResults().getFirst();
		return embedding.getOutput();
	}

	@Override
	public void saveEmbedding(List<DrugVectorDto> dtos) {
		govDrugGptEmbedJpaRepository.saveAll(
			dtos.stream()
				.map(dto -> EmbedEntityBuilder.buildEmbedEntity(dto, DrugGptEmbedEntity.class))
				.toList()
		);
		govDrugGptEmbedJpaRepository.flush();
	}

	private static Drug toDomainFromEntity(DrugRawDataEntity drugEntity, DrugGptEmbedEntity embedEntity) {
		return Drug.builder()
			.drugId(drugEntity.getDrugId())
			.drugName(drugEntity.getDrugName())
			.company(drugEntity.getCompany())
			.permitDate(drugEntity.getPermitDate())
			.isGeneral(drugEntity.isGeneral())
			.materialInfo(DrugMapper.parseMaterials(drugEntity.getMaterialInfo()))
			.storeMethod(drugEntity.getStoreMethod())
			.validTerm(drugEntity.getValidTerm())
			.efficacy(DrugMapper.parseStringToList(drugEntity.getEfficacy()))
			.usage(DrugMapper.parseStringToList(drugEntity.getUsage()))
			.precaution(DrugMapper.parsePrecaution(drugEntity.getPrecaution()))
			.imageUrl(drugEntity.getImageUrl())
			.cancelDate(drugEntity.getCancelDate())
			.cancelName(drugEntity.getCancelName())
			.isHerbal(drugEntity.getIsHerbal())
			.vector(DrugMapper.parseJsonToFloatArray(embedEntity.getGptVector()))
			.build();
	}

}
