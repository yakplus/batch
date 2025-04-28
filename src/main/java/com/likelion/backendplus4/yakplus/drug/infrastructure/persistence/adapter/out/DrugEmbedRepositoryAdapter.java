package com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.adapter.out;

import java.util.function.Function;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugEmbedRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugGptEmbedEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugKmBertEmbedEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugKrSbertEmbedEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugGptEmbedJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugKmBertEmbedJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugKrSbertEmbedJpaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DrugEmbedRepositoryAdapter implements DrugEmbedRepositoryPort {
	private final GovDrugGptEmbedJpaRepository gptRepository;
	private final GovDrugKmBertEmbedJpaRepository kmBertRepository;
	private final GovDrugKrSbertEmbedJpaRepository krSbertRepository;

	@Override
	public void saveGptEmbed(Long drugId, float[] gptVector){
		gptRepository.save(
			buildEmbedEntity(drugId, gptVector, DrugGptEmbedEntity.class)
		);
	}

	@Transactional
	@Override
	public void saveKmBertEmbed(Long drugId, float[] kmBertVector){
		kmBertRepository.save(
			buildEmbedEntity(drugId, kmBertVector, DrugKmBertEmbedEntity.class)
		);
	}

	@Transactional
	@Override
	public void saveKrSbertEmbed(Long drugId, float[] krSbertVector){
		krSbertRepository.save(
			buildEmbedEntity(drugId, krSbertVector, DrugKrSbertEmbedEntity.class)
		);
	}

	@Override
	public float[] getGptVector(Long drugId){
		return getVectorFromRepository(
			drugId, gptRepository, e -> e.getGptVector()
		);
	}

	@Override
	public float[] getKmBertVector(Long drugId){
		return getVectorFromRepository(
			drugId, kmBertRepository, e -> e.getKmBertVector()
		);
	}

	@Override
	public float[] getKrSbertVector(Long drugId){
		return getVectorFromRepository(
			drugId, krSbertRepository, e -> e.getKrSbertVector()
		);
	}

	private  <T> T buildEmbedEntity(Long drugId, float[] vector, Class<T> clazz) {
		try {
			String vectorString = toStringFromFloatArray(vector);
			return clazz.getDeclaredConstructor(Long.class, String.class)
				.newInstance(drugId, toStringFromFloatArray(vector));
		} catch (Exception e) {
			//TODO: 엔터티 생성 실패
			throw new RuntimeException(e);
		}
	}

	private <T> float[] getVectorFromRepository(Long drugId, JpaRepository<T, Long> repository, Function<T, String> vectorGetter) {
		T entity = repository.findById(drugId).orElse(null);

		if (entity == null) {
			return null;
		}

		String vectorString = vectorGetter.apply(entity);
		return toFloatFromString(vectorString);
	}
	
	private String toStringFromFloatArray(float[] openAIVector) {
		try {
			return new ObjectMapper().writeValueAsString(openAIVector);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private float[] toFloatFromString(String vector) {
		try {
			return new ObjectMapper().readValue(vector, float[].class);	
		} catch (Exception e) {
			//TODO: 벡터 변환 실패 예외 처리
			System.out.println("벡터 변환 실패");
			return null;
		}
	}
}
