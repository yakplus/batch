package com.likelion.backendplus4.yakplus.drug.application.service.port.out;

import java.util.List;

import com.likelion.backendplus4.yakplus.drug.domain.model.DrugRawData;
/**
 * 의약품 원시 데이터를 저장하는 리포지토리 포트 인터페이스입니다.
 *
 * @since 2025-04-21
 */
public interface DrugRawDataRepositoryPort {
	/**
	 * 의약품 원시 데이터를 저장합니다.
	 *
	 * @param rawData 저장할 의약품 원시 데이터
	 *
	 * @since 2025-04-21
	 */
	void save(DrugRawData rawData);


	/**
	 * 여러 의약품 원시 데이터를 일괄 저장합니다.
	 *
	 * @param rawData 저장할 의약품 원시 데이터 리스트
	 *
	 * @since 2025-04-21
	 */
	void saveAll(List<DrugRawData> rawData);
}
