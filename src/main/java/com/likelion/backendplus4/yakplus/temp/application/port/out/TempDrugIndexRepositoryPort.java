package com.likelion.backendplus4.yakplus.temp.application.port.out;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import org.springframework.data.domain.Page;

public interface TempDrugIndexRepositoryPort {

    void saveAllSymptom(Page<Drug> drugPage);


}
