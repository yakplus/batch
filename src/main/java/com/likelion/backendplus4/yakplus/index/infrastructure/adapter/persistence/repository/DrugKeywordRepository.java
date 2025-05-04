package com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence.repository;

import com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence.repository.document.DrugKeywordDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DrugKeywordRepository extends ElasticsearchRepository<DrugKeywordDocument, Long> {

}
