package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.out;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.ApiRequestPort;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.api.ApiPageCounter;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper.ApiResponseMapper;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.api.ApiUriCompBuilder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApiRequestAdapter implements ApiRequestPort {
	private final RestTemplate restTemplate;
	private final ApiUriCompBuilder apiUriCompBuilder;
	private final ApiPageCounter apiPageCounter;

	@Override
	public JsonNode getAllDetailData(int pageNo) {
		try {
			String response = fetchDetailPage(pageNo);
			return ApiResponseMapper.getItemsFromResponse(response);
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public JsonNode getAllImageData(int pageNo) {
		try {
			String response = fetchImagePage(pageNo);
			return ApiResponseMapper.getItemsFromResponse(response);
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getDetailTotalPageCount() {
		return apiPageCounter.getDetailApiTotalPageCount();
	}

	@Override
	public int getImageTotalPageCount() {
		return apiPageCounter.getImgApiTotalPageCount();
	}

	private String fetchDetailPage(int pageNo) {
		return restTemplate.getForObject(apiUriCompBuilder.getUriForDetailApi(pageNo), String.class);
	}

	private String fetchImagePage(int pageNo) {
		return restTemplate.getForObject(apiUriCompBuilder.getUriForImgApi(pageNo), String.class);
	}
}
