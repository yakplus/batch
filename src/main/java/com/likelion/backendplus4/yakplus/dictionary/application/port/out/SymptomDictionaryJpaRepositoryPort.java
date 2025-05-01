package com.likelion.backendplus4.yakplus.dictionary.application.port.out;

import java.util.List;

public interface SymptomDictionaryJpaRepositoryPort {

    void setDictionary(List<String> symptoms);

}
