package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.detail.processor;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.*;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.common.util.MaterialParser;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.common.util.XMLParser;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.api.ApiRequestManager;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.mapper.ApiResponseMapper;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.dto.DrugDetailRequest;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugDetailEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper.DrugDetailRequestMapper;

/**
 * pageNumber를 받아 외부 REST API 호출 → JSON → DTO 리스트 변환 →
 * Entity 리스트로 매핑
 */

public class DrugDetailProcessor implements ItemProcessor<Integer, List<DrugDetailEntity>> {

	private final ApiRequestManager apiRequestManager;
	private final ApiResponseMapper apiResponseMapper;

	public DrugDetailProcessor(ApiRequestManager apiRequestManager,
		ApiResponseMapper apiResponseMapper) {
		this.apiRequestManager = apiRequestManager;
		this.apiResponseMapper = apiResponseMapper;
	}

	@Override
	public List<DrugDetailEntity> process(Integer pageNumber) throws Exception {
		String response = apiRequestManager.fetchDetailData(pageNumber);
		JsonNode items = apiRequestManager.getItemsFromResponse(response);
		List<DrugDetailRequest> drugItems = apiResponseMapper.toListFromDrugDetails(items);

		for (int i = 0; i < drugItems.size(); i++) {
			DrugDetailRequest drugDetail = drugItems.get(i);
			JsonNode item = items.get(i);

			String materialRawData = item.get("MATERIAL_NAME").asText();
			String materialInfo = MaterialParser.parseMaterial(materialRawData);

			drugDetail.changeMaterialInfo(materialInfo);
			log(LogLevel.DEBUG, "drugDetail 객체에 약품 성분 저장 완료: \n" + drugDetail);

			log(LogLevel.DEBUG, "약품 효능 데이터 파싱 시작");
			String efficacyXmlText = item.get("EE_DOC_DATA").asText();
			log(LogLevel.DEBUG, "약품 효능 Raw 데이터 조회 성공: \n" + efficacyXmlText);

			String efficacy = XMLParser.toJson(efficacyXmlText);
			log(LogLevel.DEBUG, "약품 효능 파싱 성공: \n" + efficacy);

			drugDetail.changeEfficacy(efficacy);
			log(LogLevel.DEBUG, "drugDetail 객체에 약품 효능 저장 완료: \n" + drugDetail);

			log(LogLevel.DEBUG, "약품 사용법 데이터 파싱 시작");
			String usageXmlText = item.get("UD_DOC_DATA").asText();
			log(LogLevel.DEBUG, "약품 사용법 Raw 데이터 조회 성공: \n" + usageXmlText);

			String usages = XMLParser.toJson(usageXmlText);
			log(LogLevel.DEBUG, "약품 사용법 파싱 성공: \n" + usages);

			drugDetail.changeUsage(usages);
			log(LogLevel.DEBUG, "drugDetail 객체에 약품 사용법 저장 완료: \n" + drugDetail);

			log(LogLevel.DEBUG, "약품 주의사항 데이터 파싱 시작");
			String precautionxmlText = item.get("NB_DOC_DATA").asText();
			log(LogLevel.DEBUG, "약품 주의사항 Raw 데이터 조회 성공: \n" + precautionxmlText);

			String precautions = XMLParser.toJson(precautionxmlText);
			log(LogLevel.DEBUG, "약품 주의사항 파싱 성공: \n" + precautions);

			drugDetail.changePrecaution(precautions);
			log(LogLevel.DEBUG, "drugDetail 객체에 약품 주의사항 저장 완료: \n" + drugDetail);

			String precaution = drugDetail.getPrecaution();
			if (precaution != null && (precaution.contains("한의사") || precaution.contains("한약사"))) {
				drugDetail.changeIsHerbal(true);
			}
		}
		return drugItems.stream()
			.map(DrugDetailRequestMapper::toEntityFromRequest)
			.toList();
	}
}
