package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.embedding.client;

import java.net.URI;

import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.web.client.RestTemplate;

import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.embedding.EmbeddingModelType;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.api.ApiUriCompBuilder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KmBertEmbeddingClient implements EmbeddingClient {
	private final ApiUriCompBuilder apiUriCompBuilder;
	private final RestTemplate restTemplate;

	@Override
	public EmbeddingModelType getModelType() {
		return EmbeddingModelType.KM_BERT;
	}

	@Override
	public float[] getEmbedding(String text) {
		URI embeddingURI = getEmbeddingURI();
		getEmbeddingVetor(embeddingURI, text);
		return new float[0];
	}

	private float[] getEmbeddingVetor(URI embedUri, String text) {
		EmbeddingRequestText embeddingRequestText = new EmbeddingRequestText(text);
		return restTemplate.postForObject(embedUri, embeddingRequestText, float[].class);
	}

	private URI getEmbeddingURI() {
		return apiUriCompBuilder.getUriForKmbertEmbeding();
	}
}
