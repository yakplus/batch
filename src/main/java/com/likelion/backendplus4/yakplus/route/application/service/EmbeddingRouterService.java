package com.likelion.backendplus4.yakplus.route.application.service;

import com.likelion.backendplus4.yakplus.drug.domain.model.port.out.EmbeddingPort;
import com.likelion.backendplus4.yakplus.route.application.port.in.EmbeddingRoutingUseCase;
import com.likelion.backendplus4.yakplus.route.application.port.out.EmbeddingSwitchPort;
import com.likelion.backendplus4.yakplus.route.infrastructure.adapter.embedding.EmbeddingRouterAdapter;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmbeddingRouterService implements EmbeddingRoutingUseCase {

    private final EmbeddingSwitchPort embeddingSwitchPort;
    private final Map<String, EmbeddingPort> adapters;
    private final String defaultAdapterBeanName = "openAIEmbeddingAdapter";

    public EmbeddingRouterService(
            EmbeddingSwitchPort embeddingSwitchPort,
            Map<String, EmbeddingPort> adapters
    ) {
        this.embeddingSwitchPort = embeddingSwitchPort;
        this.adapters = adapters;
    }

    @PostConstruct
    public void initDefault() {
        EmbeddingPort defaultAdapter = adapters.get(defaultAdapterBeanName);
        if (defaultAdapter == null) {
            throw new IllegalStateException("Missing default embedding adapter: " + defaultAdapterBeanName);
        }
        embeddingSwitchPort.switchTo(defaultAdapterBeanName);
    }

    @Override
    public void switchEmbedding(String adapterBeanName) {
        if (!adapters.containsKey(adapterBeanName)) {
            throw new IllegalArgumentException("No such Embedding bean: " + adapterBeanName);
        }
        embeddingSwitchPort.switchTo(adapterBeanName);
    }
}
