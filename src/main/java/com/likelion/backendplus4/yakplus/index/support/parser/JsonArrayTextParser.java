package com.likelion.backendplus4.yakplus.index.support.parser;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 최상위 JSON 배열에서 각 요소(문자열)을 추출하고,
 * 정규식을 이용해 HTML 태그 제거, HTML 엔티티(예: &#8226;, &#x2022;) 디코딩, &nbsp; 등을 제거한 뒤
 * 깨끗한 텍스트 리스트를 반환하는 유틸리티 클래스입니다.
 *
 * <p>예시 JSON: ["<b>첫번째</b> 텍스트&#8226;", "텍스트&nbsp;예시"]</p>
 * <p>→ 리턴: ["첫번째 텍스트•", "텍스트 예시"]</p>
 *
 * @author 박찬병
 * @since 2025-04-27
 * @modified 2025-04-27
 */
public class JsonArrayTextParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    // HTML 태그 제거용 정규식
    private static final Pattern TAG_REGEX = Pattern.compile("<[^>]+>");
    // 10진수 HTML 엔티티 디코딩용 정규식 (예: &#8226;)
    private static final Pattern DECIMAL_ENTITY_REGEX = Pattern.compile("&#(\\d+);");
    // 16진수 HTML 엔티티 디코딩용 정규식 (예: &#x2022;)
    private static final Pattern HEX_ENTITY_REGEX = Pattern.compile("&#x([0-9A-Fa-f]+);");

    /**
     * JSON 문자열 최상위가 배열일 때, 각 요소를 텍스트로 파싱하고 HTML 태그, HTML 엔티티, &nbsp; 등을 제거하여 리스트로 반환합니다.
     *
     * @param json JSON 배열 형태의 문자열
     * @return 정제된 텍스트 리스트
     * @throws IOException JSON 파싱 실패 시 발생
     */
    public static List<String> extractAndClean(String json) throws IOException {
        JsonNode root = objectMapper.readTree(json);
        List<String> texts = new ArrayList<>();

        if (!root.isArray()) {
            return texts;
        }

        for (JsonNode element : root) {
            if (element.isTextual()) {
                String raw = element.asText().trim();
                if (raw.isEmpty()) {
                    continue;
                }

                // 1) HTML 태그 제거
                String noHtml = TAG_REGEX.matcher(raw).replaceAll("");
                // 2) &nbsp; 등을 일반 공백으로 치환
                String withSpaces = noHtml.replaceAll("&nbsp;", " ");
                // 3) HTML 엔티티 디코딩 (10진수 및 16진수)
                String decoded = decodeHtmlEntities(withSpaces);
                // 4) 최종 트리밍
                String clean = decoded.trim();

                if (!clean.isEmpty()) {
                    texts.add(clean);
                }
            }
        }
        return texts;
    }

    /**
     * HTML 엔티티(10진수 &#DDD; 및 16진수 &#xHHHH;)를 대응하는 문자로 디코딩합니다. 예: "foo&#8226;bar" → "foo•bar",
     * "foo&#x2022;bar" → "foo•bar"
     *
     * @param input 엔티티를 포함한 문자열
     * @return 디코딩된 문자열
     */
    private static String decodeHtmlEntities(String input) {
        String result = input;

        // 10진수 엔티티 디코딩
        Matcher decMatcher = DECIMAL_ENTITY_REGEX.matcher(result);
        StringBuffer sb = new StringBuffer();
        while (decMatcher.find()) {
            int code = Integer.parseInt(decMatcher.group(1));
            decMatcher.appendReplacement(sb,
                    Matcher.quoteReplacement(Character.toString((char) code)));
        }
        decMatcher.appendTail(sb);
        result = sb.toString();

        // 16진수 엔티티 디코딩
        Matcher hexMatcher = HEX_ENTITY_REGEX.matcher(result);
        sb = new StringBuffer();
        while (hexMatcher.find()) {
            int code = Integer.parseInt(hexMatcher.group(1), 16);
            hexMatcher.appendReplacement(sb,
                    Matcher.quoteReplacement(Character.toString((char) code)));
        }
        hexMatcher.appendTail(sb);
        return sb.toString();
    }
}