package com.likelion.backendplus4.yakplus.drug.application.service.port.out;

import java.util.List;

import com.likelion.backendplus4.yakplus.drug.domain.model.DrugImage;

public interface DrugImageRepositoryPort {
	List<DrugImage> getAllGovDrugImage();

	DrugImage getById(Long drugId);

	void saveAllAndFlush(List<DrugImage> imgData);
}
