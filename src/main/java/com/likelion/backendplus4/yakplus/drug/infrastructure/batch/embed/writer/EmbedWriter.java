package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.embed.writer;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.embed.dto.DrugVectorDto;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugJpaRepository;
import com.likelion.backendplus4.yakplus.index.application.port.out.EmbeddingLoadingPort;

import lombok.RequiredArgsConstructor;

@Component
@StepScope
@RequiredArgsConstructor
public class EmbedWriter implements ItemWriter<DrugVectorDto> {
	private final EmbeddingLoadingPort embeddingLoadingPort;
	private AtomicInteger count = new AtomicInteger();

	@Override
	public void write(Chunk<? extends DrugVectorDto> dto) throws Exception {
		List<DrugVectorDto> items = new ArrayList<>(dto.getItems());
		embeddingLoadingPort.saveEmbedding(items);
		log("임베딩 작업 - 쓰기 완료: " + count.addAndGet(items.size()));
	}
}
