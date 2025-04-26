package com.likelion.backendplus4.yakplus.drug.application.service.scraper.detail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jdbc.GovDrugJdbcRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugDetailJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.api.ApiResponseMapper;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.api.ApiUriCompBuilder;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.parser.MaterialParser;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.parser.XMLParser;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.DrugDetailEntity;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DrugApprovalDetailScraperImpl implements DrugApprovalDetailScraper {
	private final ObjectMapper objectMapper;
	private final RestTemplate restTemplate;
	private final ApiUriCompBuilder apiUriCompBuilder;
	private final GovDrugDetailJpaRepository govDrugDetailJpaRepository;
	private final GovDrugJdbcRepository govDrugJdbcRepository;

	@Override
	public void requestUpdateRawData() {
		log.info("API 데이터 요청");
		String response = restTemplate.getForObject(apiUriCompBuilder.getUriForDetailApi(1), String.class);
		log.debug("API Response: {}", response);

		JsonNode items = ApiResponseMapper.getItemsFromResponse(response);
		List<DrugDetailEntity> drugs = toListFromJson(items);
		govDrugDetailJpaRepository.saveAllAndFlush(drugs);
	}


	@Override
	public void requestUpdateAllRawData() {
		int pageNo = 1;
		int receivedCount = 0;
		int savedCountWithoutDuplicates = 0;

		String response = fetchPage(pageNo);
		int totalCount = ApiResponseMapper.getTotalCountFromResponse(response);

		while (hasMoreData(receivedCount, totalCount)) {
			JsonNode items = ApiResponseMapper.getItemsFromResponse(response);
			List<DrugDetailEntity> drugs = toListFromJson(items);
			receivedCount += drugs.size();

			// item_seq 기준 중복 제거된 약품 개수 유지 (실제 db에 저장된 데이터와 같은 지 비교용)
			int uniqueItems = deduplicateByItemSeq(drugs);
			savedCountWithoutDuplicates += uniqueItems;

			govDrugDetailJpaRepository.saveAllAndFlush(drugs);

			log.info("Page {}, received: {}, saved (unique): {}, totalReceived: {}, totalUniqueSaved: {}",
				pageNo, drugs.size(), uniqueItems, receivedCount, savedCountWithoutDuplicates);

			response = fetchPage(++pageNo);
		}

	}

	@Override
	public void requestUpdateAllRawDataByJdbc() {
		int pageNo = 1;
		int receivedCount = 0;
		int savedCountWithoutDuplicates = 0;

		String response = fetchPage(pageNo);
		int totalCount = ApiResponseMapper.getTotalCountFromResponse(response);

		while (hasMoreData(receivedCount, totalCount)) {
			JsonNode items = ApiResponseMapper.getItemsFromResponse(response);
			List<DrugDetailEntity> drugs = toListFromJson(items);
			receivedCount += drugs.size();

			// item_seq 기준 중복 제거된 약품 개수 유지 (실제 db에 저장된 데이터와 같은 지 비교용)
			int uniqueItems = deduplicateByItemSeq(drugs);
			savedCountWithoutDuplicates += uniqueItems;

			govDrugJdbcRepository.saveAll(drugs);

			log.info("Page {}, received: {}, saved (unique): {}, totalReceived: {}, totalUniqueSaved: {}",
				pageNo, drugs.size(), uniqueItems, receivedCount, savedCountWithoutDuplicates);

			response = fetchPage(++pageNo);
		}
	}

	private List<DrugDetailEntity> toListFromJson(JsonNode items) {

		log.info("items 약품 객체로 맵핑");
		try {
			List<DrugDetailEntity> apiDataDrugDetails = toApiDetails(items);
			for (int i = 0; i < apiDataDrugDetails.size(); i++) {
				DrugDetailEntity drugDetail = apiDataDrugDetails.get(i);
				JsonNode item = items.get(i);
				log.debug("item seq: " + item.get("ITEM_SEQ").asText());

				String materialRawData = item.get("MATERIAL_NAME").asText();
				String materialInfo = MaterialParser.parseMaterial(materialRawData);
				drugDetail.changeMaterialInfo(materialInfo);

				String efficacyXmlText = item.get("EE_DOC_DATA").asText();
				String efficacy = XMLParser.toJson(efficacyXmlText);
				drugDetail.changeEfficacy(efficacy);

				String usageXmlText = items.get(i).get("UD_DOC_DATA").asText();
				String usages = XMLParser.toJson(usageXmlText);
				drugDetail.changeUsage(usages);

				String precautionxmlText = items.get(i).get("NB_DOC_DATA").asText();
				String precautions = XMLParser.toJson(precautionxmlText);
				drugDetail.changePrecaution(precautions);
			}
			return apiDataDrugDetails;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List<DrugDetailEntity> toApiDetails(JsonNode items) {
		try {
			return objectMapper.readValue(items.toString(),
				new TypeReference<List<DrugDetailEntity>>() {
				});
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	// private JsonNode toJsonFromXml(String usageXmlText) throws JsonProcessingException {
	//     XmlMapper xmlMapper = new XmlMapper();
	//
	//     JsonNode jsonNode = xmlMapper.readTree(usageXmlText)
	//                                 .path("SECTION")
	//                                 .path("ARTICLE");
	//     return jsonNode;
	// }

	// TODO: 추후 삭제 예정
	// private String replaceText(String text){
	//     return text.replace("&#x119e; ", "&")
	//         .replace("&#x2022; ","")
	//         .replace("&#x301c; ", "~");
	// }

	private int deduplicateByItemSeq(List<DrugDetailEntity> drugs) {
		// itemseq 기준으로 set에 저장 --> set은 중복 허용하지 않으므로 item seq 다 넣으면 알아서 중복 없이 저장됨
		Set<Long> uniqueItems = new HashSet<>();

		for (DrugDetailEntity drug : drugs) {
			uniqueItems.add(drug.getDrugId());
		}
		return uniqueItems.size();
	}

	private String fetchPage(int pageNo) {
		return restTemplate.getForObject(apiUriCompBuilder.getUriForDetailApi(pageNo), String.class);
	}

	private boolean hasMoreData(int receivedCount, int totalCount) {
		return receivedCount < totalCount;
	}

}
