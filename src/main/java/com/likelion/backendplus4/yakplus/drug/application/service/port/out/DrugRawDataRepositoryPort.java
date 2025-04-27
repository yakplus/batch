package com.likelion.backendplus4.yakplus.drug.application.service.port.out;

import com.likelion.backendplus4.yakplus.drug.domain.model.DrugRawData;

public interface DrugRawDataRepositoryPort {
	void save(DrugRawData rawData);
}
