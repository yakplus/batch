package com.likelion.backendplus4.yakplus.drug.application.service.scraper.combiner;

import java.util.List;

import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugCombineUsecase;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugDetailRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugImageRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugRawDataRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.domain.model.DrugImage;
import com.likelion.backendplus4.yakplus.drug.domain.model.DrugRawData;
import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrugDetail;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DrugCombiner implements DrugCombineUsecase {
	private final DrugDetailRepositoryPort drugDetailRepository;
	private final DrugImageRepositoryPort drugImageRepositoryPort;
	private final DrugRawDataRepositoryPort	drugRawDataRepositoryPort;

	@Transactional
	@Override
	public void mergeTable(){
		List<GovDrugDetail> drugDetails = drugDetailRepository.getAllGovDrugDetail();

		for (GovDrugDetail d : drugDetails) {
			DrugImage i = drugImageRepositoryPort.getById(d.getDrugId());
			DrugRawData rawData = DrugRawData.builder()
				.drugId(d.getDrugId())
				.drugName(d.getDrugName())
				.company(d.getCompany())
				.permitDate(d.getPermitDate())
				.isGeneral(d.isGeneral())
				.materialInfo(d.getMaterialInfo())
				.storeMethod(d.getStoreMethod())
				.validTerm(d.getValidTerm())
				.efficacy(d.getEfficacy())
				.usage(d.getUsage())
				.precaution(d.getPrecaution())
				.imageUrl(i.getImageUrl())
				.build();
			drugRawDataRepositoryPort.save(rawData);
		}
	}
}
