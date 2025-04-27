package com.likelion.backendplus4.yakplus.index.application.port.out;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;

import java.util.List;

public interface EmbeddingLoadingPort {
    List<Drug> loadAllEmbeddings();
}
