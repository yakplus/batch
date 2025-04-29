package com.likelion.backendplus4.yakplus.drug.application.service.scraper.embed;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugEmbedProcessorUseCase;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugDetailRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugEmbedRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.drug.domain.model.DrugDetail;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.EmbeddingPort;
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
	private final EmbeddingPort embeddingPort;
	private final DrugEmbedRepositoryPort embedRepositoryPort;

	@Override
	public void startEmbedding() {
		log("약품 효능 임베딩 작업 시작");
		Page<Drug> firstPage = getAllItem(0);
		for(int i=0; i<firstPage.getTotalPages(); i++){
			getAllItem(i).forEach(data -> {
				String efficacy = convertSingleStringForEfficacy(data.getEfficacy());
				saveSbertVector(data, efficacy);
				saveKmBertVector(data, efficacy);
				saveGptVector(data, efficacy);
			});

		}

		log("약품 효능 임베딩 작업 완료");
	}

	/**
	 * GPT 모델을 사용하여 임베딩 벡터를 생성하고 저장합니다.
	 *
	 * @param detail 의약품 상세 정보
	 * @param text 임베딩 대상 텍스트
	 *
	 * @since 2025-04-25
	 */
	private void saveGptVector(Drug detail, String text) {
		float[] openAIVector = embeddingPort.getEmbedding(
			text, EmbeddingModelType.OPENAI);
		embedRepositoryPort.saveGptEmbed(detail.getDrugId(), openAIVector);
	}

	/**
	 * KmBERT 모델을 사용하여 임베딩 벡터를 생성하고 저장합니다.
	 *
	 * @param detail 의약품 상세 정보
	 * @param text 임베딩 대상 텍스트
	 *
	 * @since 2025-04-25
	 */
	private void saveKmBertVector(Drug detail, String text) {
		float[] kmbertVector = embeddingPort.getEmbedding(
			text, EmbeddingModelType.KM_BERT);
		embedRepositoryPort.saveKmBertEmbed(detail.getDrugId(), kmbertVector);
	}

	/**
	 * KrSBERT 모델을 사용하여 임베딩 벡터를 생성하고 저장합니다.
	 *
	 * @param detail 의약품 상세 정보
	 * @param text 임베딩 대상 텍스트
	 *
	 * @since 2025-04-25
	 */
	private void saveSbertVector(Drug detail, String text) {
		float[] sbertVector = embeddingPort.getEmbedding(
			text, EmbeddingModelType.SBERT);
		embedRepositoryPort.saveKrSbertEmbed(detail.getDrugId(), sbertVector);
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
}