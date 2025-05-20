package com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.support.mapper;

import static com.likelion.backendplus4.yakplus.common.logging.util.LogUtil.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.common.logging.util.LogLevel;
import com.likelion.backendplus4.yakplus.drug.domain.model.vo.Material;

import java.util.List;
import java.util.Map;

public class DrugFieldTypeMapper {
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

    public static String convertSingleStringForEfficacy(List<String> stringList) {
        log(LogLevel.DEBUG, "약품 효능 정보 단일 문자로 변환 시작");
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : stringList) {
            stringBuilder.append(s);
            stringBuilder.append(" ");
        }

        String s = stringBuilder.toString();
        log(LogLevel.DEBUG, "약품 효능 정보 단일 문자로 변환 완료" + s);
        return s;
    }
}
