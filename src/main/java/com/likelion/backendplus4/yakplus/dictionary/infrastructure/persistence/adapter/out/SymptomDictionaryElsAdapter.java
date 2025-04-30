package com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.adapter.out;

import com.likelion.backendplus4.yakplus.dictionary.application.port.out.SymptomDictionaryElsRepositoryPort;
import com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.repository.SymptomDictionaryDocRepository;
import com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.repository.document.SymptomDictionaryDocument;
import com.likelion.backendplus4.yakplus.dictionary.infrastructure.support.mapper.DictionaryMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SymptomDictionaryElsAdapter implements SymptomDictionaryElsRepositoryPort {

    private final SymptomDictionaryDocRepository repository;

    @Override
    @Transactional
    public void setDictionary(List<String> symptoms) {
        List<SymptomDictionaryDocument> docs = symptoms.stream()
                .distinct()
                .map(DictionaryMapper::toDocument)
                .toList();

        repository.saveAll(docs);
    }
}
