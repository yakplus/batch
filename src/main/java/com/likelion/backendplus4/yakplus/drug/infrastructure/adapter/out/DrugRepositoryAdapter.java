package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.out;

import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugJpaRepository;
import com.likelion.backendplus4.yakplus.index.domain.model.Drug;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DrugRepositoryAdapter implements DrugRepositoryPort {

	private final GovDrugJpaRepository jpaDrugRepository;

	public void saveDrug(Drug drug){
		//TODO save
	}
}
