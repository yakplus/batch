package com.likelion.backendplus4.yakplus.drug.application.service.scraper.detail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugDetailScraperUsecase;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.ApiRequestPort;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugDetailRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.out.dto.DrugDetailRequest;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.parser.MaterialParser;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.parser.XMLParser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/***
 * @class DrugDetailScraperService
 * @description 약품 상세 정보를 API로부터 스크래핑하여 저장하는 서비스
 * @since 2025-04-21
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DrugDetailScraperService implements DrugDetailScraperUsecase {
	private final ApiRequestPort apiRequestPort;
	private final ObjectMapper objectMapper;
	private final DrugDetailRepositoryPort drugDetailRepository;

	@Override
	public void requestSingleData() {
		log.info("API 데이터 요청");
		JsonNode items = apiRequestPort.getAllDetailData(1);
		List<DrugDetailRequest> drugs = toListFromJson(items);
		drugDetailRepository.saveDrugDetailBulk(drugs);
	}


	@Override
	public void requestAllData() {
		int totalPageCount = apiRequestPort.getDetailTotalPageCount();
		int receivedCount = 0;
		int savedCountWithoutDuplicates = 0;

		for(int i=1;i<=totalPageCount;i++){
			JsonNode items = apiRequestPort.getAllDetailData(i);
			receivedCount += drugs.size();

			// item_seq 기준 중복 제거된 약품 개수 유지 (실제 db에 저장된 데이터와 같은 지 비교용)
			int uniqueItems = deduplicateByItemSeq(drugs);
			savedCountWithoutDuplicates += uniqueItems;

			drugDetailRepository.saveDrugDetailBulk(drugs);

			log.info("Page {}, received: {}, saved (unique): {}, totalReceived: {}, totalUniqueSaved: {}",
				i, drugs.size(), uniqueItems, receivedCount, savedCountWithoutDuplicates);
		}
	}

	private List<DrugDetailRequest> toListFromJson(JsonNode items) {

		log.info("items 약품 객체로 맵핑");
		try {
			List<DrugDetailRequest> apiDataDrugDetails = toApiDetails(items);
			for (int i = 0; i < apiDataDrugDetails.size(); i++) {
				DrugDetailRequest drugDetail = apiDataDrugDetails.get(i);
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

	private List<DrugDetailRequest> toApiDetails(JsonNode items) {
		try {
			return objectMapper.readValue(items.toString(),
				new TypeReference<List<DrugDetailRequest>>() {
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

	private int deduplicateByItemSeq(List<DrugDetailRequest> drugs) {
		// itemseq 기준으로 set에 저장 --> set은 중복 허용하지 않으므로 item seq 다 넣으면 알아서 중복 없이 저장됨
		Set<Long> uniqueItems = new HashSet<>();

		for (DrugDetailRequest drug : drugs) {
			uniqueItems.add(drug.getDrugId());
		}
		return uniqueItems.size();
	}
}
