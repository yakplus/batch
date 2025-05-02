package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.image.reader;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class PartitionedPageReader implements ItemReader<Integer> {
	private int currentPage;
	private final int endPage;

	public PartitionedPageReader(
		@Value("#{stepExecutionContext['startPage']}") int startPage,
		@Value("#{stepExecutionContext['endPage']}") int endPage) {
		this.currentPage = startPage;
		this.endPage = endPage;
	}

	@Override
	public Integer read() {
		if (currentPage > endPage) {
			return null;
		}
		return currentPage++;
	}
}
