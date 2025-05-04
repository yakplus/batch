package com.likelion.backendplus4.yakplus.common.batch.infrastructure.detail.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.likelion.backendplus4.yakplus.common.batch.infrastructure.common.util.MaterialParser;
import com.likelion.backendplus4.yakplus.common.batch.infrastructure.common.util.XMLParser;
import com.likelion.backendplus4.yakplus.common.batch.infrastructure.detail.dto.DrugDetailRequest;
import com.likelion.backendplus4.yakplus.common.batch.infrastructure.mapper.ApiResponseMapper;
import com.likelion.backendplus4.yakplus.common.batch.infrastructure.mapper.DrugDetailRequestMapper;
import com.likelion.backendplus4.yakplus.drug.infrastructure.api.util.ApiRequestManager;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugDetailEntity;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

/**
 * 의약품 상세정보 API 응답을 처리하는 ItemProcessor 구현체입니다.
 * 입력으로 페이지 번호를 받아 해당 페이지의 데이터를 API로 조회하고,
 * XML 및 기타 데이터를 파싱하여 도메인 엔티티 리스트로 변환합니다.
 * 성분, 효능, 사용법, 주의사항 등의 세부 항목을 파싱하며,
 * 한약 여부는 주의사항 텍스트에 특정 키워드가 포함되어 있는지를 기반으로 판단합니다.
 * <p>
 * ApiRequestManager: API 요청을 관리하는 클래스
 * ApiResponseMapper: API 응답을 매핑하는 클래스
 *
 * @since 2025-05-02
 */
public class DrugDetailProcessor implements ItemProcessor<Integer, List<DrugDetailEntity>> {
    private final String materialTagName = "MATERIAL_NAME";
    private final String efficacyTagName = "EE_DOC_DATA";
    private final String usageTagName = "UD_DOC_DATA";
    private final String precautionTagName = "NB_DOC_DATA";

    private final ApiRequestManager apiRequestManager;
    private final ApiResponseMapper apiResponseMapper;

    public DrugDetailProcessor(ApiRequestManager apiRequestManager,
                               ApiResponseMapper apiResponseMapper) {
        this.apiRequestManager = apiRequestManager;
        this.apiResponseMapper = apiResponseMapper;
    }

    /**
     * 입력된 페이지 번호에 해당하는 상세 API 데이터를 조회하고,
     * 필요한 필드를 파싱 및 변환하여 엔티티 리스트로 반환합니다.
     * <p>
     * 작업 순서는 아래와 같습니다:
     * 1) API 요청을 통해 JSON 응답을 가져옵니다.
     * 2) JSON 응답에서 필요한 항목을 추출합니다.
     * 3) 각 항목에 대해 XML 데이터를 파싱합니다.
     * 4) 성분, 효능, 사용법, 주의사항을 파싱하여 DTO에 설정합니다.
     * 5) 주의사항에 한약 관련 키워드가 포함되어 있는지 확인하여 플래그를 설정합니다.
     * 6) DTO를 엔티티로 변환하여 리스트로 반환합니다.
     *
     * @param pageNumber API 조회에 사용할 페이지 번호
     * @return 변환된 DrugDetailEntity 리스트
     * @author 함예정
     * @since 2025-05-02
     */
    @Override
    public List<DrugDetailEntity> process(Integer pageNumber) {

        String response = apiRequestManager.fetchDetailData(pageNumber);
        JsonNode items = apiRequestManager.getItemsFromResponse(response);
        List<DrugDetailRequest> drugItems = apiResponseMapper.toListFromDrugDetails(items);

        for (int i = 0; i < drugItems.size(); i++) {
            DrugDetailRequest drugDetail = drugItems.get(i);
            JsonNode item = items.get(i);

            String materialRawData = item.get(materialTagName).asText();
            String materialInfo = MaterialParser.parseMaterial(materialRawData);

            drugDetail.changeMaterialInfo(materialInfo);

            String efficacyXmlText = item.get(efficacyTagName).asText();
            String efficacy = XMLParser.toJson(efficacyXmlText);
            drugDetail.changeEfficacy(efficacy);

            String usageXmlText = item.get(usageTagName).asText();
            String usages = XMLParser.toJson(usageXmlText);
            drugDetail.changeUsage(usages);

            String precautionXmlText = item.get(precautionTagName).asText();
            String precautions = XMLParser.toJson(precautionXmlText);
            drugDetail.changePrecaution(precautions);

            String precaution = drugDetail.getPrecaution();
            if (isContainHerbalText(precaution)) {
                drugDetail.changeIsHerbal(true);
            }
        }
        return drugItems.stream()
                .map(DrugDetailRequestMapper::toEntityFromRequest)
                .toList();
    }

    /**
     * 주의사항 텍스트에 한약 관련 키워드가 포함되어 있는지 확인합니다.
     *
     * @param precaution 주의사항 텍스트
     * @return boolean
     * true: 포함됨, false: 포함되지 않음
     * @author 함예정
     * @since 2025-05-02
     */
    private static boolean isContainHerbalText(String precaution) {
        return precaution != null && (precaution.contains("한의사") || precaution.contains("한약사"));
    }
}
