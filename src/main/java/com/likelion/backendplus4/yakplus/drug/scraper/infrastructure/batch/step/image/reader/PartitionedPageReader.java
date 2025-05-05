package com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.batch.step.image.reader;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.likelion.backendplus4.yakplus.common.logging.util.LogUtil.log;

/**
 * PartitionedPageReader는 Spring Batch의 ItemReader를 구현하여
 * 이미지 페이지를 읽어오는 역할을 합니다.
 * 이 클래스는 StepScope로 설정되어, 각 파티션에 대해 독립적인 상태를 유지합니다.
 *
 * @field currentPage 현재 읽고 있는 페이지 번호
 * @field endPage 읽을 페이지의 마지막 번호
 * @since 2025-05-02
 * @author 함예정
 */
@Component
@StepScope
public class PartitionedPageReader implements ItemReader<Integer> {
    private int currentPage;
    private final int endPage;

    /**
     * PartitionedPageReader의 생성자.
     * <p>
     * Spring Batch의 파티셔닝에서 사용되는 {@code stepExecutionContext}로부터 startPage와 endPage 값을 주입받아,
     * 각 슬레이브 Step이 처리해야 할 페이지 범위를 설정합니다.
     * <p>
     * {@code @Value}는 Spring의 Expression Language(SpEL)를 사용하여 실행 컨텍스트에서 값을 추출합니다.
     * 예를 들어, 파티셔너가 {@code startPage=1, endPage=10}으로 설정하면 이 값이 해당 슬레이브에 주입됩니다.
     *
     * @param startPage 파티션에서 처리할 시작 페이지 번호
     * @param endPage   파티션에서 처리할 마지막 페이지 번호
     * @author 함예정
     * @since 2025-05-02
     */
    public PartitionedPageReader(
            @Value("#{stepExecutionContext['startPage']}") int startPage,
            @Value("#{stepExecutionContext['endPage']}") int endPage) {
        log("[Reader bean 생성] startPage=" + startPage + ", endPage=" + endPage);
        this.currentPage = startPage;
        this.endPage = endPage;
    }
    
    /**
     * 현재 페이지 번호를 반환하고 다음 페이지로 증가시킵니다.
     * <p>
     * 지정된 endPage를 초과하면 {@code null}을 반환하여 더 이상 읽을 데이터가 없음을 Spring Batch에 알립니다.
     *
     * @return 현재 페이지 번호, 또는 endPage를 초과하면 null
     * @author 함예정
     * @since 2025-05-02
     */
    @Override
    public Integer read() {
        log("이미지 페이지 Read: " + currentPage);
        if (currentPage > endPage) {
            return null;
        }
        return currentPage++;
    }
}
