package com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.api.util;

import static com.likelion.backendplus4.yakplus.common.logging.util.LogUtil.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.common.logging.util.LogLevel;
import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.api.exception.RestApiError;
import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.api.exception.RestApiException;

/**
 * 공공의약품 API 요청을 처리하는 매니저 클래스입니다.
 * 상세정보 및 이미지 데이터를 조회하고, 총 페이지 수 계산 및 응답 파싱 기능을 제공합니다.
 *
 * @since 2025-04-21
 */
@Component
public class ApiRequestManager {
	private final RestTemplate restTemplate;
	private final UriCompBuilder uriCompBuilder;
	private final ObjectMapper objectMapper;
	private final int NUM_OF_ROWS;

	/**
	 * 생성자 주입을 통해 필요한 컴포넌트를 초기화합니다.
	 *
	 * @param restTemplate REST API 호출을 위한 RestTemplate
	 * @param uriCompBuilder API URI 조립 유틸리티
	 * @param objectMapper JSON 파싱용 ObjectMapper
	 * @param NUM_OF_ROWS 페이지당 데이터 수
	 */
	public ApiRequestManager(RestTemplate restTemplate,
		UriCompBuilder uriCompBuilder,
		ObjectMapper objectMapper,
		@Value("${gov.numOfRows}")
		int NUM_OF_ROWS) {
		this.restTemplate = restTemplate;
		this.uriCompBuilder = uriCompBuilder;
		this.objectMapper = objectMapper;
		this.NUM_OF_ROWS = NUM_OF_ROWS;
	}

	/**
	 * 상세 정보 API 응답으로부터 전체 페이지 수를 계산합니다.
	 *
	 * @return 전체 페이지 수
	 *
	 * @author 이해창
	 * @since 2025-04-21
	 */
	public int getDetailTotalPage() {
		return getTotalPageCountFromResponse(fetchDetailData(1));
	}

	/**
	 * 이미지 정보 API 응답으로부터 전체 페이지 수를 계산합니다.
	 *
	 * @return 전체 페이지 수
	 *
	 * @author 이해창
	 * @since 2025-04-21
	 */
	public int getImageTotalPage() {
		return getTotalPageCountFromResponse(fetchImageData(1));
	}

	/**
	 * 의약품 ID를 통해 약품 이미지를 가져옵니다.
	 *
	 * @param drugId 의약품 고유 ID
	 * @return 이미지 데이터 jpeg(base64)
	 *
	 * @author 함예정
	 * @since 2025-05-01
	 */
	public String getImage(Long drugId) {
		return restTemplate.getForObject("https://nedrug.mfds.go.kr/pbp/ezdrug/" + drugId.toString(), JsonNode.class)
			.get("item")
			.get("extimgImageDocid")
			.asText();
	}

	/**
	 * 특정 페이지의 상세 정보를 API에서 가져옵니다.
	 *
	 * @param pageNo 페이지 번호
	 * @return 상세 정보 JSON 문자열
	 *
	 * @author 함예정
	 * @since 2025-04-21
	 */
	public String fetchDetailData(int pageNo) {
		return restTemplate.getForObject(uriCompBuilder.getUriForDetailApi(pageNo), String.class);
	}

	/**
	 * 특정 페이지의 낱알 이미지 정보를 API에서 가져옵니다.
	 *
	 * @param pageNo 페이지 번호
	 * @return 낱알 이미지 정보 JSON 문자열
	 *
	 * @author 함예정
	 * @since 2025-04-21
	 */
	public String fetchImageData(int pageNo) {
		return restTemplate.getForObject(uriCompBuilder.getUriForImgApi(pageNo), String.class);
	}

	/**
	 * 응답 문자열에서 items 노드를 추출합니다.
	 *
	 * @param response API 응답 문자열
	 * @return items 노드, 실패 시 null
	 *
	 * @author 함예정
	 * @since 2025-04-21
	 */
	public JsonNode getItemsFromResponse(String response) {
		try {
			return new ObjectMapper().readTree(response)
				.path("body")
				.path("items");
		} catch (JsonProcessingException e) {
			log(LogLevel.ERROR, "items 추출 실패");
			log(LogLevel.ERROR, "response: " + response);
			throw new RestApiException(RestApiError.ITEM_NOT_FOUND);
		}
	}

	/**
	 * 전체 데이터 수에 기반하여 총 페이지 수를 계산합니다.
	 *
	 * @param response API 응답 문자열
	 * @return 전체 페이지 수
	 *
	 * @author 이해창
	 * @since 2025-04-21
	 */
	private int getTotalPageCountFromResponse(String response) {
		try {
			int totalRows = objectMapper.readTree(response).path("body").path("totalCount").asInt();
			int pageCount = totalRows / NUM_OF_ROWS;
			if (totalRows % NUM_OF_ROWS > 0) {
				pageCount += 1;
			}
			return pageCount;
		} catch (Exception e) {
			log(LogLevel.ERROR, "전체 페이지 개수 확인 실패");
			throw new RestApiException(RestApiError.PAGE_COUNT_FAIL);
		}
	}
}
