package com.likelion.backendplus4.yakplus.drug.application.service.scraper.embed;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugDetailRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugEmbedRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.domain.model.DrugImage;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.out.DrugImageRepositoryAdapter;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper.DrugDetailMapper;
import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrugDetail;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.embedding.EmbeddingAdapter;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.embedding.EmbeddingModelType;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.ApiDataDrugImgEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.DrugDetailEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.GovDrugEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.ApiDataDrugImgRepo;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugDetailJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DrugEmbedProcessor {
	private final DrugDetailRepositoryPort detailRepositoryPort;
	private final DrugImageRepositoryAdapter drugImageRepository;
	private final EmbeddingAdapter embeddingAdapter;
	private final GovDrugJpaRepository govDrugJpaRepository;
	private final DrugEmbedRepositoryPort embedRepositoryPort;


	public void startEmbedding(){
		getAllItem().forEach(detail -> {
			String efficacy = convertSingleStringForEfficacy(detail.getEfficacy());
			saveSbertVector(detail, efficacy);
			saveKmBertVector(detail, efficacy);
			saveGptVector(detail, efficacy);
		});
	}

	private void saveGptVector(GovDrugDetail detail, String text) {
		float[] openAIVector = embeddingAdapter.getEmbedding(
			text, EmbeddingModelType.OPENAI);
		embedRepositoryPort.saveGptEmbed(detail.getDrugId(), openAIVector);
	}

	private void saveKmBertVector(GovDrugDetail detail, String text) {
		float[] kmbertVector = embeddingAdapter.getEmbedding(
			text, EmbeddingModelType.KM_BERT);
		embedRepositoryPort.saveKmBertEmbed(detail.getDrugId(), kmbertVector);
	}

	private void saveSbertVector(GovDrugDetail detail, String text) {
		float[] sbertVector = embeddingAdapter.getEmbedding(
			text, EmbeddingModelType.SBERT);
		embedRepositoryPort.saveKrSbertEmbed(detail.getDrugId(), sbertVector);
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
		DrugImage drugImage =  drugImageRepository.getById(drugId);
		return drugImage.getImageUrl();
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

	private List<GovDrugDetail> getAllItem() {
		return detailRepositoryPort.getAllGovDrugDetail();
	}
}