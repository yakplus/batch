package com.likelion.backendplus4.yakplus.drug.application.service.port.in;

import jakarta.transaction.Transactional;

public interface DrugCombineUsecase {
	@Transactional
	void mergeTable();
}
