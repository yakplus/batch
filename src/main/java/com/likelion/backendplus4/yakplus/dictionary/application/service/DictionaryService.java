package com.likelion.backendplus4.yakplus.dictionary.application.service;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;
import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.dictionary.application.port.in.DictionaryUseCase;
import com.likelion.backendplus4.yakplus.dictionary.application.port.out.SymptomDictionaryElsRepositoryPort;
import com.likelion.backendplus4.yakplus.dictionary.application.port.out.SymptomDictionaryJpaRepositoryPort;
import com.likelion.backendplus4.yakplus.dictionary.infrastructure.persistence.adapter.out.JsonSymptomDictionaryLoader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 애플리케이션 계층에서 증상 사전 관리 기능을 제공하는 서비스 클래스입니다.
 * JsonSymptomDictionaryLoader를 통해 로컬 JSON 파일에서 증상 리스트를 로드하고,
 * 이를 JPA 및 Elasticsearch에 차례로 저장합니다.
 *
 * @since 2025-05-01
 * @modified 2025-05-01
 */
@Service
@RequiredArgsConstructor
public class DictionaryService implements DictionaryUseCase {

    private final JsonSymptomDictionaryLoader jsonSymptomDictionaryLoader;
    private final SymptomDictionaryJpaRepositoryPort dictionaryRepositoryPort;
    private final SymptomDictionaryElsRepositoryPort dictionaryElsRepositoryPort;

    /**
     * 로컬 JSON 파일에서 증상 리스트를 읽어들여
     * DB(JPA) 및 Elasticsearch에 차례로 저장합니다.
     */
    @Override
    public void setDictionary() {
        log("DictionaryService.setDictionary() 호출 시작");

        // 1) JSON 파일에서 증상 리스트 로드
        List<String> symptomList = jsonSymptomDictionaryLoader.loadDictionary();
        log("  loadDictionary() 완료, 증상 수: " + symptomList.size());

        // 2) JPA 저장
        dictionaryRepositoryPort.setDictionary(symptomList);
        log("  SymptomDictionaryJpaAdapter.setDictionary() 완료");

        // 3) Elasticsearch 저장
        dictionaryElsRepositoryPort.setDictionary(symptomList);
        log("  SymptomDictionaryElsAdapter.setDictionary() 완료");

        log("DictionaryService.setDictionary() 호출 종료");
    }
}