package com.likelion.backendplus4.yakplus.drug.index.application.port.out;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.drug.embed.infrastructure.batch.step.dto.DrugVectorDto;

public interface EmbeddingPort {
	/**
	 * 주어진 페이지 정보(Pageable)에 따라 데이터베이스에서 원시 약품 데이터와 임베딩 데이터를
	 * 조인하여 한 페이지 분량의 Drug 도메인 객체 목록을 조회합니다.
	 *
	 * @param pageable 조회할 페이지 번호와 크기, 정렬 정보 등을 포함하는 Pageable 객체
	 * @return 지정된 페이지 범위에 해당하는 Drug 도메인 객체들의 리스트
	 * @author 이해창
	 * @since 2025-05-03
	 */
	List<Drug> loadEmbeddingsByPage(Pageable pageable);

	/**
	 * 주어진 텍스트에 대해 OpenAI 임베딩 모델을 호출하여 임베딩 벡터를 생성하고 반환합니다.
	 *
	 * @param text 임베딩을 생성할 원본 텍스트
	 * @return 생성된 임베딩 벡터 (float 배열)
	 * @author 이해창
	 * @since 2025-04-25
	 * @modified
	 * 2025-04-30 - 임베딩 저장/로딩포트 통합으로 인한 위치 이동
	 */
	float[] getEmbedding(String text);

	/**
	 * 전달된 DrugVectorDto 리스트에 포함된 약품 ID와 해당 임베딩 벡터를
	 * 데이터베이스에 일괄 저장합니다.
	 *
	 * @param dtos 저장할 약품 임베딩 정보 리스트 (DrugVectorDto 객체)
	 * @author 함예정
	 * @since 2025-04-25
	 * @modified
	 * 2025-05-02 - 배치 적용으로 인한 입력 타입 변경
	 * 2025-04-30 - 임베딩 저장/로딩포트 통합으로 인한 위치 이동
	 */
	void saveEmbedding(List<DrugVectorDto> dtos);
}
