package com.likelion.backendplus4.yakplus.drug.application.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrug;

public interface DrugDataService {
	List<GovDrug> findAllRawDrug(Pageable pageable);
}
