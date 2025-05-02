package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.combine.processor;

import java.util.ArrayList;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@StepScope
@RequiredArgsConstructor
public class TableCombineWriter implements ItemWriter<DrugRawDataEntity> {
	private final GovDrugJpaRepository drugRawDataRepository;

	@Override
	public void write(Chunk<? extends DrugRawDataEntity> entity) {
		drugRawDataRepository.saveAll(new ArrayList<>(entity.getItems()));
	}
}
