package com.likelion.backendplus4.yakplus.dictionary.infrastructure.support.mapper;

import com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.repository.entity.SymptomDictionary;

public class DictionaryMapper {

    public static SymptomDictionary toEntity(String name){
        return new SymptomDictionary(null, name);
    }

}
