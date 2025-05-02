package com.likelion.backendplus4.yakplus.drug.application.service.scraper.embed;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

import java.util.List;

import com.likelion.backendplus4.yakplus.index.application.port.out.EmbeddingLoadingPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugEmbedProcessorUseCase;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugEmbedRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.drug.infrastructure.embedding.model.EmbeddingModelType;
import com.likelion.backendplus4.yakplus.index.application.port.out.GovDrugRawDataPort;

import lombok.RequiredArgsConstructor;

/**
 * 의약품 상세 정보를 기반으로 다양한 임베딩 모델을 이용하여
 * 벡터를 생성하고 저장하는 프로세서 클래스입니다.
 *
 * {@link DrugEmbedProcessorUseCase}를 구현하며, GPT, KmBERT, KorSBERT 임베딩을 수행합니다.
 *
 * @since 2025-04-25
 */
@Service
@RequiredArgsConstructor
public class DrugEmbedProcessor implements DrugEmbedProcessorUseCase {
	private final GovDrugRawDataPort drugRawDataPort;
	private final EmbeddingLoadingPort embeddingLoadingPort;
	private final DrugEmbedRepositoryPort embedRepositoryPort;

	private volatile EmbeddingModelType currentEmbeddingModel = EmbeddingModelType.OPENAI; // 기본 모델

	@Override
	public void startEmbedding() {
		log("약품 효능 임베딩 작업 시작");
		Page<Drug> firstPage = getAllItem(0);
		for (int i = 0; i < firstPage.getTotalPages(); i++) {
			getAllItem(i).forEach(data -> {
				String efficacy = convertSingleStringForEfficacy(data.getEfficacy());
				saveVector(data, efficacy);
			});
		}
		log("약품 효능 임베딩 작업 완료");
	}

	// 임베딩 벡터를 생성하고 저장하는 공통 메서드
	private void saveVector(Drug detail, String text) {
		// 현재 선택된 임베딩 모델에 따라 벡터를 생성
		float[] vector = embeddingLoadingPort.getEmbedding(text);
		// 모델에 따른 저장 처리
		embeddingLoadingPort.saveEmbedding(detail.getDrugId(),vector);
	}

	/**
	 * 효능 정보를 단일 문자열로 변환합니다.
	 *
	 * @param stringList 효능 정보 리스트
	 * @return 공백으로 결합된 단일 문자열
	 *
	 * @since 2025-04-25
	 */
	private String convertSingleStringForEfficacy(List<String> stringList) {
		log(LogLevel.DEBUG, "약품 효능 정보 단일 문자로 변환 시작");
		StringBuilder stringBuilder = new StringBuilder();
		for (String s : stringList) {
			stringBuilder.append(s);
			stringBuilder.append(" ");
		}

		String s = stringBuilder.toString();
		log(LogLevel.DEBUG, "약품 효능 정보 단일 문자로 변환 완료" + s);
		return s;
	}

	/**
	 * 저장된 모든 의약품 상세 정보를 조회합니다.
	 *
	 * @return 의약품 상세 정보 리스트
	 *
	 * @since 2025-04-25
	 */
	private Page<Drug> getAllItem(int i) {
		return drugRawDataPort.findAllDrugs(PageRequest.of(i, 100));
	}

	/**
	 * 임베딩 모델을 스위칭하는 메서드입니다.
	 *
	 * @param modelType 전환할 임베딩 모델 타입 (GPT, KmBERT, KrSBERT)
	 */
	@Override
	public void switchEmbeddingModel(String modelType) {
		// 유효하지 않으면 기본값(OPENAI)로 처리
		this.currentEmbeddingModel = EmbeddingModelType.valueOf(modelType); // valueOf로 직접 변환
		log("임베딩 모델 스위치 완료 - 현재 모델: " + currentEmbeddingModel);
	}

	@Override
	public EmbeddingModelType getCurrentEmbeddingModel() {
		log("현재 사용 중인 임베딩 모델 조회 - 현재 모델: " + currentEmbeddingModel);
		return currentEmbeddingModel;  // Enum 반환
	}
}