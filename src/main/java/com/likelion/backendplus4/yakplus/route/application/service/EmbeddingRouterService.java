package com.likelion.backendplus4.yakplus.route.application.service;

import com.likelion.backendplus4.yakplus.route.application.port.in.EmbeddingRoutingUseCase;
import com.likelion.backendplus4.yakplus.route.application.port.out.EmbeddingSwitchPort;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class EmbeddingRouterService implements EmbeddingRoutingUseCase {

    private final EmbeddingSwitchPort embeddingSwitchPort;
    private final String defaultAdapterBeanName = "openAIEmbeddingAdapter";

    public EmbeddingRouterService(
            EmbeddingSwitchPort embeddingSwitchPort
    ) {
        this.embeddingSwitchPort = embeddingSwitchPort;
    }

    @PostConstruct
    public void initDefault() {
        try {
            embeddingSwitchPort.switchTo(defaultAdapterBeanName);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Failed to switch to default embedding adapter: " + defaultAdapterBeanName, e);
        }
    }

    @Override
    public void switchEmbedding(String adapterBeanName) {
        try {
            embeddingSwitchPort.switchTo(adapterBeanName);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Failed to switch to embedding adapter: " + adapterBeanName, e);
        }
    }
}
