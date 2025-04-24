package com.likelion.backendplus4.yakplus.drug.domain.model.port.out;

import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.embedding.EmbeddingModelType;

public interface EmbeddingPort {
	float[] getEmbedding(String text, EmbeddingModelType modelType); //문자 embedding 하여 float 배열로 반환
}
