package com.likelion.backendplus4.yakplus.dictionary.application.service;

import com.likelion.backendplus4.yakplus.dictionary.application.port.in.DictionaryUseCase;
import com.likelion.backendplus4.yakplus.dictionary.application.port.out.SymptomDictionaryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DictionaryService implements DictionaryUseCase {

    private final SymptomDictionaryRepositoryPort dictionaryRepositoryPort;

    @Override
    public void setDictionary() {
        try {
            dictionaryRepositoryPort.setDictionary();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
