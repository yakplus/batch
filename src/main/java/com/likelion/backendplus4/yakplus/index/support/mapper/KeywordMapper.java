package com.likelion.backendplus4.yakplus.index.support.mapper;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence.repository.document.DrugKeywordDocument;

import java.util.List;

public class KeywordMapper {

    public static DrugKeywordDocument toDocument(
            Drug entity,
            List<String> efficacyTokens,
            List<String> ingredientTokens
    ) {
        return DrugKeywordDocument.builder()
                .drugId(entity.getDrugId())
                .drugName(entity.getDrugName())
                .efficacy(entity.getEfficacy())
                .efficacyList(efficacyTokens)
                .imageUrl(entity.getImageUrl())
                .company(entity.getCompany())
                .drugNameSuggester(List.of(entity.getDrugName()))
                .ingredientName(ingredientTokens)
                .ingredientNameSuggester(ingredientTokens)
                .build();
    }
}
