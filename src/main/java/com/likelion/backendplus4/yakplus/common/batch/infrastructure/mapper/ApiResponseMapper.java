package com.likelion.backendplus4.yakplus.common.batch.infrastructure.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.common.batch.infrastructure.detail.dto.DrugDetailRequest;
import com.likelion.backendplus4.yakplus.common.batch.infrastructure.exception.ParserBatchError;
import com.likelion.backendplus4.yakplus.common.batch.infrastructure.exception.ParserBatchException;
import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.common.util.log.LogUtil;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.dto.DrugImageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * API 응답(JsonNode)을 DTO로 변환하는 Mapper 클래스입니다.
 * JsonNode를 DTO 리스트로 변환하는 메서드를 제공합니다.
 *
 * @fields objectMapper Jackson ObjectMapper 인스턴스
 * @modified 2025-05-02
 * @since 2025-04-22
 */
@Component
@RequiredArgsConstructor
public class ApiResponseMapper {
    private final ObjectMapper objectMapper;

    /**
     * JsonNode를 DrugDetailRequest 리스트로 변환합니다.
     *
     * @param items JsonNode 형태의 데이터
     * @return DrugDetailRequest 리스트
     * @throws ParserBatchException JSON 타입 변환 실패 시 발생
     * @author 함예정
     * @modified 2025-05-02
     * @since 2025-04-22
     */
    public List<DrugDetailRequest> toListFromDrugDetails(JsonNode items) {
        try {
            return objectMapper.readValue(
                    items.toString(), new TypeReference<>() {
                    }
            );
        } catch (JsonProcessingException e) {
            LogUtil.log(LogLevel.ERROR, "타입변환 실패(JsonNode -> List<DrugDetailRequest>");
            throw new ParserBatchException(ParserBatchError.JSON_TYPE_CHANGE_FAIL);
        }
    }

    /**
     * JsonNode를 DrugImageRequest 리스트로 변환합니다.
     *
     * @param items JsonNode 형태의 데이터
     * @return DrugImageRequest 리스트
     * @throws ParserBatchException JSON 타입 변환 실패 시 발생
     * @author 함예정
     * @modified 2025-05-02
     * @since 2025-04-22
     */
    public List<DrugImageRequest> toListFromDrugImages(JsonNode items) {
        try {
            return objectMapper.readValue(
                    items.toString(), new TypeReference<>() {
                    }
            );
        } catch (JsonProcessingException e) {
            LogUtil.log(LogLevel.ERROR, "타입변환 실패(JsonNode -> List<DrugImageRequest>");
            throw new ParserBatchException(ParserBatchError.JSON_TYPE_CHANGE_FAIL);
        }
    }
}
