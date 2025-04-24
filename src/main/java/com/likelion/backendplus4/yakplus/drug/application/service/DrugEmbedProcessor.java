package com.likelion.backendplus4.yakplus.drug.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrugDetail;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.embedding.EmbeddingAdapter;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.embedding.EmbeddingModelType;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.GovDrugDetailEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.GovDrugEmbedEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugDetailJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugEmbedJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper.DrugDetailMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DrugEmbedProcessor {
	private final EmbeddingAdapter adapter;
	private final GovDrugDetailJpaRepository govDrugDetailJpaRepository;
	private final EmbeddingAdapter embeddingAdapter;
	private final GovDrugEmbedJpaRepository embeddingRepository;

	public void startEmbedding(){
		List<GovDrugDetailEntity> allItem = getAllItem();
		for (GovDrugDetailEntity detailEntity : allItem) {
			GovDrugDetail govDrugDetail = DrugDetailMapper.toDomainFromEntity(detailEntity);
			Long drugId = govDrugDetail.getDrugId();
			convertSingleStringForEfficacy(govDrugDetail.getEfficacy());

			float[] openAIVector = embeddingAdapter.getEmbedding(
										detailEntity.getEfficacy(),
										EmbeddingModelType.OPENAI);
			float[] sbertVector = embeddingAdapter.getEmbedding(
										detailEntity.getEfficacy(),
										EmbeddingModelType.SBERT);
			float[] kmbertVector = embeddingAdapter.getEmbedding(
										detailEntity.getEfficacy(),
										EmbeddingModelType.KM_BERT);


			embeddingRepository.save(GovDrugEmbedEntity.builder()
				 					.drugId(drugId)
									.gptVector(toStringFromFloatArray(openAIVector))
									.krSbertVector(toStringFromFloatArray(sbertVector))
									.kmBertVector(toStringFromFloatArray(kmbertVector))
									.build());
		}

	}

	private String toStringFromFloatArray(float[] openAIVector) {
		try {
			return new ObjectMapper().writeValueAsString(openAIVector);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String convertSingleStringForEfficacy(List<String> stringList) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String s : stringList) {
			stringBuilder.append(s);
			stringBuilder.append(" ");
		}
		return stringBuilder.toString();
	}

	private List<GovDrugDetailEntity> getAllItem() {
		return govDrugDetailJpaRepository.findAll();
	}
}