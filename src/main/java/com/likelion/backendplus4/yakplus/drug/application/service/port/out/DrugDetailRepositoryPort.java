package com.likelion.backendplus4.yakplus.drug.application.service.port.out;

import java.util.List;

import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrugDetail;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.out.dto.DrugDetailRequest;

public interface DrugDetailRepositoryPort {
	void saveDrugDetail(DrugDetailRequest e);

	void saveDrugDetailBulk(List<DrugDetailRequest> list);

	List<GovDrugDetail> getAllGovDrugDetail();
}
