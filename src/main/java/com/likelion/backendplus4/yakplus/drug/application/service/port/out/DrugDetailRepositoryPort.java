package com.likelion.backendplus4.yakplus.drug.application.service.port.out;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.likelion.backendplus4.yakplus.drug.domain.model.DrugDetail;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.dto.DrugDetailRequest;

/**
 * 의약품 상세 정보를 저장 및 조회하는 리포지토리 포트 인터페이스입니다.
 * 개별 및 대량 저장 기능과 저장된 전체 데이터를 조회하는 기능을 제공합니다.
 *
 * @since 2025-04-21
 */
public interface DrugDetailRepositoryPort {
	/**
	 * 단일 의약품 상세 정보를 저장합니다.
	 *
	 * @param e 저장할 의약품 상세 정보 저장 요청 객체
	 *
	 * @since 2025-04-21
	 */
	void saveDrugDetail(DrugDetailRequest e);

	/**
	 * 의약품 상세 정보를 일괄 저장합니다.
	 *
	 * @param list 저장할 의약품 상세 정보 저장 요청 객체 리스트
	 *
	 * @since 2025-04-21
	 */
	void saveDrugDetailBulk(List<DrugDetailRequest> list);

	/**
	 * 저장된 모든 의약품 상세 정보를 조회합니다.
	 *
	 * @return 의약품 상세 정보 리스트
	 *
	 * @since 2025-04-21
	 */
	List<DrugDetail> getAllGovDrugDetail();

	Page<DrugDetail> getGovDrugDetailByPage(Pageable pageable);
}
