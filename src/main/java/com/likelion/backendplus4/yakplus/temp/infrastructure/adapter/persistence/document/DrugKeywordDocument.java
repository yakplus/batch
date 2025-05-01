package com.likelion.backendplus4.yakplus.temp.infrastructure.adapter.persistence.document;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "drug_keyword")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugKeywordDocument {

    @Id
    @Field(type = FieldType.Keyword, name = "drugId")
    private Long drugId;

    @Field(type = FieldType.Text, name = "drugName")
    private String drugName;

    @Field(type = FieldType.Text, name = "company")
    private String company;

    @Field(type = FieldType.Keyword, name = "imageUrl")
    private String imageUrl;

    @Field(type = FieldType.Text, name = "efficacy")
    private List<String> efficacy;

    @Field(type = FieldType.Text, name = "efficacy_list")
    private List<String> efficacyList;

    @Field(type = FieldType.Text, name = "ingredientName")
    private List<String> ingredientName;

    @CompletionField(
            analyzer = "drugName_autocomplete",
            searchAnalyzer = "drugName_autocomplete"
    )
    private List<String> drugNameSuggester;

    @CompletionField(
            analyzer = "drugName_autocomplete",
            searchAnalyzer = "drugName_autocomplete"
    )
    private List<String> ingredientNameSuggester;

}
