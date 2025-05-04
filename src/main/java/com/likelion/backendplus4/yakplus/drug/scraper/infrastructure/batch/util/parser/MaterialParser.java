package com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.batch.util.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.likelion.backendplus4.yakplus.common.logging.util.LogLevel;
import com.likelion.backendplus4.yakplus.common.logging.util.LogUtil;
import com.likelion.backendplus4.yakplus.drug.scraper.application.exception.ScraperException;
import com.likelion.backendplus4.yakplus.drug.scraper.application.exception.error.ScraperErrorCode;

/**
 * 원재료 정보를 파싱하여 JSON 배열 형식의 문자열로 변환하는 유틸리티 클래스입니다.
 */
public class MaterialParser {

    /**
     * 원재료 문자열을 파싱하여 JSON 배열 형태의 문자열로 변환합니다.
     *
     * @param raw 원재료 정보가 담긴 문자열
     *            (세미콜론으로 블록 구분, 파이프로 키-값 쌍 구분)
     * @return JSON 배열 형태의 문자열
     * @author 함예정
     * @since 2025-04-21
     */
    public static String parseMaterial(String raw) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode resultArray = objectMapper.createArrayNode();
        String[] blocks = splitBlock(raw);
        parsingBlocksAndPutArrayItem(blocks, resultArray);
        return convertString(objectMapper, resultArray);
    }

    /**
     * 블록 배열을 파싱하여 JSON 배열에 항목으로 추가합니다.
     *
     * @param blocks      원재료 블록 배열
     * @param resultArray 결과를 저장할 JSON 배열
     */
    private static void parsingBlocksAndPutArrayItem(String[] blocks, ArrayNode resultArray) {
        for (String block : blocks) {
            block = block.trim();
            if (block.isEmpty()) {
                continue;
            }
            String[] pairs = splitByPipe(block);
            ObjectNode item = makeItem(pairs);
            resultArray.add(item);
        }
    }

    /**
     * JSON 배열을 문자열로 변환합니다.
     *
     * @param objectMapper Jackson ObjectMapper 인스턴스
     * @param arrayNode    변환할 JSON 배열
     * @return JSON 문자열
     * @throws ScraperException JSON 변환 실패 시 발생
     */
    private static String convertString(ObjectMapper objectMapper, ArrayNode arrayNode) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
        } catch (JsonProcessingException e) {
            LogUtil.log(LogLevel.ERROR, "JSON 문자열을 String으로 변환하는 중 오류 발생: " + e.getMessage());
            throw new ScraperException(ScraperErrorCode.MATERIAL_PARSING_FAIL);
        }
    }

    /**
     * 키-값 쌍 배열로부터 JSON 객체를 생성합니다.
     *
     * @param pairs 파이프로 구분된 키-값 쌍 배열
     * @return 생성된 JSON 객체
     */
    private static ObjectNode makeItem(String[] pairs) {
        ObjectNode item = new ObjectMapper().createObjectNode();
        for (String pair : pairs) {
            String[] kv = pair.split(":", 2);
            String key = kv[0].trim();
            String value = "";
            if (kv.length == 2) {
                value = kv[1].trim();
            }
            item.put(key, value);
        }
        return item;
    }

    /**
     * 블록 내 키-값 쌍을 파이프(|) 기호로 분리합니다.
     *
     * @param block 블록 문자열
     * @return 키-값 쌍 배열
     */
    private static String[] splitByPipe(String block) {
        return block.split("\\|");
    }

    /**
     * 원재료 정보를 세미콜론(;) 기준으로 블록으로 분리합니다.
     *
     * @param raw 원재료 문자열
     * @return 블록 배열
     */
    private static String[] splitBlock(String raw) {
        return raw.split(";");
    }
}
