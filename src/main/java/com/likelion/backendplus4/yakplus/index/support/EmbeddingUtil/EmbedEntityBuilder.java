package com.likelion.backendplus4.yakplus.index.support.EmbeddingUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EmbedEntityBuilder {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T buildEmbedEntity(Long drugId, float[] vector, Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor(Long.class, String.class)
                    .newInstance(drugId, toStringFromFloatArray(vector));
        } catch (Exception e) {
            //TODO: 엔터티 생성 실패
            throw new RuntimeException(e);
        }
    }

    private static String toStringFromFloatArray(float[] vector) {
        try {
            return MAPPER.writeValueAsString(vector);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
