package com.likelion.backendplus4.yakplus.temp.infrastructure.adapter.persistence.repository;

import com.likelion.backendplus4.yakplus.temp.infrastructure.adapter.persistence.document.DrugKeywordDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DrugKeywordRepository extends ElasticsearchRepository<DrugKeywordDocument, Long> {

}
