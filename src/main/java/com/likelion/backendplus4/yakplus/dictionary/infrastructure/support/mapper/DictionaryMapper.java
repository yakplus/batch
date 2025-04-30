package com.likelion.backendplus4.yakplus.dictionary.infrastructure.support.mapper;

import com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.repository.document.SymptomDictionaryDocument;
import com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.repository.entity.SymptomDictionary;
import java.util.List;

public class DictionaryMapper {

    public static SymptomDictionary toEntity(String name){
        return new SymptomDictionary(null, name);
    }

    public static SymptomDictionaryDocument toDocument(String name) {
        return SymptomDictionaryDocument.builder()
                .symptom(name)
                .symptomSuggester(List.of(name))
                .build();
    }

}
