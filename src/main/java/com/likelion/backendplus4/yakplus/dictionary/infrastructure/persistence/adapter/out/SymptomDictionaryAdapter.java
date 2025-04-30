package com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.adapter.out;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.dictionary.application.port.out.SymptomDictionaryRepositoryPort;
import com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.repository.SymptomDictionaryRepository;
import com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.repository.entity.SymptomDictionary;
import com.likelion.backendplus4.yakplus.dictionary.infrastructure.support.mapper.DictionaryMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class SymptomDictionaryAdapter implements SymptomDictionaryRepositoryPort {

    private final SymptomDictionaryRepository repository;
    private final ObjectMapper objectMapper;
    private static final String DICT_PATH = "unique_symptoms.json"; // classpath 기준

    @Override
    @Transactional
    public void setDictionary() throws IOException {
        // Classpath 기준으로 리소스 읽기
        ClassPathResource resource = new ClassPathResource(DICT_PATH);
        InputStream inputStream = resource.getInputStream();

        // JSON 파일 → List<String> 파싱
        List<String> symptoms = objectMapper.readValue(inputStream, new TypeReference<>() {});

        // 엔티티 변환 및 저장
        List<SymptomDictionary> entities = symptoms.stream()
                .map(DictionaryMapper::toEntity)
                .toList();

        repository.saveAll(entities);
    }
}
