package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.embed.processor;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.drug.domain.model.vo.Material;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.combine.dto.TableCombineDto;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.embed.dto.DrugVectorDto;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.index.application.port.out.EmbeddingLoadingPort;
import com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence.DrugMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmbedProcessor implements ItemProcessor<DrugRawDataEntity, DrugVectorDto> {
	private final EmbeddingLoadingPort embeddingLoadingPort;

	@Override
	public DrugVectorDto process(DrugRawDataEntity item) throws Exception {
		Long id = item.getDrugId();
		String embeddingText = getEmbedTextFromItem(item);
		float[] embeddingVector = embeddingLoadingPort.getEmbedding(embeddingText);
		return DrugVectorDto
			.builder()
			.drugId(id)
			.vector(embeddingVector)
			.build();
	}

	private String getEmbedTextFromItem(DrugRawDataEntity item){
		return DrugMapper.convertSingleStringForEfficacy(
			DrugMapper.parseStringToList(item.getEfficacy())
		);
	}


}
