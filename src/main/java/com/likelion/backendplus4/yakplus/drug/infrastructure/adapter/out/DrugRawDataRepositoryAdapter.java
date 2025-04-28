package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.out;

import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugRawDataRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.domain.model.DrugRawData;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper.DrugRawDataMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DrugRawDataRepositoryAdapter implements DrugRawDataRepositoryPort {

	private final GovDrugJpaRepository jpaDrugRepository;

	@Override
	public void save(DrugRawData drug){
		DrugRawDataEntity entity = DrugRawDataMapper.toEntityFromDomain(drug);
		jpaDrugRepository.save(entity);
	}
}
