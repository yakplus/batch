package com.likelion.backendplus4.yakplus.index.application.port.out;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.embed.dto.DrugVectorDto;

public interface EmbeddingLoadingPort {
	List<Drug> loadEmbeddingsByPage(Pageable pageable);

	float[] getEmbedding(String text);

	void saveEmbedding(List<DrugVectorDto> dtos);
}
