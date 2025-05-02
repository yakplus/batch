package com.likelion.backendplus4.yakplus.drug.infrastructure.api.support;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static <T> List<T> toListFromJsonString(String jsonString) {
		try {
			return objectMapper.readValue(
				jsonString, new TypeReference<>() {
				}
			);
		} catch (JsonProcessingException e) {
			//TODO: 예외처리
			throw new RuntimeException(e);
		}
	}
}
