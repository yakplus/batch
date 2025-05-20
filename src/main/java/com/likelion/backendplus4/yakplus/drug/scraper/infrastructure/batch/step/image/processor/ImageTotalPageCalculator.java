package com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.batch.step.image.processor;

import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.batch.step.image.reader.PageRangePartitioner;
import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.api.util.ApiRequestManager;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import static com.likelion.backendplus4.yakplus.common.logging.util.LogUtil.log;

/**
 * 이미지 총 페이지 수를 계산하는 Tasklet 클래스입니다.
 * <p>
 * 이 클래스는 Spring Batch의 Tasklet 인터페이스를 구현하여,
 * 이미지 API에서 총 페이지 수를 가져와 PageRangePartitioner에 설정합니다.
 * </p>
 *
 * @field pageRangePartitioner 페이지 범위를 나누는 Partitioner
 * @field apiRequestManager API 요청을 관리하는 매니저
 * @since 2025-05-02
 */
@Component
@RequiredArgsConstructor
public class ImageTotalPageCalculator implements Tasklet {

    private final PageRangePartitioner pageRangePartitioner;
    private final ApiRequestManager apiRequestManager;

    /**
     * 이미지 API에서 총 페이지 수를 가져와 PageRangePartitioner에 설정합니다.
     *
     * @param contribution 현재 step의 기여도
     * @param chunkContext 현재 chunk 실행 컨텍스트
     * @return RepeatStatus.FINISHED (작업 완료 신호)
     * @author 함예정
     * @since 2025-05-02
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        int totalPage = apiRequestManager.getImageTotalPage();
        pageRangePartitioner.setTotalPages(totalPage);

        log("[Image-Total-Page-Calculator] 총 페이지 수 계산 완료: " + totalPage);
        return RepeatStatus.FINISHED;
    }
}