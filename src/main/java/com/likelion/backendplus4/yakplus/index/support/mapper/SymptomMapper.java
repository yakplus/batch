package com.likelion.backendplus4.yakplus.index.support.mapper;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence.repository.document.DrugSymptomDocument;
import com.likelion.backendplus4.yakplus.index.support.parser.SymptomTextParser;
import java.util.List;

/**
 * 증상 관련 Document를 다루는 매퍼 클래스입니다.
 *
 * @author 박찬병
 * @since 2025-04-25
 * @modified 2025-04-25
 */
public class SymptomMapper {

    /**
     * 주어진 GovDrug 도메인 객체를 기반으로 ES 색인용 DrugSymptomDocument로 변환합니다. 내부에서 JSON 파싱 및 전처리 로직을 실행하며, 파싱
     * 실패 시 ScraperException을 던집니다.
     *
     * @param entity 변환 대상 GovDrug 도메인 객체
     * @return 변환된 DrugSymptomDocument 객체
     * @author 박찬병
     * @modified 2025-04-25
     * @since 2025-04-25
     */
    public static DrugSymptomDocument toDocument(Drug entity) {
        // 1) 추출된 텍스트 리스트를 단일 문자열로 전처리
        String flatText = SymptomTextParser.flattenLines(entity.getEfficacy());
        // 2) 전처리된 문자열을 자동완성용 토큰 리스트로 변환
        List<String> suggestTokens = SymptomTextParser.tokenizeForSuggestion(flatText);

        return DrugSymptomDocument.builder()
                .drugId(entity.getDrugId())
                .drugName(entity.getDrugName())
                .efficacy(suggestTokens)
                .imageUrl(entity.getImageUrl())
                .company(entity.getCompany())
                .symptomSuggester(suggestTokens)
                .build();
    }

}