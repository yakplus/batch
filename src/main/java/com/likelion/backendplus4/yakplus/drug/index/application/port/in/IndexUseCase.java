package com.likelion.backendplus4.yakplus.drug.index.application.port.in;

public interface IndexUseCase {

    /**
     * 요청으로 전달된 lastSeq, limit 정보를 바탕으로 RDB에서 데이터를 조회하고
     * ES 인덱스에 저장한다.
     *
     * @author 정안식
     * @modified 2025-05-02 이해창
     * 25.05.02 - 저장된 약물 상세정보 데이터 크기를 기준으로 ES에 색인하는 loop를 만들도록 수정
     * 25.04.28 - IndexRequest를 인자로 더 이상 받지 않도록 수정
     * 25.04.27 - esIndexname을 인자로 받아 saveAll 메서드에 전달하도록 수정
     * @since 2025-04-22
     */
    void index();

    /**
     * DB에서 약품 데이터를 페이징으로 가져와 Elasticsearch에 일괄 색인합니다.
     * 각 페이지는 CHUNK_SIZE만큼 처리되며, 모든 데이터를 순차적으로 색인합니다.
     *
     * @author 박찬병
     * @modified 2025-04-25
     * @since 2025-04-24
     */
    void indexKeyword();

}