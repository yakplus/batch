package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.detail.writer;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugDetailEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugDetailJpaRepository;

/**
 * Entity 리스트를 받아 JPA Repository로 한 번에 저장
 */
public class DrugDetailWriter implements ItemWriter<List<DrugDetailEntity>> {

	private final GovDrugDetailJpaRepository repository;

	public DrugDetailWriter(GovDrugDetailJpaRepository repository) {
		this.repository = repository;
	}

	@Override
	public void write(Chunk<? extends List<DrugDetailEntity>> chunk) throws Exception {

		for (List<DrugDetailEntity> items : chunk.getItems()) {
			repository.saveAll(items);
		}
	}
}
