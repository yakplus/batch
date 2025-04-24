package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.embedding.client;

import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.embedding.EmbeddingModelType;

public interface EmbeddingClient {
	EmbeddingModelType getModelType();
	float[] getEmbedding(String text);
}
