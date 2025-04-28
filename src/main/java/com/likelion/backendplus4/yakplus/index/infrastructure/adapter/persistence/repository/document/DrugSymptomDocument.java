package com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence.repository.document;


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

@Document(indexName = "eedoc")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugSymptomDocument {

    @Id
    @Field(type = FieldType.Keyword, name = "ITEM_SEQ")
    private Long drugId;

    @Field(type = FieldType.Text, name = "ITEM_NAME")
    private String drugName;

    @Field(type = FieldType.Text, name = "company")
    private String company;

    @Field(type = FieldType.Text, name = "efficacy")
    private List<String> efficacy;

    @Field(type = FieldType.Keyword, name = "imageUrl")
    private String imageUrl;

    @CompletionField(analyzer = "symptom_autocomplete")
    private List<String> symptomSuggester;
}
