package com.likelion.backendplus4.yakplus.temp.support.mapper;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.drug.domain.model.vo.Material;
import com.likelion.backendplus4.yakplus.index.support.parser.SymptomTextParser;
import com.likelion.backendplus4.yakplus.temp.infrastructure.adapter.persistence.document.DrugKeywordDocument;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class KeywordMapper {

    public static DrugKeywordDocument toDocument(Drug entity) {
        // 1) 추출된 텍스트 리스트를 단일 문자열로 전처리
        String flatText = SymptomTextParser.flattenLines(entity.getEfficacy());
        // 2) 전처리된 문자열을 자동완성용 토큰 리스트로 변환
        List<String> suggestTokens = SymptomTextParser.tokenizeForSuggestion(flatText);

        // 3) 성분명 자동완성 필드
        List<String> ingredientSuggest = Optional.ofNullable(entity.getMaterialInfo())
                .orElse(Collections.emptyList())
                .stream()
                .map(Material::getName)
                .filter(Objects::nonNull)        // MaterialName이 null일 때 걸러주기
                .toList();

        return DrugKeywordDocument.builder()
                .drugId(entity.getDrugId())
                .drugName(entity.getDrugName())
                .efficacy(entity.getEfficacy())
                .efficacyList(suggestTokens)
                .imageUrl(entity.getImageUrl())
                .company(entity.getCompany())
                .drugNameSuggester(List.of(entity.getDrugName()))
                .ingredientName(ingredientSuggest)
                .ingredientNameSuggester(ingredientSuggest)
                .build();
    }


}
