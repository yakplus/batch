package com.likelion.backendplus4.yakplus.index.application.port.out;

import com.likelion.backendplus4.yakplus.index.domain.model.Drug;

import java.util.List;
import org.springframework.data.domain.Page;

public interface DrugIndexRepositoryPort {
    void saveAll(String esIndexName, List<Drug> drugs);

    void saveAllSymptom(Page<Drug> drugPage);
}