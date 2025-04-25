package com.likelion.backendplus4.yakplus.drug.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrugDetail;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.embedding.EmbeddingAdapter;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.embedding.EmbeddingModelType;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.ApiDataDrugImgEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.GovDrugDetailEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.GovDrugEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jdbc.GovDrugJdbcRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.ApiDataDrugImgRepo;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugDetailJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper.DrugDetailMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DrugEmbedProcessor {
	private final EmbeddingAdapter adapter;
	private final GovDrugDetailJpaRepository govDrugDetailJpaRepository;
	private final EmbeddingAdapter embeddingAdapter;
	private final ApiDataDrugImgRepo apiDataDrugImgRepo;
	private final GovDrugJpaRepository govDrugJpaRepository;
	private final GovDrugJdbcRepository govDrugJdbcRepository;

	public void startEmbedding(){
		List<GovDrugDetailEntity> allItem = getAllItem();
		List<GovDrugEntity> drugEntitys = new ArrayList<>();
		for (GovDrugDetailEntity detailEntity : allItem) {
			GovDrugDetail govDrugDetail = DrugDetailMapper.toDomainFromEntity(detailEntity);

			String text = convertSingleStringForEfficacy(govDrugDetail.getEfficacy());

			float[] sbertVector = embeddingAdapter.getEmbedding(
				text,
				EmbeddingModelType.SBERT);

			float[] kmbertVector = embeddingAdapter.getEmbedding(
				text,
				EmbeddingModelType.KM_BERT);

			float[] openAIVector = embeddingAdapter.getEmbedding(
				text,
				EmbeddingModelType.OPENAI);

			GovDrugEntity govDrugEntity = bulidGovDrugEntity(
					govDrugDetail , openAIVector, kmbertVector, sbertVector
			);
			drugEntitys.add(govDrugEntity);
		}
		saveEntitys(drugEntitys);


	}
	private void saveEntitys(List<GovDrugEntity> entitys){
		govDrugJpaRepository.saveAll(entitys);
		govDrugJpaRepository.flush();
		entitys.clear();
	}

	private GovDrugEntity bulidGovDrugEntity(GovDrugDetail govDrugDetail, float[] openAIVector, float[] kmbertVector,
		float[] sbertVector) {
		return GovDrugEntity.builder()
				.drugId(govDrugDetail.getDrugId())
				.drugName(govDrugDetail.getDrugName())
				.company(govDrugDetail.getCompany())
				.permitDate(govDrugDetail.getPermitDate())
				.isGeneral(govDrugDetail.isGeneral())
				.materialInfo(govDrugDetail.getMaterialInfo().toString())
				.storeMethod(govDrugDetail.getStoreMethod())
				.validTerm(govDrugDetail.getValidTerm())
				.efficacy(govDrugDetail.getEfficacy().toString())
				.usage(govDrugDetail.getUsage().toString())
				.precaution(govDrugDetail.getPrecaution().toString())
				.imageUrl(getImageUrl(govDrugDetail.getDrugId()))
				.gptVector(toStringFromFloatArray(openAIVector))
				.kmBertVector(toStringFromFloatArray(kmbertVector))
				.sbertVector(toStringFromFloatArray(sbertVector))
				.build();
	}

	private String getImageUrl(Long drugId) {
		ApiDataDrugImgEntity drugImgEntity =  apiDataDrugImgRepo.findById(drugId)
											.orElseGet(() -> new ApiDataDrugImgEntity());
			return drugImgEntity.getImgUrl();
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