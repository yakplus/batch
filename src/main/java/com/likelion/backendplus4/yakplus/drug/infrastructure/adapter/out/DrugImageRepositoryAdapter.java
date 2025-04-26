package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.out;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugImageRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.domain.model.DrugImage;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.ApiDataDrugImgEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.ApiDataDrugImgRepo;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper.DrugImageMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DrugImageRepositoryAdapter implements DrugImageRepositoryPort {
	private final ApiDataDrugImgRepo imageRepository;

	@Override
	public List<DrugImage> getAllGovDrugImage(){
		return imageRepository.findAll().stream()
			.map(DrugImageMapper::toDomainFromEntity)
			.collect(Collectors.toList());
	}

	@Override
	public DrugImage getById(Long drugId) {
		return imageRepository.findById(drugId)
			.map(DrugImageMapper::toDomainFromEntity)
			.orElseGet(() -> getDefaultDomain());
	}

	@Override
	public void saveAllAndFlush(List<DrugImage> imgData) {
		imageRepository.saveAll(DrugImageMapper.toEntityListFromDomainList(imgData));
		imageRepository.flush();
	}

	private static DrugImage getDefaultDomain() {
		return DrugImageMapper.toDomainFromEntity(new ApiDataDrugImgEntity());
	}
}
