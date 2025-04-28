package com.likelion.backendplus4.yakplus.drug.application.service.port.out;

import java.util.List;

import com.likelion.backendplus4.yakplus.drug.domain.model.DrugImage;
/**
 * 의약품 이미지 정보를 저장 및 조회하는 리포지토리 포트 인터페이스입니다.
 * 이미지 데이터의 조회 및 일괄 저장 기능을 제공합니다.
 *
 * @since 2025-04-21
 */
public interface DrugImageRepositoryPort {
	/**
	 * 저장된 모든 의약품 이미지 정보를 조회합니다.
	 *
	 * @return List 의약품 이미지 정보 리스트
	 *
	 * @since 2025-04-21
	 */
	List<DrugImage> getAllGovDrugImage();

	/**
	 * 의약품 ID로 단일 의약품 이미지 정보를 조회합니다.
	 *
	 * @param drugId 조회할 의약품 ID
	 * @return 해당 의약품의 이미지 정보
	 *
	 * @since 2025-04-21
	 */
	DrugImage getById(Long drugId);

	/**
	 * 의약품 이미지 정보를 일괄 저장하고 즉시 반영(flush)합니다.
	 *
	 * @param imgData 저장할 의약품 이미지 정보 리스트
	 *
	 * @since 2025-04-21
	 */
	void saveAllAndFlush(List<DrugImage> imgData);
}
