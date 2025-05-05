package com.likelion.backendplus4.yakplus.symptomdictionary.application.port.out;

import java.util.Set;

public interface SymptomDictionaryJpaRepositoryPort {


    /**
     * 주어진 증상 단어 리스트를 바탕으로
     * 데이터베이스에 신규 증상 단어만 저장합니다.
     *
     * 1) 기존에 저장된 증상명을 조회하고,
     * 2) 전달받은 리스트에서 중복되지 않는 단어만 필터링한 후,
     * 3) 엔티티로 변환하여 일괄 저장합니다.
     *
     * @param symptoms 저장할 증상 단어 리스트
     * @author 박찬병
     * @modified 2025-05-01
     * @since 2025-04-30
     */
    void setDictionary(Set<String> symptoms);

}
