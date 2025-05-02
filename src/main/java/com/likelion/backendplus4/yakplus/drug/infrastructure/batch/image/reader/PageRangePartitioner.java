package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.image.reader;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
public class PageRangePartitioner implements Partitioner {
	private int totalPages = 0;

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		int range = totalPages / gridSize;
		Map<String, ExecutionContext> result = new HashMap<>();

		for (int i = 0; i < gridSize; i++) {
			int start = i * range + 1;
			int end = (i == gridSize - 1) ? totalPages : start + range - 1;

			ExecutionContext context = new ExecutionContext();
			context.putInt("startPage", start);
			context.putInt("endPage", end);

			result.put("partition" + i, context);
		}

		return result;
	}
}
