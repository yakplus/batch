package com.likelion.backendplus4.yakplus.symptomdictionary.infrastructure.persistence.repository.document;

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

@Document(indexName = "symptom_dictionary")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SymptomDictionaryDocument {

    @Id
    @Field(type = FieldType.Keyword, name = "symptom")
    private String symptom;

    @CompletionField(
            analyzer = "symptom_autocomplete",
            searchAnalyzer = "symptom_search_autocomplete"
    )
    private List<String> symptomSuggester;

}
