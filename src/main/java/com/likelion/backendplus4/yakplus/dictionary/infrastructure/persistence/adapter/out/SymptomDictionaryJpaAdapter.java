package com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.adapter.out;

import com.likelion.backendplus4.yakplus.dictionary.application.port.out.SymptomDictionaryJpaRepositoryPort;
import com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.repository.SymptomDictionaryRepository;
import com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.repository.entity.SymptomDictionary;
import com.likelion.backendplus4.yakplus.dictionary.infrastructure.support.mapper.DictionaryMapper;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class SymptomDictionaryJpaAdapter implements SymptomDictionaryJpaRepositoryPort {

    private final SymptomDictionaryRepository repository;

    @Override
    @Transactional
    public void setDictionary(List<String> symptoms) {
        // 기존에 존재하는 증상 단어 조회
        Set<String> existingSymptoms = repository.findAll().stream()
                .map(SymptomDictionary::getName)
                .collect(Collectors.toSet());

        // 새로 추가해야되는 단어 검사
        List<String> uniqueToInsert = symptoms.stream()
                .filter(s -> !existingSymptoms.contains(s))
                .toList();

        // 엔티티 변환 및 저장
        List<SymptomDictionary> entities = uniqueToInsert.stream()
                .map(DictionaryMapper::toEntity)
                .toList();

        repository.saveAll(entities);
    }
}
