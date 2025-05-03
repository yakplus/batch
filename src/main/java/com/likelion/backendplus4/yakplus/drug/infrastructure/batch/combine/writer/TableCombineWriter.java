package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.combine.writer;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.common.util.log.LogUtil;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@StepScope
@RequiredArgsConstructor
public class TableCombineWriter implements ItemWriter<DrugRawDataEntity> {
	private final GovDrugJpaRepository drugRawDataRepository;
	private AtomicInteger count = new AtomicInteger();

	@Override
	public void write(Chunk<? extends DrugRawDataEntity> entity) {
		List<DrugRawDataEntity> items = new ArrayList<>(entity.getItems());
		drugRawDataRepository.saveAll(items);
		log("테이블 병합 작업 - 쓰기 완료: " + count.addAndGet(items.size()));

	}
}
