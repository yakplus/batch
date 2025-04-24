package com.likelion.backendplus4.yakplus.drug.domain.model.port.out;

public interface EmbeddingPort {
	float[] getEmbedding(String text); //문자 embedding 하여 float 배열로 반환
}
