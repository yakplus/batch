package com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence.repository;

import com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence.repository.document.DrugSymptomDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DrugSymptomRepository extends ElasticsearchRepository<DrugSymptomDocument, String> {
}
