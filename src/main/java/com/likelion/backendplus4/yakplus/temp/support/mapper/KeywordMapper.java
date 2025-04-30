package com.likelion.backendplus4.yakplus.temp.support.mapper;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.index.support.parser.SymptomTextParser;
import com.likelion.backendplus4.yakplus.temp.infrastructure.adapter.persistence.document.DrugKeywordDocument;
import java.util.List;

public class KeywordMapper {

    public static DrugKeywordDocument toDocument(Drug entity) {
        // 1) 추출된 텍스트 리스트를 단일 문자열로 전처리
        String flatText = SymptomTextParser.flattenLines(entity.getEfficacy());
        // 2) 전처리된 문자열을 자동완성용 토큰 리스트로 변환
        List<String> suggestTokens = SymptomTextParser.tokenizeForSuggestion(flatText);

        return DrugKeywordDocument.builder()
                .drugId(entity.getDrugId())
                .drugName(entity.getDrugName())
                .efficacy(entity.getEfficacy())
                .efficacyList(suggestTokens)
                .imageUrl(entity.getImageUrl())
                .company(entity.getCompany())
                .drugNameSuggester(List.of(entity.getDrugName()))
                .build();
    }


}
