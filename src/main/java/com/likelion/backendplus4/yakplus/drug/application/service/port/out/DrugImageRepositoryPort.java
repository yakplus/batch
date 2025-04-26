package com.likelion.backendplus4.yakplus.drug.application.service.port.out;

import java.util.List;
import java.util.Optional;

import com.likelion.backendplus4.yakplus.drug.domain.model.DrugImage;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.ApiDataDrugImgEntity;

public interface DrugImageRepositoryPort {
	List<DrugImage> getAllGovDrugDetail();

	DrugImage getById(Long drugId);
}
