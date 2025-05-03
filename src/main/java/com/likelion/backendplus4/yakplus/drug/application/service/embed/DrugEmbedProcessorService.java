package com.likelion.backendplus4.yakplus.drug.application.service.embed;

import org.springframework.stereotype.Service;

import com.likelion.backendplus4.yakplus.drug.application.service.port.in.embed.DrugEmbedProcessorUseCase;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.BatchJobPort;
import com.likelion.backendplus4.yakplus.switcher.application.port.out.EmbeddingSwitchPort;

import lombok.RequiredArgsConstructor;

/**
 * DrugEmbedProcessorService는 약품 효능에 대한 텍스트를 임베딩하는 작업을 처리하는 서비스입니다.
 * 이 서비스는 임베딩 작업의 시작, 중지, 상태 확인 및 임베딩 모델 스위칭 기능을 제공합니다.
 * batchJobPort: 배치 작업 실행을 위한 포트
 * switchPort: 임베딩 모델 스위치를 위한 포트
 *
 * @since 2025-04-25
 * @modify 2025-05-02 함예정
 *   - 스프링 배치로 전환, 배치 포트로 외부서비스 요청하도록 변경
 */
@Service
@RequiredArgsConstructor
public class DrugEmbedProcessorService implements DrugEmbedProcessorUseCase {
	private final BatchJobPort batchJobPort;
	private final EmbeddingSwitchPort switchPort;

	@Override
	public String startEmbedding() {
		return batchJobPort.embedJobStart();
	}

	@Override
	public void switchEmbeddingModel(String modelType) {
		switchPort.switchTo(modelType);
	}

	@Override
	public String getCurrentEmbeddingModel() {
		return switchPort.getAdapterBeanName();
	}

	@Override
	public String statusEmbedding() {
		return batchJobPort.embedjobStatus();
	}

	@Override
	public String stopEmbedding() {
		return batchJobPort.embedJobStop();
	}
}
