package com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.adapter.out;

import java.util.List;

import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugRawDataRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.domain.model.DrugRawData;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugJpaRepository;
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

	@Override
	public void saveAll(List<DrugRawData> rawData) {
		jpaDrugRepository.saveAll(
			rawData.stream()
				   .map(DrugRawDataMapper::toEntityFromDomain).toList()
		);
		jpaDrugRepository.flush();
	}
}
