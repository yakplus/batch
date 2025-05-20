package com.likelion.backendplus4.yakplus.drug.embed.application.service;

import com.likelion.backendplus4.yakplus.drug.embed.application.port.in.EmbeddingRoutingUseCase;
import com.likelion.backendplus4.yakplus.drug.embed.application.port.out.EmbeddingSwitchPort;
import org.springframework.stereotype.Service;

import static com.likelion.backendplus4.yakplus.common.logging.util.LogUtil.log;

@Service
public class EmbeddingRouter implements EmbeddingRoutingUseCase {
    private final EmbeddingSwitchPort switchPort;

    public EmbeddingRouter(EmbeddingSwitchPort switchPort) {
        this.switchPort = switchPort;
    }

    /**
     * 지정된 adapter Bean 이름으로 Embedding adapter를 전환합니다.
     *
     * @param adapterBeanName 전환할 adapter Bean 이름
     * @author 정안식
     * @since 2025-05-02
     */
    @Override
    public void switchEmbedding(String adapterBeanName) {
        log("임베딩 스위치 요청 수신 - 어댑터명: " + adapterBeanName);
        switchPort.switchTo(adapterBeanName);
    }

    /**
     * 현재 활성화된 Embedding adapter Bean 이름을 반환합니다.
     *
     * @return 현재 adapter Bean 이름
     * @author 정안식
     * @since 2025-05-02
     */
    @Override
    public String getAdapterBeanName() {
        log("현재 선택된 어댑터 빈 이름 요청");
        return switchPort.getAdapterBeanName();
    }
}
