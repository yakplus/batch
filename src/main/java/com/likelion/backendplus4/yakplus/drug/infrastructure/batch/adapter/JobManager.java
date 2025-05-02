package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.adapter;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.exception.ParserBatchError;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.exception.ParserBatchException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class JobManager {
	private final AtomicBoolean isRunning = new AtomicBoolean(false);
	private final JobLauncher jobLauncher;
	private final JobOperator jobOperator;
	private final JobExplorer jobExplorer;

	private final Map<String, TaskExecutor> taskExecutorMap;

	private Set<Long> stoppedExecutionIds = new ConcurrentSkipListSet<>();

	public void IfAlreadyRunThrowException() {
		if (isRunning.get()) {
			throw new ParserBatchException(ParserBatchError.ALREADY_RUN);
		}
	}

	public String startJob(Job job) {
		TaskExecutor taskExecutor = taskExecutorMap.get("batchExecutor");
		log("배치 시작");
		isRunning.set(true); // 실행 시작
		taskExecutor.execute(() -> {
			try {
				JobParameters params = new JobParametersBuilder()
					.addLong("requestTime", System.currentTimeMillis())
					.toJobParameters();

				jobLauncher.run(job, params);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRunning.set(false); // 완료되면 해제
			}
		});

		return "배치 실행 요청 수락됨";
	}

	public String restart() {
		IfAlreadyRunThrowException();
		TaskExecutor taskExecutor = taskExecutorMap.get("batchExecutor");
		taskExecutor.execute(() -> {
			for (Long id : new HashSet<>(stoppedExecutionIds)) {
				try {
					isRunning.set(true);
					jobOperator.restart(id);
				} catch (Exception e) {
					//TODO 나중에 예외처리
					System.out.println("e = " + e);
				} finally {
					isRunning.set(false);
				}
			}
		});

		stoppedExecutionIds.clear();
		return "재시작 요청 성공";
	}

	public String getJobStatus(Job job) {
		List<JobInstance> instances = jobExplorer.getJobInstances(job.getName(), 0, 30);

		if (instances.isEmpty()) {
			return "실행된 Job이 없습니다.";
		}

		JobInstance instance = instances.get(0);
		List<JobExecution> executions = jobExplorer.getJobExecutions(instance);

		JobExecution latest = executions.get(0);

		for (StepExecution step : latest.getStepExecutions()) {
			long read = step.getReadCount();
			long total = step.getReadCount() + step.getSkipCount();

			int progress = (total > 0) ? (int)((read * 100.0) / total) : 0;

			return "Step: " + step.getStepName() +
				", Read: " + read +
				", Skip: " + step.getSkipCount() +
				", Progress: " + progress + "%";
		}

		return "진행 중인 Step이 없습니다.";

	}

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
}
