package com.likelion.backendplus4.yakplus.drug.infrastructure.support.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class ApiPageCounter {

	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final ApiUriCompBuilder uriBuilder;
	private final int numOfRows;

	public ApiPageCounter(ApiUriCompBuilder uriBuilder,
		@Value("${gov.numOfRows}") int numOfRows) {
		this.uriBuilder = uriBuilder;
		this.numOfRows = numOfRows;
	}

	public int getDetailApiTotalPageCount() {
		return 4;
		// URI uri = uriBuilder.getUriForDetailApiShort();
		// return getPageCountFromUri(uri);
	}

	public int getImgApiTotalPageCount() {
		return 4;
		// URI uri = uriBuilder.getUriForImgApiShort();
		// return getPageCountFromUri(uri);
	}

	private int getPageCountFromUri(URI uri) {
		try {
			String response = restTemplate.getForObject(uri, String.class);
			int totalRows = objectMapper.readTree(response).path("body").path("totalCount").asInt();
			int pageCount = totalRows / numOfRows;
			if (totalRows % numOfRows > 0) {
				pageCount += 1;
			}
			return pageCount;
		} catch (Exception e) {
			throw new RuntimeException("페이지 수 계산 중 오류 발생", e);
		}
	}
}
