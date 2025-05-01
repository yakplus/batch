package com.likelion.backendplus4.yakplus.temp.application.service;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.temp.application.port.in.IndexTempUseCase;
import com.likelion.backendplus4.yakplus.temp.application.port.out.TempDrugIndexRepositoryPort;
import com.likelion.backendplus4.yakplus.temp.application.port.out.TempRawDataPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrugTempIndexer implements IndexTempUseCase {

    private final TempRawDataPort govDrugRawDataPort;
    private final TempDrugIndexRepositoryPort drugIndexRepositoryPort;

    private static final int CHUNK_SIZE = 1_000;

    @Override
    public void indexKeyword() {
        log("indexKeyword 요청 수신");
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
}
