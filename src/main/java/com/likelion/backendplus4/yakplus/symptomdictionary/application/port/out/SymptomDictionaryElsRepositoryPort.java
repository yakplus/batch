package com.likelion.backendplus4.yakplus.symptomdictionary.application.port.out;

import java.util.Set;

public interface SymptomDictionaryElsRepositoryPort {

    /**
     * 주어진 증상 단어 리스트를 바탕으로 Elasticsearch에 신규 증상 사전 문서를 일괄 저장합니다.
     * 1) DictionaryMapper를 통해 Document 객체로 변환한 뒤,
     * 2) repository.saveAll을
     * 호출하여 일괄 색인합니다.
     *
     * @param symptoms 저장할 증상 단어 리스트
     * @author 박찬병
     * @modified 2025-05-01
     * @since 2025-04-30
     */
    void setDictionary(Set<String> symptoms);
}
