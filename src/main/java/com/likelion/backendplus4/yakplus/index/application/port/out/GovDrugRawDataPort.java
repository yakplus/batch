package com.likelion.backendplus4.yakplus.index.application.port.out;

import com.likelion.backendplus4.yakplus.index.domain.model.Drug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GovDrugRawDataPort {
    List<Drug> fetchRawData(Long lastSeq, Pageable pageable);
    String getEsIndexName();

    Page<Drug> findAllDrugs(Pageable pageable);
}