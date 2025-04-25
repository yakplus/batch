package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.embedding.client;

import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.embedding.EmbeddingModelType;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.api.ApiUriCompBuilder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KrSbertEmbeddingClient implements EmbeddingClient {
	private final ApiUriCompBuilder apiUriCompBuilder;
	private final RestTemplate restTemplate;

	@Override
	public EmbeddingModelType getModelType() {
		return EmbeddingModelType.SBERT;
	}

	@Override
	public float[] getEmbedding(String text) {
		URI embeddingURI = getEmbeddingURI();
		return getEmbeddingVetor(embeddingURI, text);
	}

	private float[] getEmbeddingVetor(URI embedUri, String text) {
		EmbeddingRequestText embeddingRequestText = new EmbeddingRequestText();
		embeddingRequestText.setText(text);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<EmbeddingRequestText> request = new HttpEntity<>(embeddingRequestText, headers);
		return restTemplate.postForObject(embedUri, request, float[].class);
	}

	private URI getEmbeddingURI() {
		return apiUriCompBuilder.getUriForKrSbertEmbeding();
	}
}
