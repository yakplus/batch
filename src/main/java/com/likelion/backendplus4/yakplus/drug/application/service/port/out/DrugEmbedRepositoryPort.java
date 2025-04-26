package com.likelion.backendplus4.yakplus.drug.application.service.port.out;

public interface DrugEmbedRepositoryPort {
	void saveGptEmbed(Long drugId, float[] vector);

	void saveKmBertEmbed(Long drugId, float[] gptVector);

	void saveKrSbertEmbed(Long drugId, float[] krSbertVector);

	float[] getGptVector(Long drugId);

	float[] getKmBertVector(Long drugId);

	float[] getKrSbertVector(Long drugId);
}
