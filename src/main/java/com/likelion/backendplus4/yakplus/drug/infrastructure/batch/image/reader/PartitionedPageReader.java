package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.image.reader;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.*;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.common.util.log.LogUtil;

@Component
@StepScope
public class PartitionedPageReader implements ItemReader<Integer> {
	private int currentPage;
	private final int endPage;

	public PartitionedPageReader(
		@Value("#{stepExecutionContext['startPage']}") int startPage,
		@Value("#{stepExecutionContext['endPage']}") int endPage) {
		log("[Reader bean 생성] startPage=" + startPage + ", endPage=" + endPage);
		this.currentPage = startPage;
		this.endPage = endPage;
	}

	@Override
	public Integer read() {
		log("이미지 페이지 Read: " + currentPage);
		if (currentPage > endPage) {
			return null;
		}
		return currentPage++;
	}
}
