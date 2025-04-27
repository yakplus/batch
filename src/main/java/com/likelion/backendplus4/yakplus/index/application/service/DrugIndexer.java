package com.likelion.backendplus4.yakplus.index.application.service;

import com.likelion.backendplus4.yakplus.index.application.port.in.IndexUseCase;
import com.likelion.backendplus4.yakplus.index.application.port.out.DrugIndexRepositoryPort;
import com.likelion.backendplus4.yakplus.index.application.port.out.GovDrugRawDataPort;
import com.likelion.backendplus4.yakplus.index.domain.model.Drug;
import com.likelion.backendplus4.yakplus.index.presentation.controller.dto.request.IndexRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

/**
 * 약품 색인(인덱싱) 작업을 수행하는 서비스 구현체
 *
 * @modified 2025-04-27
 * 25.04.27 - esIndexname을 인자로 받아 saveAll 메서드에 전달하도록 수정
 * - itemSeq -> drugId로 수정
 * @since 2025-04-22
 */
@Service
@RequiredArgsConstructor
public class DrugIndexer implements IndexUseCase {
    private final GovDrugRawDataPort govDrugRawDataPort;
    private final DrugIndexRepositoryPort drugIndexRepositoryPort;
    private static final String SORT_BY_PROPERTY = "drugId";

    /**
     * 요청으로 전달된 lastSeq, limit 정보를 바탕으로 RDB에서 데이터를 조회하고
     * ES 인덱스에 저장한다.
     *
     * @param request 색인 기준 및 개수 정보
     * @author 정안식
     * @modified 2025-04-27
     * 25.04.27 - esIndexname을 인자로 받아 saveAll 메서드에 전달하도록 수정
     * @since 2025-04-22
     */
    @Override
    public void index(IndexRequest request) {
        log("index 서비스 요청 수신");
        Pageable pageable = createPageable(request.limit());
        List<Drug> drugs = fetchRawData(request, pageable);
        String esIndexName = getEsIndexName();
        saveDrugs(esIndexName, drugs);
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
     * @param request  색인 기준 정보
     * @param pageable 페이징 및 정렬 정보
     * @return 도메인 모델 리스트
     * @author 정안식
     * @modified 2025-04-24
     * @since 2025-04-22
     */
    private List<Drug> fetchRawData(IndexRequest request, Pageable pageable) {
        log("RDB에서 원시 데이터 조회");
        return govDrugRawDataPort.fetchRawData(request.lastDrugId(), pageable);
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