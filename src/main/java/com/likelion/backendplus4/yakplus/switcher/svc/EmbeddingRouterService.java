package com.likelion.backendplus4.yakplus.switcher.svc;

import com.likelion.backendplus4.yakplus.switcher.svc.in.EmbeddingRoutingUseCase;
import com.likelion.backendplus4.yakplus.switcher.svc.out.EmbeddingSwitchPort;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class EmbeddingRouterService implements EmbeddingRoutingUseCase {
    private final EmbeddingSwitchPort switchPort;
    public EmbeddingRouterService(EmbeddingSwitchPort switchPort) {
        this.switchPort = switchPort;
    }

    @PostConstruct
    public void init() {
        switchPort.switchTo("openAIEmbeddingAdapter");
    }

    @Override
    public void switchEmbedding(String adapterBeanName) {
        switchPort.switchTo(adapterBeanName);
    }
}
