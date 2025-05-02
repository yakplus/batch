package com.likelion.backendplus4.yakplus.index.application.port.out;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GovDrugRawDataPort {
    List<Drug> fetchRawData(int pageNo, int numOfRows);

    Page<Drug> findAllDrugs(Pageable pageable);

    long getDrugTotalSize();
}