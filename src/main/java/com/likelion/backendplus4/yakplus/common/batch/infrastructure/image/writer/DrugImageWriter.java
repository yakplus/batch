package com.likelion.backendplus4.yakplus.common.batch.infrastructure.image.writer;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.*;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.ApiDataDrugImgEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.ApiDataDrugImgRepo;

/**
 * Entity 리스트를 받아 JPA Repository로 한 번에 저장하는 Writer입니다.
 * 
 * @field repository 이미지 정보를 저장하는 JPA Repository
 * @since 2025-05-02
 */
public class DrugImageWriter implements ItemWriter<List<ApiDataDrugImgEntity>> {

	private final ApiDataDrugImgRepo repository;

	public DrugImageWriter(ApiDataDrugImgRepo repository) {
		this.repository = repository;
	}

	/**
	 * Chunk 단위로 전달된 ApiDataDrugImgEntity 리스트를 JPA Repository에 저장합니다.
	 * 
	 * @param chunk 저장할 ApiDataDrugImgEntity 리스트를 포함하는 Chunk
	 * @author 함예정   
	 * @since 2025-05-02   
	 */
	@Override
	public void write(Chunk<? extends List<ApiDataDrugImgEntity>> chunk) throws Exception {
		log(Thread.currentThread().getName() + " - Start Write");
		for (List<ApiDataDrugImgEntity> items : chunk.getItems()) {
			repository.saveAllAndFlush(items);
		}
	}
}
