package com.likelion.backendplus4.yakplus.drug.infrastructure.embedding.client;

import java.util.List;

import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.infrastructure.embedding.model.EmbeddingModelType;

@Component
public class OpenaiEmbeddingClient implements EmbeddingClient {
	private final OpenAiApi openAiApi;

	public OpenaiEmbeddingClient(OpenAiApi openAiApi) {
		this.openAiApi = openAiApi;
	}

	@Override
	public EmbeddingModelType getModelType() {
		return EmbeddingModelType.OPENAI;
	}

	@Override
	public float[] getEmbedding(String text) {
		OpenAiEmbeddingModel openAiEmbeddingModel = new OpenAiEmbeddingModel(
			this.openAiApi,
			MetadataMode.EMBED,
			OpenAiEmbeddingOptions.builder()
				.model("text-embedding-3-small")
				.build(),
			RetryUtils.DEFAULT_RETRY_TEMPLATE);

		EmbeddingResponse embeddingResponse = openAiEmbeddingModel
			.embedForResponse(List.of(text));

		Embedding embedding = embeddingResponse.getResults().getFirst();
		return embedding.getOutput();
	}
}
