package com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.adapter.out;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugDetailRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.domain.model.DrugDetail;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.dto.DrugDetailRequest;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper.DrugDetailRequestMapper;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugDetailJpaRepository;
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
	public List<DrugDetail> getAllGovDrugDetail(){
		return drugdetailJpaRepository.findAll().stream()
			.map(DrugDetailMapper::toDomainFromEntity)
			.collect(Collectors.toList());
	}

	@Override
	public Page<DrugDetail> getGovDrugDetailByPage(Pageable pageable) {
		return drugdetailJpaRepository.findAll(pageable)
			.map(DrugDetailMapper::toDomainFromEntity);
	}
}
