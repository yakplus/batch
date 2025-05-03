package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.common.util.log.LogUtil;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.exception.ParserBatchError;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.exception.ParserBatchException;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.detail.dto.DrugDetailRequest;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.dto.DrugImageRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApiResponseMapper {
	private final ObjectMapper objectMapper;
	public List<DrugDetailRequest> toListFromDrugDetails(JsonNode items) {
		try {
			return objectMapper.readValue(
				items.toString(), new TypeReference<>() {}
			);
		} catch (JsonProcessingException e) {
			LogUtil.log(LogLevel.ERROR, "타입변환 실패(JsonNode -> List<DrugDetailRequest>");
			throw new ParserBatchException(ParserBatchError.JSON_TYPE_CHANGE_FAIL);
		}
	}

	public List<DrugImageRequest> toListFromDrugImages(JsonNode items) {
		try {
			return objectMapper.readValue(
				items.toString(), new TypeReference<>() {}
			);
		} catch (JsonProcessingException e) {
			LogUtil.log(LogLevel.ERROR, "타입변환 실패(JsonNode -> List<DrugImageRequest>");
			throw new ParserBatchException(ParserBatchError.JSON_TYPE_CHANGE_FAIL);
		}
	}
}
