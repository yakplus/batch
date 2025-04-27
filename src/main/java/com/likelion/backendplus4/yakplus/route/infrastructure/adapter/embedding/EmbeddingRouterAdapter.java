package com.likelion.backendplus4.yakplus.route.infrastructure.adapter.embedding;

import com.likelion.backendplus4.yakplus.index.application.port.out.EmbeddingPort;
import com.likelion.backendplus4.yakplus.route.application.port.out.EmbeddingSwitchPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component("embeddingRouterAdapter")
@Primary
public class EmbeddingRouterAdapter implements EmbeddingPort, EmbeddingSwitchPort {

    private final Map<String, EmbeddingPort> adapters;
    private volatile EmbeddingPort embeddingPort;

    public EmbeddingRouterAdapter(Map<String, EmbeddingPort> allAdapters) {
        // 자기 자신("embeddingRouterAdapter")을 제외한 실제 구현체들만 보관
        this.adapters = allAdapters.entrySet().stream()
                .filter(e -> !"embeddingRouterAdapter".equals(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void switchTo(String adapterKey) {
        EmbeddingPort target = adapters.get(adapterKey);
        if (target == null) {
            throw new IllegalArgumentException("Unknown adapter: " + adapterKey);
        }
        this.embeddingPort = target;
    }

    @Override
    public float[] getEmbedding(String text) {
        if (embeddingPort == null) {
            throw new IllegalStateException("No Embedding adapter selected");
        }
        return embeddingPort.getEmbedding(text);
    }
}