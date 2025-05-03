package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.image.processor;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.*;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.infrastructure.api.util.ApiRequestManager;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.image.reader.PageRangePartitioner;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImageTotalPageCalculator implements Tasklet {

	private final PageRangePartitioner pageRangePartitioner;
	private final ApiRequestManager apiRequestManager;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		int totalPage = apiRequestManager.getImageTotalPage();
		pageRangePartitioner.setTotalPages(totalPage);

		log("[Image-Total-Page-Calculator] 총 페이지 수 계산 완료: " + totalPage);
		return RepeatStatus.FINISHED;
	}
}