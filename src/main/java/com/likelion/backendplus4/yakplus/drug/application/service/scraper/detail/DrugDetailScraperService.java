package com.likelion.backendplus4.yakplus.drug.application.service.scraper.detail;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugDetailScraperUsecase;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.ApiRequestPort;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugDetailRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.domain.exception.ScraperException;
import com.likelion.backendplus4.yakplus.drug.domain.exception.error.ScraperErrorCode;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.dto.DrugDetailRequest;
import com.likelion.backendplus4.yakplus.drug.application.service.scraper.detail.support.MaterialParser;
import com.likelion.backendplus4.yakplus.drug.application.service.scraper.detail.support.XMLParser;

import lombok.RequiredArgsConstructor;

/**
 * 의약품 상세 정보를 외부 API로부터 수집하여 저장하는 서비스 클래스입니다.
 * {@link DrugDetailScraperUsecase}를 구현하며,
 * 단일 페이지 또는 전체 데이터를 처리할 수 있습니다.
 *
 * @since 2025-04-21
 */

@Component
@RequiredArgsConstructor
public class DrugDetailScraperService implements DrugDetailScraperUsecase {
	private final ApiRequestPort apiRequestPort;
	private final ObjectMapper objectMapper;
	private final DrugDetailRepositoryPort drugDetailRepository;

	@Override
	public void requestSingleData(int pageNumber) {
		JsonNode items = apiRequestPort.getAllDetailData(pageNumber);

		log("API 응답 처리 시작 - Drug Detail");
		List<DrugDetailRequest> drugs = toListFromJson(items);
		log("API 응답 처리 완료 - Drug Detail");
		log(LogLevel.DEBUG, "완료 데이터 : \n" + drugs);

		drugDetailRepository.saveDrugDetailBulk(drugs);
	}

	@Override
	public void requestAllData() {
		int totalPageCount = apiRequestPort.getDetailTotalPageCount();
		int receivedCount = 0;
		int savedCountWithoutDuplicates = 0;

		log("전체 API 데이터 수집 시작 - Drug Detail");

		for (int i = 1; i <= totalPageCount; i++) {
			JsonNode items = apiRequestPort.getAllDetailData(i);
			List<DrugDetailRequest> drugs = toListFromJson(items);
			receivedCount += drugs.size();

			// item_seq 기준 중복 제거된 약품 개수 유지 (실제 db에 저장된 데이터와 같은 지 비교용)
			int uniqueItems = deduplicateByItemSeq(drugs);
			savedCountWithoutDuplicates += uniqueItems;

			drugDetailRepository.saveDrugDetailBulk(drugs);

			log(LogLevel.DEBUG,
				"Page: " + i
					+ "received: " + drugs.size()
					+ "saved (unique): " + uniqueItems
					+ "totalReceived: " + receivedCount
					+ "totalUniqueSaved: " + savedCountWithoutDuplicates
			);
		}
	}

	/**
	 * 외부 API로부터 받은 JSON 데이터를 {@link DrugDetailRequest} 리스트로 변환하고,
	 * 각 항목의 상세 정보를 가공하여 반환합니다.
	 *
	 * @param items API에서 수신한 JSON 노드
	 * @return {@link DrugDetailRequest} 리스트
	 *
	 * @author 함예정, 이해창
	 * @since 2025-04-21
	 */
	private List<DrugDetailRequest> toListFromJson(JsonNode items) {

		log("API 응답 > DrugDetailRequest 객체 변환 시작");
		try {
			List<DrugDetailRequest> apiDataDrugDetails = changeTypeToList(items);
			for (int i = 0; i < apiDataDrugDetails.size(); i++) {
				DrugDetailRequest drugDetail = apiDataDrugDetails.get(i);
				JsonNode item = items.get(i);
				log(LogLevel.DEBUG, "item seq: " + item.get("ITEM_SEQ").asText());

				log(LogLevel.DEBUG, "약품 성분 파싱 시작");
				String materialRawData = item.get("MATERIAL_NAME").asText();
				log(LogLevel.DEBUG, "약품 성분 Raw 데이터 조회 성공: \n" + materialRawData);

				String materialInfo = MaterialParser.parseMaterial(materialRawData);
				log(LogLevel.DEBUG, "약품 성분 파싱 성공: \n" + materialInfo);

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
				String usageXmlText = items.get(i).get("UD_DOC_DATA").asText();
				log(LogLevel.DEBUG, "약품 사용법 Raw 데이터 조회 성공: \n" + usageXmlText);

				String usages = XMLParser.toJson(usageXmlText);
				log(LogLevel.DEBUG, "약품 사용법 파싱 성공: \n" + usages);

				drugDetail.changeUsage(usages);
				log(LogLevel.DEBUG, "drugDetail 객체에 약품 사용법 저장 완료: \n" + drugDetail);

				log(LogLevel.DEBUG, "약품 주의사항 데이터 파싱 시작");
				String precautionxmlText = items.get(i).get("NB_DOC_DATA").asText();
				log(LogLevel.DEBUG, "약품 주의사항 Raw 데이터 조회 성공: \n" + precautionxmlText);

				String precautions = XMLParser.toJson(precautionxmlText);
				log(LogLevel.DEBUG, "약품 주의사항 파싱 성공: \n" + precautions);

				drugDetail.changePrecaution(precautions);
				log(LogLevel.DEBUG, "drugDetail 객체에 약품 주의사항 저장 완료: \n" + drugDetail);
			}
			return apiDataDrugDetails;
		} catch (Exception e) {
			throw new ScraperException(ScraperErrorCode.API_DRUG_DETAIL_PARSING_FAIL);
		}
	}

	/**
	 * JSON 노드를 {@link DrugDetailRequest} 리스트로 변환합니다.
	 *
	 * @param items 변환할 JSON 노드
	 * @return {@link DrugDetailRequest} 리스트
	 *
	 * @author 함예정, 이해창
	 * @since 2025-04-21
	 */
	private List<DrugDetailRequest> changeTypeToList(JsonNode items) {
		try {
			return objectMapper.readValue(items.toString(),
				new TypeReference<List<DrugDetailRequest>>() {
				});
		} catch (JsonProcessingException e) {
			throw new ScraperException(ScraperErrorCode.RESPONSE_TYPE_CHANGE_FAIL);
		}
	}
	// TODO: 추후 삭제 예정
	// private String replaceText(String text){
	//     return text.replace("&#x119e; ", "&")
	//         .replace("&#x2022; ","")
	//         .replace("&#x301c; ", "~");
	// }

	/**
	 * 의약품 상세 정보 리스트에서 중복되지 않는 항목 수를 계산합니다.
	 * 중복 기준은 item_seq (drugId)입니다.
	 *
	 * @param drugs 중복 제거 대상 {@link DrugDetailRequest} 리스트
	 * @return 중복 제거 후 고유 항목 수
	 *
	 * @author 이해창
	 * @since 2025-04-21
	 */
	private int deduplicateByItemSeq(List<DrugDetailRequest> drugs) {
		Set<Long> uniqueItems = new HashSet<>();

		for (DrugDetailRequest drug : drugs) {
			uniqueItems.add(drug.getDrugId());
		}
		return uniqueItems.size();
	}
}
