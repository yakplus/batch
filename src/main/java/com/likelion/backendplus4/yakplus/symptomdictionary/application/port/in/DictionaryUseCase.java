package com.likelion.backendplus4.yakplus.symptomdictionary.application.port.in;

public interface DictionaryUseCase {

    /**
     * 증상 사전 데이터 설정 메서드
     * <p>
     * 1) JSON 파일에서 증상 단어 리스트를 읽어들여,
     * 2) 중복되지 않는 항목을 JPA DB에 저장하고,
     * 3) Elasticsearch에도 저장합니다.
     * <p>
     * 내부 로그는 각 단계별 완료 시 출력됩니다.
     *
     * @author 박찬병
     * @modified 2025-05-03
     * @since 2025-05-01
     */
    void setDictionary();

}
