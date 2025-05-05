package com.likelion.backendplus4.yakplus.drug.index.application.service;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.drug.index.application.port.in.IndexUseCase;
import com.likelion.backendplus4.yakplus.drug.index.application.port.out.DrugIndexRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.index.application.port.out.DrugRawDataPort;
import com.likelion.backendplus4.yakplus.drug.embed.application.port.out.EmbeddingSwitchPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.likelion.backendplus4.yakplus.common.logging.util.LogUtil.log;

/**
 * 약품 색인(인덱싱) 작업을 수행하는 서비스 구현체
 *
 * @modified 2025-04-28
 * 25.04.27 - esIndexname을 인자로 받아 saveAll 메서드에 전달하도록 수정
 * - itemSeq -> drugId로 수정
 * - 페이징 처리 로직 수정
 * @since 2025-04-22
 */
@Service
@RequiredArgsConstructor
public class DrugIndexer implements IndexUseCase {
    private final DrugRawDataPort drugRawDataPort;
    private final DrugIndexRepositoryPort drugIndexRepositoryPort;
    private final EmbeddingSwitchPort embeddingSwitchPort;

    private static final String INDENT = "  ";
    private static final int CHUNK_SIZE = 100;

    /**
     * 요청으로 전달된 lastSeq, limit 정보를 바탕으로 RDB에서 데이터를 조회하고
     * ES 인덱스에 저장한다.
     *
     * @author 정안식
     * @modified
     * 25.05.02 - 이해창: 저장된 약물 상세정보 데이터 크기를 기준으로 ES에 색인하는 loop를 만들도록 수정
     * 25.04.28 - IndexRequest를 인자로 더 이상 받지 않도록 수정
     * 25.04.27 - esIndexname을 인자로 받아 saveAll 메서드에 전달하도록 수정
     * @since 2025-04-22
     */
    @Override
    public void index() {
        log("index 서비스 요청 수신");

        String esIndexName = getEsIndexName();
        long totalDataSize = drugRawDataPort.getDrugTotalSize();
        int totalPages = getTotalPages(totalDataSize);

        for(int currentPage = 0; currentPage < totalPages; currentPage++) {
            log("색인 시작: page=" + currentPage);

            List<Drug> drugs = fetchRawData(currentPage, CHUNK_SIZE);
            log(INDENT+"조회 완료: page=" + currentPage + ", 건수=" + drugs.size());

            saveDrugs(esIndexName, drugs);
            log("색인 완료: page=" + currentPage + ", 건수=" + drugs.size());
        }
    }

    /**
     * DB에서 약품 데이터를 페이징으로 가져와 Elasticsearch에 일괄 색인합니다.
     * 각 페이지는 CHUNK_SIZE만큼 처리되며, 모든 데이터를 순차적으로 색인합니다.
     *
     * @author 박찬병
     * @modified 2025-04-25
     * @since 2025-04-24
     */
    @Override
    public void indexKeyword() {
        log("indexKeyword 요청 수신");
        int page = 0;
        Page<Drug> drugPage;

        do {
            log("색인 시작: page=" + page);

            // 1. 페이징으로 DB에서 한 청크 가져오기
            drugPage = drugRawDataPort.findAllDrugs(PageRequest.of(page, CHUNK_SIZE));
            log(INDENT+"조회 완료: page=" + page + ", 건수=" + drugPage.getNumberOfElements());

            // 2. 청크별 ES에 색인
            drugIndexRepositoryPort.saveAllKeyword(drugPage);
            log(INDENT+"색인 완료: page=" + page + ", 건수=" + drugPage.getNumberOfElements());

            // 3. 다음 1000개 값 루프
            page++;
        } while (drugPage.hasNext());
        log("indexSymptom 전체 처리 완료");
    }

    /**
     * Elasticsearch 인덱스 이름을 조회한다.
     *
     * @return Elasticsearch 인덱스 이름
     * @author 정안식
     * @since 2025-04-27
     * @modified
     * 2025-05-02 - 이해창: 하드코딩 된 문자를 받어오던 것을 임베딩 모델 BeanName을 가져오도록 수정
     */
    private String getEsIndexName() {
        log("ES 인덱스 이름 조회");
        return embeddingSwitchPort.getAdapterBeanName();
    }

    /**
     * 전체 데이터 개수를 기준으로 CHUNK_SIZE 단위로 나누어 필요한 페이지 수를 계산합니다.
     *
     * <p>예를 들어, 전체 데이터가 25개이고 CHUNK_SIZE가 10이라면
     * (25 + 10 - 1) / 10 = 3페이지가 계산됩니다.</p>
     *
     * @param totalDataSize 전체 데이터 개수
     * @return 필요한 전체 페이지 수 (마지막 페이지에 남는 데이터도 한 페이지로 처리)
     * @author 이해창
     * @since 2025-05-03
     */
    private static int getTotalPages(long totalDataSize) {
        int totalPages = (int) ((totalDataSize + CHUNK_SIZE - 1) / CHUNK_SIZE);
        return totalPages;
    }

    /**
     * RDB에서 lastSeq 이후의 원시 데이터를 조회하여 도메인 객체로 변환한다.
     *
     * @param  pageNum 현재 페이지 번호
     * @param numOfRows 한 페이지당 조회할 건수
     * @return 도메인 모델 리스트
     * @author 정안식
     * @modified
     * 25.05.02 - 이해창: 페이징 처리 시 페이지 사이즈 받도록 수정 <br/>
     * 25.04.28 - 페이징 처리 로직 수정
     * @since 2025-04-22
     */
    private List<Drug> fetchRawData(int pageNum, int numOfRows) {
        log("RDB에서 원시 데이터 조회");
        return drugRawDataPort.fetchRawData(pageNum, numOfRows);
    }

    /**
     * 조회된 도메인 객체들을 ES 인덱스에 저장 처리한다.
     *
     * @param drugs 저장할 도메인 모델 리스트
     * @author 정안식
     * @modified 2025-04-27
     * 25.04.27 - esIndexName을 인자로 받도록 수정
     * @since 2025-04-22
     */
    private void saveDrugs(String esIndexName, List<Drug> drugs) {
        log("ES 인덱스에 저장");
        drugIndexRepositoryPort.saveAll(esIndexName, drugs);
    }
}