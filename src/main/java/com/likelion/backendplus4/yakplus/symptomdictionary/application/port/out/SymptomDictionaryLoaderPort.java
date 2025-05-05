package com.likelion.backendplus4.yakplus.symptomdictionary.application.port.out;

import com.likelion.backendplus4.yakplus.symptomdictionary.exception.DictionaryException;

import java.util.Set;

public interface SymptomDictionaryLoaderPort {

    /**
     * ClassPathResource를 통해 지정된 JSON 파일을 읽고,
     * Set<String> 형태로 변환하여 반환합니다.
     *
     * @return JSON에 정의된 증상 문자열 리스트
     * @throws DictionaryException 파일 형식 오류 또는 파싱/IO 실패 시 발생
     * @since 2025-04-30
     * @author 박찬병
     * @modified 2025-05-01
     */
    Set<String> loadDictionary();

}
