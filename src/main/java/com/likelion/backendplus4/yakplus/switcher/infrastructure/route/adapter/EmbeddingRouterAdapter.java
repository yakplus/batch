package com.likelion.backendplus4.yakplus.switcher.infrastructure.route.adapter;

import com.likelion.backendplus4.yakplus.common.batch.infrastructure.embed.dto.DrugVectorDto;
import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.index.application.port.out.EmbeddingLoadingPort;
import com.likelion.backendplus4.yakplus.switcher.application.port.out.EmbeddingSwitchPort;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

@Component("embeddingRouterAdapter")
@Primary
public class EmbeddingRouterAdapter implements EmbeddingLoadingPort, EmbeddingSwitchPort {
    @Value("${embed.switcher.default-adapter}")
    private String DEFAULT_ADAPTER; // 임베딩 로딩 어뎁터 이름 변경 시 수동으로 수정해야함!!!!!!!
    private final Map<String, EmbeddingLoadingPort> adapters;
    private volatile EmbeddingLoadingPort embeddingLoadingPort;
    private volatile String adapterBeanName;

    /**
     * 모든 EmbeddingPort 구현체를 주입받아 어댑터 라우팅 맵을 초기화합니다.
     *
     * @param allAdapters 어댑터 빈 이름을 키로 하고 EmbeddingPort 구현체를 값으로 갖는 맵
     * @author 정안식
     * @since 2025-05-02
     */
    public EmbeddingRouterAdapter(Map<String, EmbeddingLoadingPort> allAdapters) {
        this.adapters = allAdapters;
        log("구현체 목록: " + adapters.keySet());
    }

    /**
     * 컴포넌트 초기화 후 기본 어댑터로 전환합니다.
     *
     * @author 정안식
     * @since 2025-05-02
     */
    @PostConstruct
    public void init() {
        log("EmbeddingRouterAdapter 초기화 - 어댑터명: " + DEFAULT_ADAPTER);
        switchTo(DEFAULT_ADAPTER);
    }

    /**
     * 주어진 페이지 정보(Pageable)에 따라 데이터베이스에서 원시 약품 데이터와 임베딩 데이터를
     * 조인하여 한 페이지 분량의 Drug 도메인 객체 목록을 조회합니다.
     *
     * @param pageable 조회할 페이지 번호와 크기, 정렬 정보 등을 포함하는 Pageable 객체
     * @return 지정된 페이지 범위에 해당하는 Drug 도메인 객체들의 리스트
     * @author 이해창
     * @since 2025-05-03
     */
    @Override
    public List<Drug> loadEmbeddingsByPage(Pageable pageable) {
        if (embeddingLoadingPort == null) {
            log(LogLevel.ERROR, "임베딩 어댑터가 선택되지 않았습니다.");
            throw new IllegalStateException("No adapter selected");
        }
        return embeddingLoadingPort.loadEmbeddingsByPage(pageable);
    }

    /**
     * 현재 선택된 어댑터로부터 임베딩 벡터를 반환합니다.
     *
     * @param text 임베딩을 생성할 입력 문자열
     * @return 입력 문자열에 대한 임베딩 벡터 배열
     * @throws IllegalStateException 어댑터가 선택되지 않은 경우
     * @author 정안식
     * @since 2025-05-02
     */
    @Override
    public float[] getEmbedding(String text) {
        if (embeddingLoadingPort == null) {
            log(LogLevel.ERROR, "임베딩 어댑터가 선택되지 않았습니다.");
            throw new IllegalStateException("No adapter selected");
        }
        return embeddingLoadingPort.getEmbedding(text);
    }

    /**
     * 전달된 DrugVectorDto 리스트에 포함된 약품 ID와 해당 임베딩 벡터를
     * 데이터베이스에 일괄 저장합니다.
     *
     * @param dtos 저장할 약품 임베딩 정보 리스트 (DrugVectorDto 객체)
     * @author 이해창
     * @since 2025-04-25
     * @modified
     * 2025-05-02 - 배치 적용으로 인한 입력 타입 변경
     * 2025-04-30 - 임베딩 저장/로딩포트 통합으로 인한 위치 이동
     */
    @Override
    public void saveEmbedding(List<DrugVectorDto> dtos) {
        if (embeddingLoadingPort == null) {
            log(LogLevel.ERROR, "임베딩 어댑터가 선택되지 않았습니다.");
            throw new IllegalStateException("No adapter selected");
        }
        embeddingLoadingPort.saveEmbedding(dtos);
    }

    /**
     * 지정된 Bean 이름에 해당하는 어댑터로 전환합니다.
     *
     * @param adapterBeanName 전환할 어댑터 Bean 이름
     * @throws IllegalArgumentException 지원되지 않는 어댑터 이름인 경우
     * @author 정안식
     * @since 2025-05-02
     */
    @Override
    public void switchTo(String adapterBeanName) {
        log("어댑터 스위치 시도 - 어댑터명: " + adapterBeanName);
        EmbeddingLoadingPort target = adapters.get(adapterBeanName);
        if (target == null) {
            log(LogLevel.ERROR, "어댑터 빈을 찾을 수 없습니다: " + adapterBeanName);
            throw new IllegalArgumentException("Unknown adapter: " + adapterBeanName);
        }
        this.embeddingLoadingPort = target;
        this.adapterBeanName = adapterBeanName;
        log("어댑터 스위치 완료 - 현재 어댑터: " + adapterBeanName);
    }

    /**
     * 현재 활성화된 어댑터 Bean 이름을 반환합니다.
     *
     * @return 활성화된 어댑터 Bean 이름
     * @author 정안식
     * @since 2025-05-02
     */
    @Override
    public String getAdapterBeanName() {
        log("어댑터 빈 이름 요청 - 현재 선택된 어댑터: " + adapterBeanName);
        return adapterBeanName;
    }
}
