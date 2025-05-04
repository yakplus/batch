package com.likelion.backendplus4.yakplus.dictionary.application.service;

import com.likelion.backendplus4.yakplus.dictionary.application.port.in.DictionaryUseCase;
import com.likelion.backendplus4.yakplus.dictionary.application.port.out.SymptomDictionaryElsRepositoryPort;
import com.likelion.backendplus4.yakplus.dictionary.application.port.out.SymptomDictionaryJpaRepositoryPort;
import com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.adapter.out.JsonSymptomDictionaryLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

/**
 * 증상 사전 데이터를 관리하는 서비스 클래스입니다.
 *
 *
 * @author 박찬병
 * @since 2025-05-01
 * @modified 2025-05-03
 */
@Service
@RequiredArgsConstructor
public class DictionaryService implements DictionaryUseCase {

    private static final String INDENT = "  ";

    private final JsonSymptomDictionaryLoader jsonSymptomDictionaryLoader;
    private final SymptomDictionaryJpaRepositoryPort dictionaryRepositoryPort;
    private final SymptomDictionaryElsRepositoryPort dictionaryElsRepositoryPort;

    /**
     * 증상 사전 데이터 설정 메서드
     * 1) JSON 파일에서 증상 단어 리스트를 읽어들여,
     * 2) 중복되지 않는 항목을 JPA DB에 저장하고,
     * 3) Elasticsearch에도 저장합니다.
     *
     * 내부 로그는 각 단계별 완료 시 출력됩니다.
     *
     * @author 박찬병
     * @modified 2025-05-03
     * @since 2025-05-01
     */
    @Override
    public void setDictionary() {
        log("DictionaryService.setDictionary() 호출 시작");

        Set<String> symptomList = jsonSymptomDictionaryLoader.loadDictionary();
        log(INDENT+"loadDictionary() 완료, 증상 수: " + symptomList.size());

        dictionaryRepositoryPort.setDictionary(symptomList);
        log(INDENT+"SymptomDictionaryJpaAdapter.setDictionary() 완료");

        dictionaryElsRepositoryPort.setDictionary(symptomList);
        log(INDENT+"SymptomDictionaryElsAdapter.setDictionary() 완료");

        log("DictionaryService.setDictionary() 호출 종료");
    }
}