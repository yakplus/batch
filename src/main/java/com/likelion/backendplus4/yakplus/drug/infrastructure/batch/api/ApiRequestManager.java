package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.common.util.log.LogUtil;

@Component
public class ApiRequestManager {
	private final RestTemplate restTemplate;
	private final UriCompBuilder uriCompBuilder;
	private final ObjectMapper objectMapper;
	private final int numOfRows;

	public ApiRequestManager(RestTemplate restTemplate,
							UriCompBuilder uriCompBuilder,
							 ObjectMapper objectMapper,
							 @Value("${gov.numOfRows}")
	                         int numOfRows) {
		this.restTemplate = restTemplate;
		this.uriCompBuilder = uriCompBuilder;
		this.objectMapper = objectMapper;
		this.numOfRows = numOfRows;
	}

	public int getDetailTotalPage(){
		return getTotalPageCountFromResponse(fetchDetailData(1), numOfRows);
	}

	public int getImageTotalPage(){
		return getTotalPageCountFromResponse(fetchImageData(1), numOfRows);
	}

	public String getImage(Long drugId){
		return restTemplate.getForObject("https://nedrug.mfds.go.kr/pbp/ezdrug/"+ drugId.toString(), JsonNode.class)
			.get("item")
			.get("extimgImageDocid")
			.asText();
	}

	public String fetchDetailData(int pageNo) {
		return restTemplate.getForObject(uriCompBuilder.getUriForDetailApi(pageNo), String.class);
	}

	public String fetchImageData(int pageNo) {
		return restTemplate.getForObject(uriCompBuilder.getUriForImgApi(pageNo), String.class);
	}

	public JsonNode getItemsFromResponse(String response) {
		LogUtil.log("응답에서 items 값 추출");
		try {
			return new ObjectMapper().readTree(response)
				.path("body")
				.path("items");
		} catch (JsonProcessingException e) {
			LogUtil.log(LogLevel.ERROR, "items 추출 실패");
			LogUtil.log(LogLevel.ERROR, "response: " + response);
			return null;
		}
	}

	private int getTotalPageCountFromResponse(String response, int pageSize) {
		try {
			int totalRows = objectMapper.readTree(response).path("body").path("totalCount").asInt();
			int pageCount = totalRows / pageSize;
			if (totalRows % numOfRows > 0) {
				pageCount += 1;
			}
			return pageCount;
		} catch (JsonProcessingException e) {
			//TODO 배치 예외처리
			throw new RuntimeException(e);
		}
	}
}
