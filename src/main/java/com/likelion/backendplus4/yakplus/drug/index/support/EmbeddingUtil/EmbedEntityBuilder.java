package com.likelion.backendplus4.yakplus.drug.index.support.EmbeddingUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.drug.embed.infrastructure.batch.step.dto.DrugVectorDto;

public class EmbedEntityBuilder {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T buildEmbedEntity(DrugVectorDto dto, Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor(Long.class, String.class)
                    .newInstance(dto.getDrugId(), toStringFromFloatArray(dto.getVector()));
        } catch (Exception e) {
            //TODO: 엔터티 생성 실패
            throw new RuntimeException(e);
        }
    }

    private static String toStringFromFloatArray(float[] vector) {
        try {
            return MAPPER.writeValueAsString(vector);
        } catch (JsonProcessingException e) {
            //TODO: 변환 실패 로그
            throw new RuntimeException(e);
        }
    }
}
