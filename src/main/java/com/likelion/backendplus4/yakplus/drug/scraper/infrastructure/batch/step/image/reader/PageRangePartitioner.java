package com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.batch.step.image.reader;

import java.util.HashMap;
import java.util.Map;

import lombok.Setter;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

/**
 * PageRangePartitioner는 페이지 범위를 기준으로 파티션을 나누는 클래스입니다.
 * 주어진 총 페이지 수를 gridSize에 따라 나누어 각 파티션의 시작과 끝 페이지를 설정합니다.
 *
 * @field totalPages 총 페이지 수
 * @since 2025-05-02
 */
@Component
@Setter
public class PageRangePartitioner implements Partitioner {
	private int totalPages = 0;

	/**
	 * partition 메서드는 주어진 gridSize에 따라 페이지 범위를 나누어
	 * 각 파티션의 시작과 끝 페이지를 설정합니다.
	 *
	 * @param gridSize 파티션의 개수
	 * @return 각 파티션의 시작과 끝 페이지를 포함하는 맵
	 */
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
