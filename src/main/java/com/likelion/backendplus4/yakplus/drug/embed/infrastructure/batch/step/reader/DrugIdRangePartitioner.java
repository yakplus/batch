package com.likelion.backendplus4.yakplus.drug.embed.infrastructure.batch.step.reader;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.persistence.repository.jpa.repository.DrugJpaRepository;

/**
 * 의약품 ID 범위를 기반으로 파티션을 분할하는 Partitioner 구현 클래스입니다.
 * <p>
 * GovDrugJpaRepository를 통해 drugId의 최소/최대 값을 조회하고,
 * 주어진 gridSize에 따라 각 파티션의 범위를 계산하여 ExecutionContext에 전달합니다.
 * 각 Step은 이 ExecutionContext를 통해 자신의 ID 범위를 인식하고 독립적으로 데이터를 처리할 수 있습니다.
 */
@Component
public class DrugIdRangePartitioner implements Partitioner {

	private final DrugJpaRepository repository;

	public DrugIdRangePartitioner(DrugJpaRepository repository) {
		this.repository = repository;
	}

	/**
	 * 의약품 ID 범위를 기준으로 주어진 gridSize만큼 파티션을 나누어 ExecutionContext에 담아 반환합니다.
	 *
	 * @param gridSize                         병렬 실행할 파티션 수
	 * @return Map<String, ExcecutionContext>  파티션 이름(String)과 그에 해당하는 ExecutionContext의 매핑
	 * @throws IllegalArgumentException        gridSize가 0 이하인 경우
	 * @throws IllegalStateException           drugId의 최소/최대 값이 null인 경우
	 * @author 함예정
	 * @since 2025-05-02
	 */
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