package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.common.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;

public class StepSkipDecider {

	public static boolean shouldSkip(StepExecution stepExecution) {
		JobParameters params = stepExecution.getJobParameters();
		String skipParam = params.getString("skipStepName", "");
		String stepName = stepExecution.getStepName();

		List<String> stepsToSkip = Arrays.stream(skipParam.split(","))
			.map(String::trim).toList();

		return stepsToSkip.contains(stepName);
	}
}