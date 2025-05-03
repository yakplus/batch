package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.image.writer;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.*;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.ApiDataDrugImgEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.ApiDataDrugImgRepo;

/**
 * Entity 리스트를 받아 JPA Repository로 한 번에 저장
 */
public class DrugImageWriter implements ItemWriter<List<ApiDataDrugImgEntity>> {

	private final ApiDataDrugImgRepo repository;

	public DrugImageWriter(ApiDataDrugImgRepo repository) {
		this.repository = repository;
	}

	@Override
	public void write(Chunk<? extends List<ApiDataDrugImgEntity>> chunk) throws Exception {
		log(Thread.currentThread().getName() + " - Start Write");
		for (List<ApiDataDrugImgEntity> items : chunk.getItems()) {
			repository.saveAllAndFlush(items);
		}
	}
}
