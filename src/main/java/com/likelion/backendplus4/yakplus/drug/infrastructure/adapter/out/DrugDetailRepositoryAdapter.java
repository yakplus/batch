package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.out;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugDetailRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrugDetail;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.out.dto.DrugDetailRequest;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.out.mapper.DrugDetailRequestMapper;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugDetailJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper.DrugDetailMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DrugDetailRepositoryAdapter implements DrugDetailRepositoryPort {
	private final GovDrugDetailJpaRepository drugdetailJpaRepository;

	@Override
	public void saveDrugDetail(DrugDetailRequest e){
		drugdetailJpaRepository.save(DrugDetailRequestMapper.toEntityFromRequest(e));
	}

	@Override
	public void saveDrugDetailBulk(List<DrugDetailRequest> list){
		drugdetailJpaRepository.saveAll(list.stream()
			.map(DrugDetailRequestMapper::toEntityFromRequest)
			.collect(Collectors.toList()));
		drugdetailJpaRepository.flush();
	}

	@Override
	public List<GovDrugDetail> getAllGovDrugDetail(){
		return drugdetailJpaRepository.findAll().stream()
			.map(DrugDetailMapper::toDomainFromEntity)
			.collect(Collectors.toList());
	}
}
