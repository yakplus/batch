package com.likelion.backendplus4.yakplus.index.application.port.out;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmbeddingLoadingPort {
    List<Drug> loadEmbeddingsByPage(Pageable pageable);

    float[] getEmbedding(String text);

    void saveEmbedding(Long drugId, float[] embedding);
}
