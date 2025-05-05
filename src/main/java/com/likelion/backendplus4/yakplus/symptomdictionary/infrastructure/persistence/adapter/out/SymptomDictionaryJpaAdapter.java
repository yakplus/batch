package com.likelion.backendplus4.yakplus.symptomdictionary.infrastructure.persistence.adapter.out;

import static com.likelion.backendplus4.yakplus.common.logging.util.LogUtil.log;
import com.likelion.backendplus4.yakplus.symptomdictionary.application.port.out.SymptomDictionaryJpaRepositoryPort;
import com.likelion.backendplus4.yakplus.symptomdictionary.infrastructure.persistence.repository.SymptomDictionaryRepository;
import com.likelion.backendplus4.yakplus.symptomdictionary.infrastructure.persistence.repository.entity.SymptomDictionary;
import com.likelion.backendplus4.yakplus.symptomdictionary.infrastructure.support.mapper.DictionaryMapper;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA를 이용해 증상 사전 데이터를 관리하는 어댑터 클래스입니다.
 * SymptomDictionaryJpaRepositoryPort를 구현하여
 * 데이터베이스 접근 로직을 캡슐화합니다.
 *
 * @since 2025-05-01
 * @modified 2025-05-01
 */
@Component
@RequiredArgsConstructor
public class SymptomDictionaryJpaAdapter implements SymptomDictionaryJpaRepositoryPort {

    private final SymptomDictionaryRepository repository;

    /**
     * 주어진 증상 단어 리스트를 바탕으로
     * 데이터베이스에 신규 증상 단어만 저장합니다.
     *
     * 1) 기존에 저장된 증상명을 조회하고,
     * 2) 전달받은 리스트에서 중복되지 않는 단어만 필터링한 후,
     * 3) 엔티티로 변환하여 일괄 저장합니다.
     *
     * @param symptoms 저장할 증상 단어 리스트
     * @since 2025-04-30
     * @author 박찬병
     * @modified 2025-05-01
     */
    @Override
    @Transactional
    public void setDictionary(Set<String> symptoms) {
        log("setDictionary() 메서드 호출, 입력 증상 수: " + symptoms.size());

        // 1) 기존에 존재하는 증상 단어 조회
        Set<String> existing = repository.findAll().stream()
                .map(SymptomDictionary::getName)
                .collect(Collectors.toSet());
        log("setDictionary() 기존 저장된 증상 수: " + existing.size());

        // 2) 신규 단어만 필터링
        List<String> toInsert = symptoms.stream()
                .filter(s -> !existing.contains(s))
                .toList();
        log("setDictionary() 신규 저장 대상 증상 수: " + toInsert.size());

        // 3) 엔티티 변환 및 저장
        List<SymptomDictionary> entities = toInsert.stream()
                .map(DictionaryMapper::toSymptomEntity)
                .toList();
        repository.saveAll(entities);
        log("setDictionary() 완료, 저장된 엔티티 수: " + entities.size());
    }
}