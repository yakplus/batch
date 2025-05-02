package com.likelion.backendplus4.yakplus.drug.presentation.controller.Scraper;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.likelion.backendplus4.yakplus.drug.infrastructure.batch.adapter.JobManager.*;
import static com.likelion.backendplus4.yakplus.response.ApiResponse.success;

import com.likelion.backendplus4.yakplus.common.util.log.LogUtil;
import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugScraperUsecase;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.adapter.JobManager;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.exception.ParserBatchError;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.exception.ParserBatchException;
import com.likelion.backendplus4.yakplus.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scraper")
public class DrugScraperController {
	private final DrugScraperUsecase drugScraperUsecase;

	@PostMapping("/start")
	public ResponseEntity<ApiResponse<String>> start(){
		return success(drugScraperUsecase.scraperStart());
	}

	@DeleteMapping("/stop")
	public ResponseEntity<ApiResponse<String>> stop(){
		return success(drugScraperUsecase.stop());
	}

	@GetMapping("/status")
	public ResponseEntity<ApiResponse<String>> getBatchProgress() {
		return success(drugScraperUsecase.getStatus());
	}
}
