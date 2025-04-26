package com.likelion.backendplus4.yakplus.drug.application.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugJpaRepository;
import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrug;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class DrugDataServiceImpl implements DrugDataService {
	private final GovDrugJpaRepository govDrugJpaRepository;

	@Override
	public List<GovDrug> findAllRawDrug(Pageable pageable) {
		log.info("findAllRawDrug called");
		//TODO: Detail을 전처리에서 미리 텍스트를 plain으로 만들고 통합 테이블 가져오는 것 다시 제작 필요
		return null;
		// return govDrugJpaRepository.findAll(pageable).stream()
		// 	.map(DrugDetailMapper::toDomainFromEntity)
		// 	.collect(toList());
	}
}
