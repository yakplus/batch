package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.embed.reader;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugJpaRepository;

@Component
public class DrugIdRangePartitioner implements Partitioner {

	private final GovDrugJpaRepository repository;

	public DrugIdRangePartitioner(GovDrugJpaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		Long minId = repository.findMinDrugId();
		Long maxId = repository.findMaxDrugId();

		long targetSize = (maxId - minId) / gridSize + 1;

		Map<String, ExecutionContext> result = new HashMap<>();
		long start = minId;
		long end = start + targetSize - 1;

		for (int i = 0; i < gridSize; i++) {
			ExecutionContext context = new ExecutionContext();
			context.putLong("minId", start);
			context.putLong("maxId", Math.min(end, maxId));
			result.put("partition" + i, context);
			start += targetSize;
			end += targetSize;
		}
		return result;
	}
}