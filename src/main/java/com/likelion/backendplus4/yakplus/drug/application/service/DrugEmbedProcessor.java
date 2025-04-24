package com.likelion.backendplus4.yakplus.drug.application.service;

import org.springframework.stereotype.Service;

import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.embedding.EmbeddingAdapter;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugDetailJpaRepository;

@Service
public class DrugEmbedProcessor {
	EmbeddingAdapter adapter;
	GovDrugDetailJpaRepository govDrugDetailJpaRepository;

	public void startEmbedding(){


	}
}
