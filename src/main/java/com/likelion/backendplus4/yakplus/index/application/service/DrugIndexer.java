package com.likelion.backendplus4.yakplus.index.application.service;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.index.application.port.in.IndexUseCase;
import com.likelion.backendplus4.yakplus.index.application.port.out.DrugIndexRepositoryPort;
import com.likelion.backendplus4.yakplus.index.application.port.out.GovDrugRawDataPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

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
    private final GovDrugRawDataPort govDrugRawDataPort;
    private final DrugIndexRepositoryPort drugIndexRepositoryPort;
    private static final String SORT_BY_PROPERTY = "drugId";
    private static final int CHUNK_SIZE = 1_000;

    /**
     * 요청으로 전달된 lastSeq, limit 정보를 바탕으로 RDB에서 데이터를 조회하고
     * ES 인덱스에 저장한다.
     *
     * @author 정안식
     * @modified 2025-04-28
     * 25.04.27 - esIndexname을 인자로 받아 saveAll 메서드에 전달하도록 수정
     * 25.04.28 - IndexRequest를 인자로 더 이상 받지 않도록 수정
     * @since 2025-04-22
     */
    @Override
    public void index() {
        log("index 서비스 요청 수신");
//        Pageable pageable = createPageable(request.limit());
        for (int i = 1; i <= 50; i++) {
            List<Drug> drugs = fetchRawData(i);
            String esIndexName = getEsIndexName();
            saveDrugs(esIndexName, drugs);
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
    public void indexSymptom() {
        log("indexSymptom 요청 수신");
        int page = 0;
        Page<Drug> drugPage;

        do {
            log("색인 시작: page=" + page);

            // 1. 페이징으로 DB에서 한 청크 가져오기
            drugPage = govDrugRawDataPort.findAllDrugs(PageRequest.of(page, CHUNK_SIZE));
            log("  조회 완료: page=" + page + ", 건수=" + drugPage.getNumberOfElements());

            // 2. 청크별 ES에 색인
            drugIndexRepositoryPort.saveAllSymptom(drugPage);
            log("  색인 완료: page=" + page + ", 건수=" + drugPage.getNumberOfElements());

            // 3. 다음 1000개 값 루프
            page++;
        } while (drugPage.hasNext());
        log("indexSymptom 전체 처리 완료");
    }

    /**
     * limit 크기 및 DrugId 오름차순 정렬 기준의 객체를 생성한다.
     *
     * @param limit 조회할 최대 건수
     * @return 페이징 객체
     * @author 정안식
     * @modified 2025-04-27
     * 25.04.27 - itemSeq -> drugId로 수정
     * @since 2025-04-22
     */
    private Pageable createPageable(int limit) {
        log("pageable 생성");
        return PageRequest.of(0, limit, Sort.by(SORT_BY_PROPERTY).ascending());
    }

    /**
     * RDB에서 lastSeq 이후의 원시 데이터를 조회하여 도메인 객체로 변환한다.
     *
     * @param i for문 내부에서 동작하는 i값(pageable의 pageNumber)
     * @return 도메인 모델 리스트
     * @author 정안식
     * @modified 2025-04-28
     * 25.04.28 - 페이징 처리 로직 수정
     * @since 2025-04-22
     */
    private List<Drug> fetchRawData(int i) {
        log("RDB에서 원시 데이터 조회");
        return govDrugRawDataPort.fetchRawData(i);
    }

    /**
     * Elasticsearch 인덱스 이름을 조회한다.
     *
     * @return Elasticsearch 인덱스 이름
     * @author 정안식
     * @since 2025-04-27
     */
    private String getEsIndexName() {
        log("ES 인덱스 이름 조회");
        return govDrugRawDataPort.getEsIndexName();
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