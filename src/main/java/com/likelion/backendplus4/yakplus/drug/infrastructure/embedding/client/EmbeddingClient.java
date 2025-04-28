package com.likelion.backendplus4.yakplus.drug.infrastructure.embedding.client;

import com.likelion.backendplus4.yakplus.drug.infrastructure.embedding.model.EmbeddingModelType;

public interface EmbeddingClient {
	EmbeddingModelType getModelType();
	float[] getEmbedding(String text);
}
