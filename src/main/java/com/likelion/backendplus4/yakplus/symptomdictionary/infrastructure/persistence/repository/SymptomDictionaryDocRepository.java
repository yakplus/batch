package com.likelion.backendplus4.yakplus.symptomdictionary.infrastructure.persistence.repository;

import com.likelion.backendplus4.yakplus.symptomdictionary.infrastructure.persistence.repository.document.SymptomDictionaryDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SymptomDictionaryDocRepository extends ElasticsearchRepository<SymptomDictionaryDocument, String> {
}
