package com.likelion.backendplus4.yakplus.drug.infrastructure.embedding.adapter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.likelion.backendplus4.yakplus.drug.application.service.port.out.EmbeddingPort;
import com.likelion.backendplus4.yakplus.drug.infrastructure.embedding.model.EmbeddingModelType;
import com.likelion.backendplus4.yakplus.drug.infrastructure.embedding.client.EmbeddingClient;

@Service
public class EmbeddingAdapter implements EmbeddingPort {
	private final Map<EmbeddingModelType, EmbeddingClient> embeddingClientMap;

	public EmbeddingAdapter(List<EmbeddingClient> clients) {
		this.embeddingClientMap = clients.stream()
			.collect(Collectors.toMap(EmbeddingClient::getModelType, client -> client));
	}
	@Override
	public float[] getEmbedding(String text, EmbeddingModelType embeddingModelType) {
		EmbeddingClient client = embeddingClientMap.get(embeddingModelType);

		return client.getEmbedding(text);
	}
}
