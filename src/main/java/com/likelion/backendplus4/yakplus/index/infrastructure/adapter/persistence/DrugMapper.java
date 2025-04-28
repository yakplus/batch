package com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.drug.domain.model.vo.Material;

import java.util.List;
import java.util.Map;

public class DrugMapper{
    public static List<Material> parseMaterials(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, new TypeReference<List<Material>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Material 파싱 실패", e);
        }
    }

    public static List<String> parseStringToList(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("String to list 파싱 실패", e);
        }
    }

    public static Map<String, List<String>> parsePrecaution(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, new TypeReference<Map<String, List<String>>>() {});
        } catch (Exception e) {
            throw new RuntimeException("precaution 파싱 실패", e);
        }
    }

    public static float[] parseJsonToFloatArray(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, float[].class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse vector JSON", e);
        }
    }
}
