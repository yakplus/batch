package com.likelion.backendplus4.yakplus.symptomdictionary.infrastructure.support.mapper;

import com.likelion.backendplus4.yakplus.symptomdictionary.infrastructure.persistence.repository.document.SymptomDictionaryDocument;
import com.likelion.backendplus4.yakplus.symptomdictionary.infrastructure.persistence.repository.entity.SymptomDictionary;
import java.util.List;

/**
 * 증상 사전 엔티티 및 문서 객체 간 변환을 담당하는 매퍼 클래스입니다.
 *
 *
 * @since 2025-05-01
 * @modified 2025-05-01
 */
public class DictionaryMapper {

    /**
     * 단일 증상명을 JPA 엔티티로 변환합니다.
     *
     * @param name 증상명 문자열
     * @return SymptomDictionary JPA 엔티티
     * @author 박찬병
     * @since 2025-05-01
     * @modified 2025-05-01
     */
    public static SymptomDictionary toSymptomEntity(String name) {
        return SymptomDictionary.builder()
                .name(name)
                .build();
    }

    /**
     * 단일 증상명을 Elasticsearch 색인용 문서로 변환합니다.
     *
     * @param name 증상명 문자열    * @return SymptomDictionaryDocument ES 문서 객체
     * @author 박찬병
     * @since 2025-05-01
     * @modified 2025-05-01
     */
    public static SymptomDictionaryDocument toSymptomDocument(String name) {
        return SymptomDictionaryDocument.builder()
                .symptom(name)
                .symptomSuggester(List.of(name))
                .build();
    }
}