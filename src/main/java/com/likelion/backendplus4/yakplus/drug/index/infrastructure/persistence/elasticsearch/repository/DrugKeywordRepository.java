package com.likelion.backendplus4.yakplus.drug.index.infrastructure.persistence.elasticsearch.repository;

import com.likelion.backendplus4.yakplus.drug.index.infrastructure.persistence.elasticsearch.document.DrugKeywordDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DrugKeywordRepository extends ElasticsearchRepository<DrugKeywordDocument, Long> {

}
