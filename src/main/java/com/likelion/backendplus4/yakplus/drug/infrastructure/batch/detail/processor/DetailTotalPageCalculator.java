package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.detail.processor;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.common.util.log.LogUtil;
import com.likelion.backendplus4.yakplus.drug.infrastructure.api.util.ApiRequestManager;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.detail.reader.DetailPageNumberReader;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DetailTotalPageCalculator implements Tasklet {

	private final DetailPageNumberReader detailPageNumberReader;
	private final ApiRequestManager apiRequestManager;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		int totalPage = apiRequestManager.getDetailTotalPage();
		detailPageNumberReader.setTotalPage(totalPage);

		LogUtil.log("[CombineProcessor] 총 페이지 수 계산 완료: " + totalPage);
		return RepeatStatus.FINISHED;
	}
}