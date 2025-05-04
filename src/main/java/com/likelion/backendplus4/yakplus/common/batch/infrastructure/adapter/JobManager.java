package com.likelion.backendplus4.yakplus.common.batch.infrastructure.adapter;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.common.batch.infrastructure.exception.ParserBatchError;
import com.likelion.backendplus4.yakplus.common.batch.infrastructure.exception.ParserBatchException;

import lombok.RequiredArgsConstructor;

/**
 * Spring Batch Job 실행 및 제어를 위한 매니저 클래스입니다.
 * Job 실행, 중단, 상태 조회, 재시작 기능을 제공합니다.
 *
 * @since 2025-05-02
 */
@Component
@RequiredArgsConstructor
public class JobManager {
	private final AtomicBoolean isRunning = new AtomicBoolean(false);
	private final JobLauncher jobLauncher;
	private final JobOperator jobOperator;
	private final JobExplorer jobExplorer;

	private final Map<String, TaskExecutor> taskExecutorMap;

	private final Set<Long> stoppedExecutionIds = new ConcurrentSkipListSet<>();

	/**
	 * 지정된 Job을 비동기로 실행합니다.
	 * 만약 이미 실행 중인 Job이 있다면 예외를 발생시킵니다.
	 *
	 * @param job 실행할 Job
	 * @return 실행 요청 결과 메시지
	 * @throws ParserBatchException 중복 실행 예외
	 * @author 함예정
	 * @since 2025-05-02
	 */
	public String startJob(Job job) {
		IfAlreadyRunThrowException();
		TaskExecutor taskExecutor = taskExecutorMap.get("batchExecutor");
		log("배치 실행 시작 - Job: " + job.getName());
		isRunning.set(true);
		taskExecutor.execute(() -> {
			try {
				JobParameters params = new JobParametersBuilder()
					.addLong("requestTime", System.currentTimeMillis())
					.toJobParameters();

				jobLauncher.run(job, params);
			} catch (Exception e) {
				log(LogLevel.ERROR, "Job 실행 요청은 정상적으로 받았으나 실행에 실패하였습니다. " + job.getName());
				throw new ParserBatchException(ParserBatchError.JOB_RUN_FAIL);
			} finally {
				isRunning.set(false);
			}
		});

		return "배치 실행 요청 수락됨";
	}

	/**
	 * 중단된 JobExecution을 재시작합니다.
	 *
	 * @return 재시작 요청 결과 메시지
	 * 
	 * @author 함예정
	 * @since 2025-05-02
	 */
	public String restart() {
		IfAlreadyRunThrowException();
		TaskExecutor taskExecutor = taskExecutorMap.get("batchExecutor");
		taskExecutor.execute(() -> {
			for (Long id : new HashSet<>(stoppedExecutionIds)) {
				try {
					isRunning.set(true);
					jobOperator.restart(id);
				} catch (Exception e) {
					log(LogLevel.ERROR, "Job 재 실행 요청은 정상적으로 받았으나 실행에 실패하였습니다.");
					throw new ParserBatchException(ParserBatchError.JOB_RUN_FAIL);
				} finally {
					isRunning.set(false);
				}
			}
		});

		stoppedExecutionIds.clear();
		return "재시작 요청 성공";
	}

	/**
	 * 지정된 Job의 최근 실행 상태를 조회합니다.
	 *
	 * @param job 조회할 Job
	 * @return 상태 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	public String getJobStatus(Job job) {
		List<JobInstance> instances = jobExplorer.getJobInstances(job.getName(), 0, 30);
		if (instances.isEmpty()) {
			return "실행된 Job이 없습니다.";
		}

		for (JobInstance instance : instances) {
			List<JobExecution> executions = jobExplorer.getJobExecutions(instance);
			for (JobExecution execution : executions) {
				if (execution.isRunning()) {
					StringBuilder status = new StringBuilder("실행 중인 Step 상태: {");
					for (StepExecution step : execution.getStepExecutions()) {
						long read = step.getReadCount();
						status.append("Step: ").append(step.getStepName())
							.append(", Read: ").append(read)
							.append(", Skip: ").append(step.getSkipCount());
					}
					return status.toString().trim();
				}
			}
		}

		return "실행 중인 Job이 없습니다.";
	}

	/**
	 * 지정된 Job을 중지합니다.
	 *
	 * @param job 중지할 Job
	 * @return 중지 요청 결과 메시지
	 * 
	 * @author 함예정
	 * @since 2025-05-02
	 */
	public String stopRunningBatch(Job job) {
		String jobName = job.getName();
		log("배치 작업 중단 요청:" + jobName);
		Collection<JobInstance> instances = jobExplorer.getJobInstances(jobName, 0, 30);
		if (!instances.isEmpty()) {
			instances.stream()
				.map(jobExplorer::getJobExecutions)
				.flatMap(List::stream)
				.filter(JobExecution::isRunning)
				.forEach(exec -> {
					try {
						jobOperator.stop(exec.getId());
					} catch (Exception e) {
						log("이미 중단 중이거나 중단 불가 상태입니다. executionId=" + exec.getId());
					} finally {
						stoppedExecutionIds.add(exec.getId());
					}
				});
		}
		isRunning.set(false);
		return "중단 요청 완료";
	}

	/**
	 * 실행 중인 배치 작업이 있을 경우 예외를 발생시킵니다.
	 *
	 * @throws ParserBatchException 중복 실행 예외
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	private void IfAlreadyRunThrowException() {
		if (isRunning.get()) {
			throw new ParserBatchException(ParserBatchError.ALREADY_RUN);
		}
	}
}
