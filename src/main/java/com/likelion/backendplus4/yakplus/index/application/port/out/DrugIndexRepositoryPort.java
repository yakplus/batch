package com.likelion.backendplus4.yakplus.index.application.port.out;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;

import java.util.List;

import com.likelion.backendplus4.yakplus.index.exception.IndexException;
import org.springframework.data.domain.Page;

public interface DrugIndexRepositoryPort {

    /**
     * 주어진 Drug 목록을 Elasticsearch에 Bulk API로 일괄 저장합니다.
     *
     * @param esIndexName Bulk 대상 ES 인덱스 이름
     * @param drugs       저장할 Drug 객체 리스트
     * @throws IndexException 색인 처리 중 오류 발생 시
     * @author 정안식
     * @modified 2025-04-27
     * 25.04.27 - esIndexname을 인자로 받아 Bulk API로 일괄 저장하도록 수정
     * @since 2025-04-22
     */
    void saveAll(String esIndexName, List<Drug> drugs);

    /**
     * 약품 도메인 객체 페이지를 Elasticsearch 문서로 변환하여 색인합니다.
     *
     * 1) Drug 도메인 객체 → DrugKeywordDocument로 변환
     * 2) Elasticsearch에 saveAll로 일괄 색인
     *
     * @param drugPage 색인할 약품 도메인 페이지
     * @author 박찬병
     * @since 2025-05-03
     * @modified 2025-05-03
     */
    void saveAllKeyword(Page<Drug> drugPage);
}