package com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.batch.step.detail.processor;

import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.batch.step.detail.reader.DetailPageNumberReader;
import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.api.util.ApiRequestManager;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import static com.likelion.backendplus4.yakplus.common.logging.util.LogUtil.log;

/**
 * 의약품 상세정보 수집을 위한 총 페이지 수를 계산하는 Tasklet입니다.
 * 
 * ApiRequestManager: API 요청을 관리하는 클래스
 * 
 * @since 2025-05-02
 */
@Component
@RequiredArgsConstructor
public class DetailTotalPageCalculator implements Tasklet {
    private final ApiRequestManager apiRequestManager;

    /**
     * 총 페이지 수를 API에서 조회한 후, DetailPageNumberReader에 설정합니다.
     * DetailPageNumberReader: 상세 페이지 번호를 읽어오는 Reader
     *
     * @param contribution 현재 step의 기여도
     * @param chunkContext 현재 chunk 실행 컨텍스트
     * @return RepeatStatus.FINISHED (작업 완료 신호)
     * @author 함예정
     * @since 2025-05-02
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        int totalPage = apiRequestManager.getDetailTotalPage();
        DetailPageNumberReader.setTotalPage(totalPage);

        log("[CombineProcessor] 총 페이지 수 계산 완료: " + totalPage);
        return RepeatStatus.FINISHED;
    }
}