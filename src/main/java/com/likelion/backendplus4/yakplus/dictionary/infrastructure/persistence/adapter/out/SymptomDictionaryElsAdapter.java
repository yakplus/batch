package com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.adapter.out;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.dictionary.application.port.out.SymptomDictionaryElsRepositoryPort;
import com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.repository.SymptomDictionaryDocRepository;
import com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.repository.document.SymptomDictionaryDocument;
import com.likelion.backendplus4.yakplus.dictionary.infrastructure.support.mapper.DictionaryMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Elasticsearch를 이용해 증상 사전 문서를 관리하는 어댑터 클래스입니다.
 *
 * @since 2025-04-30
 * @modified 2025-05-01
 */
@Component
@RequiredArgsConstructor
public class SymptomDictionaryElsAdapter implements SymptomDictionaryElsRepositoryPort {

    private final SymptomDictionaryDocRepository repository;

    /**
     * 주어진 증상 단어 리스트를 바탕으로 Elasticsearch에 신규 증상 사전 문서를 일괄 저장합니다.
     * 1) 입력된 리스트에서 중복을 제거하고,
     * 2) DictionaryMapper를 통해 Document 객체로 변환한 뒤,
     * 3) repository.saveAll을
     * 호출하여 일괄 색인합니다.
     *
     * @param symptoms 저장할 증상 단어 리스트
     * @since 2025-04-30
     * @author 박찬병
     * @modified 2025-05-01
     */
    @Override
    @Transactional
    public void setDictionary(List<String> symptoms) {
        log("setDictionary() 메서드 호출, 입력 증상 수: " + symptoms.size());
        // 중복 제거 및 Document 변환
        List<SymptomDictionaryDocument> docs = symptoms.stream()
                .distinct()
                .map(DictionaryMapper::toDocument)
                .toList();
        log("setDictionary() distinct 처리 후 문서 수: " + docs.size());

        // 일괄 저장
        repository.saveAll(docs);
        log("setDictionary() 완료, 색인된 문서 수: " + docs.size());

    }

}