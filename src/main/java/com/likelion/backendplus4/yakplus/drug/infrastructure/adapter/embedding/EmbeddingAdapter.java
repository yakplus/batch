package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.embedding;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.stereotype.Service;

import com.likelion.backendplus4.yakplus.drug.domain.model.port.out.EmbeddingPort;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.embedding.client.EmbeddingClient;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.embedding.client.OpenaiEmbeddingClient;

import lombok.RequiredArgsConstructor;

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
