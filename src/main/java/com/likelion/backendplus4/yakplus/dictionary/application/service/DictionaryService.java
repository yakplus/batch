package com.likelion.backendplus4.yakplus.dictionary.application.service;

import com.likelion.backendplus4.yakplus.dictionary.application.port.in.DictionaryUseCase;
import com.likelion.backendplus4.yakplus.dictionary.application.port.out.SymptomDictionaryElsRepositoryPort;
import com.likelion.backendplus4.yakplus.dictionary.application.port.out.SymptomDictionaryJpaRepositoryPort;
import com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.adapter.out.JsonSymptomDictionaryLoader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DictionaryService implements DictionaryUseCase {

    private final JsonSymptomDictionaryLoader jsonSymptomDictionaryLoader;
    private final SymptomDictionaryJpaRepositoryPort dictionaryRepositoryPort;
    private final SymptomDictionaryElsRepositoryPort dictionaryElsRepositoryPort;

    @Override
    public void setDictionary() {
        List<String> symptomList = jsonSymptomDictionaryLoader.loadDictionary();
        dictionaryRepositoryPort.setDictionary(symptomList);
        dictionaryElsRepositoryPort.setDictionary(symptomList);
    }
}
