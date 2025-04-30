package com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.adapter.out;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.dictionary.application.port.out.SymptomDictionaryLoaderPort;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JsonSymptomDictionaryLoader implements SymptomDictionaryLoaderPort {

    private final ObjectMapper objectMapper;
    private static final String DICT_PATH = "unique_symptoms.json";

    @Override
    public List<String> loadDictionary()  {
        ClassPathResource resource = new ClassPathResource(DICT_PATH);
        try (InputStream in = resource.getInputStream()) {
            return objectMapper.readValue(
                    in,
                    new TypeReference<>() {
                    }
            );
        } catch (IOException e) {
            // TODO 커스텀 예외 처리
            throw new RuntimeException();
        }
    }
}
