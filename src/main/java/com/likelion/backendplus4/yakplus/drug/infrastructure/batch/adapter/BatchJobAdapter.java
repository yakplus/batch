package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.adapter;

import org.springframework.batch.core.Job;
import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.application.service.port.out.BatchJobPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BatchJobAdapter implements BatchJobPort {

	private final JobManager jobManager;
	private final Job drugScrapJob;
	private final Job drugDetailScrapJob;
	private final Job drugImageScrapJob;
	private final Job drugTableCombineJob;

	@Override
	public String allJobStart() {
		jobManager.IfAlreadyRunThrowException();
		return jobManager.startJob(drugScrapJob);
	}

	@Override
	public String allJobStop() {
		return jobManager.stopRunningBatch(drugScrapJob);
	}

	@Override
	public String allJobResume() {
		return jobManager.restart();
	}

	@Override
	public String allJobStatus() {
		return jobManager.getJobStatus(drugScrapJob);
	}

	@Override
	public String detailScrapJobStart() {
		jobManager.IfAlreadyRunThrowException();
		return jobManager.startJob(drugDetailScrapJob);
	}

	@Override
	public String detailScrapJobStop() {
		return jobManager.stopRunningBatch(drugDetailScrapJob);
	}

	@Override
	public String detailScrapJobStatus() {
		return jobManager.getJobStatus(drugDetailScrapJob);
	}

	@Override
	public String imageScrapJobStart() {
		jobManager.IfAlreadyRunThrowException();
		return jobManager.startJob(drugImageScrapJob);
	}

	@Override
	public String imageScrapJobStop() {
		return jobManager.stopRunningBatch(drugImageScrapJob);
	}

	@Override
	public String imageScrapJobStatus() {
		return jobManager.getJobStatus(drugImageScrapJob);
	}

	@Override
	public String tableCombineJobStart() {
		jobManager.IfAlreadyRunThrowException();
		return jobManager.startJob(drugTableCombineJob);
	}

	@Override
	public String tableCombineJobStop() {
		return jobManager.stopRunningBatch(drugTableCombineJob);
	}

	@Override
	public String tableCombineJobStatus() {
		return jobManager.getJobStatus(drugTableCombineJob);
	}
}
