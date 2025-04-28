package com.likelion.backendplus4.yakplus.drug.application.service.port.out;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 외부 API로부터 의약품 상세 정보 및 이미지 데이터를 요청하는 포트 인터페이스입니다.
 * 페이징 처리된 데이터를 수집하고, 전체 페이지 수를 조회하는 기능을 제공합니다.
 */
public interface ApiRequestPort {

	/**
	 * 주어진 페이지 번호에 해당하는 의약품 상세 정보를 외부 API로부터 조회합니다.
	 *
	 * @param pageNo 조회할 페이지 번호
	 * @return 해당 페이지의 의약품 상세 정보를 포함하는 JsonNode
	 *
	 * @since 2025-04-21
	 */
	JsonNode getAllDetailData(int pageNo);

	/**
	 * 주어진 페이지 번호에 해당하는 의약품 이미지 정보를 외부 API로부터 조회합니다.
	 *
	 * @param pageNo 조회할 페이지 번호
	 * @return 해당 페이지의 의약품 이미지 정보를 포함하는 JsonNode
	 *
	 * @since 2025-04-21
	 */
	JsonNode getAllImageData(int pageNo);

	/**
	 * 의약품 상세 정보의 전체 페이지 수를 외부 API로부터 조회합니다.
	 *
	 * @return 상세 정보의 총 페이지 수
	 *
	 * @since 2025-04-21
	 */
	int getDetailTotalPageCount();

	/**
	 * 의약품 이미지 정보의 전체 페이지 수를 외부 API로부터 조회합니다.
	 *
	 * @return 이미지 정보의 총 페이지 수
	 *
	 * @since 2025-04-21
	 */
	int getImageTotalPageCount();
}
