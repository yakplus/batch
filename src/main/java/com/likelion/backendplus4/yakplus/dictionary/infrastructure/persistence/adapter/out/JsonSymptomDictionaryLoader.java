package com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.adapter.out;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.dictionary.application.port.out.SymptomDictionaryLoaderPort;
import com.likelion.backendplus4.yakplus.dictionary.exception.DictionaryException;
import com.likelion.backendplus4.yakplus.dictionary.exception.error.DictionaryErrorCode;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * classpath에 위치한 JSON 파일로부터 증상 사전 데이터를 로드하는 클래스입니다.
 * SymptomDictionaryLoaderPort를 구현하여 JSON 파싱 로직을 캡슐화합니다.
 *
 * @since 2025-04-30
 * @modified 2025-05-01
 */
@RequiredArgsConstructor
@Component
public class JsonSymptomDictionaryLoader implements SymptomDictionaryLoaderPort {

    private final ObjectMapper objectMapper;
    private static final String DICT_PATH = "unique_symptoms.json";

    /**
     * ClassPathResource를 통해 지정된 JSON 파일을 읽고,
     * List<String> 형태로 변환하여 반환합니다.
     *
     * @return JSON에 정의된 증상 문자열 리스트
     * @throws DictionaryException 파일 형식 오류 또는 파싱/IO 실패 시 발생
     * @since 2025-04-30
     * @author 박찬병
     * @modified 2025-05-01
     * */
    @Override
    public List<String> loadDictionary() {
        log("JsonSymptomDictionaryLoader.loadDictionary() 호출, 경로: " + DICT_PATH);

        // 1) 확장자 검증
        if (!DICT_PATH.toLowerCase().endsWith(".json")) {
            log(LogLevel.ERROR, "loadDictionary() 파일 형식 오류: JSON(.json) 파일만 허용됩니다.");
            throw new DictionaryException(DictionaryErrorCode.INVALID_FILE_TYPE);
        }

        ClassPathResource resource = new ClassPathResource(DICT_PATH);
        try (InputStream in = resource.getInputStream()) {
            // 2) JSON 읽기 및 파싱
            List<String> symptoms = objectMapper.readValue(in, new TypeReference<List<String>>() {});
            log("loadDictionary() 완료, 로드된 증상 수: " + symptoms.size());
            return symptoms;
        } catch (IOException e) {
            log(LogLevel.ERROR, "loadDictionary() 증상 사전 로드 실패", e);
            throw new DictionaryException(DictionaryErrorCode.DICTIONARY_LOAD_FAILURE);
        }
    }
}