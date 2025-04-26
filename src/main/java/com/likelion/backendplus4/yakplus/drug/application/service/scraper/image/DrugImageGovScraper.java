package com.likelion.backendplus4.yakplus.drug.application.service.scraper.image;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.ApiRequestPort;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.ApiDataDrugImgRepo;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.ApiDataDrugImgEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DrugImageGovScraper {
	private final ApiRequestPort apiRequestPort;
	private final ApiDataDrugImgRepo imgRepo;
	private final ObjectMapper objectMapper;

	@Transactional
	public void getApiData(){
		log.info("의약품 개요 정보 API 호출 시작");
		JsonNode items = apiRequestPort.getAllImageData(1);
		List<ApiDataDrugImgEntity> imgDatas = null;
		try {
			imgDatas = objectMapper.readValue(items.toString(),
				new TypeReference<List<ApiDataDrugImgEntity>>() {});
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		imgRepo.saveAllAndFlush(imgDatas);
	}

	public void getAllApiData(){
		log.info("의약품 개요 정보 API 호출 시작");
		int totalPageCount = apiRequestPort.getImageTotalPageCount();
		for(int i=1;i<=totalPageCount;i++){
			JsonNode items = apiRequestPort.getAllImageData(i);
			List<ApiDataDrugImgEntity> imgDatas = null;
			try {
				imgDatas = objectMapper.readValue(items.toString(),
					new TypeReference<List<ApiDataDrugImgEntity>>() {});
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
			imgRepo.saveAllAndFlush(imgDatas);
		}
	}

}
