package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.detail.reader;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.common.util.log.LogUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * Spring Batch에서 각 Step 실행 시 처리할 페이지 번호를 순차적으로 제공하는 Reader 클래스
 *
 * 총 페이지 수를 기준으로 1부터 시작하여 차례대로 page 번호를 반환하며,
 * 모든 페이지가 반환되면 null을 반환하여 반복을 종료한다.
 *
 * @author 함예정
 */

@Component
@Setter
@Getter
public class DetailPageNumberReader implements ItemReader<Integer> {

	private static final Queue<Integer> pageQueue = new ConcurrentLinkedQueue<>();

	public static void setTotalPage(int totalPage) {
		pageQueue.clear();
		for (int i = 1; i <= totalPage; i++) {
			pageQueue.add(i);
		}
	}
	/**
	 * 현재 페이지 번호를 반환하고, 다음 호출을 위해 내부 카운터를 증가시킨다.
	 * 총 페이지 수를 초과하면 null을 반환하여 종료를 알린다.
	 *
	 * @return 현재 처리할 페이지 번호 또는 null(모든 페이지 처리 완료 시)
	 */
	@Override
	public Integer read() {
		Integer page = pageQueue.poll();
		if (page != null) {
			LogUtil.log(Thread.currentThread().getName() + " - Page 할당: " + page);
		}
		return page;
	}
}
