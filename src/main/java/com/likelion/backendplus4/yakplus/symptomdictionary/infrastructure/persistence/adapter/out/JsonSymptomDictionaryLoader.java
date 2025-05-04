package com.likelion.backendplus4.yakplus.symptomdictionary.infrastructure.persistence.adapter.out;

import static com.likelion.backendplus4.yakplus.common.logging.util.LogUtil.log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.common.logging.util.LogLevel;
import com.likelion.backendplus4.yakplus.symptomdictionary.application.port.out.SymptomDictionaryLoaderPort;
import com.likelion.backendplus4.yakplus.symptomdictionary.exception.DictionaryException;
import com.likelion.backendplus4.yakplus.symptomdictionary.exception.error.DictionaryErrorCode;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * classpath에 위치한 JSON 파일로부터 증상 사전 데이터를 로드하는 클래스입니다.
 * SymptomDictionaryLoaderPort를 구현하여 JSON 파싱 로직을 캡슐화합니다.
 *
 * @since 2025-04-30
 * @modified 2025-05-03
 */
@RequiredArgsConstructor
@Component
public class JsonSymptomDictionaryLoader implements SymptomDictionaryLoaderPort {

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;


    @Value("${dictionary.path}")
    private String DICTIONARY_FILE_PATH;

    /**
     * ClassLoader를 통해 지정된 JSON 파일을 읽고,
     * Set<String> 형태로 변환하여 반환합니다.
     *
     * @return JSON에 정의된 증상 문자열 리스트
     * @throws DictionaryException 파일 형식 오류 또는 파싱/IO 실패 시 발생
     * @since 2025-04-30
     * @author 박찬병
     * @modified 2025-05-04
     */
    @Override
    public Set<String> loadDictionary() {
        log("loadDictionary() 호출 - 경로: " + DICTIONARY_FILE_PATH);

        if (!DICTIONARY_FILE_PATH.toLowerCase().endsWith(".json")) {
            throw new DictionaryException(DictionaryErrorCode.INVALID_FILE_TYPE);
        }

        try (InputStream in = resourceLoader.getResource(DICTIONARY_FILE_PATH).getInputStream()) {
            Set<String> result = objectMapper.readValue(in, new TypeReference<>() {});
            log("사전 로딩 완료 - 크기: " + result.size());
            return result;
        } catch (IOException e) {
            log(LogLevel.ERROR, "사전 파일 로딩 실패", e);
            throw new DictionaryException(DictionaryErrorCode.DICTIONARY_LOAD_FAILURE);
        }

    }
}