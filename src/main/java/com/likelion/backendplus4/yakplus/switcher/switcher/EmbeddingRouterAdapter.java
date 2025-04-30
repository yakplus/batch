package com.likelion.backendplus4.yakplus.switcher.switcher;

import com.likelion.backendplus4.yakplus.switcher.EmbeddingPort;
import com.likelion.backendplus4.yakplus.switcher.svc.out.EmbeddingSwitchPort;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("embeddingRouterAdapter")
@Primary
public class EmbeddingRouterAdapter implements EmbeddingPort, EmbeddingSwitchPort {
    private final Map<String, EmbeddingPort> adapters;
    private volatile EmbeddingPort embeddingPort;

    public EmbeddingRouterAdapter(Map<String, EmbeddingPort> allAdapters) {
        // Map에 주입된 빈 확인
        this.adapters = allAdapters;
        System.out.println("Injected adapters: " + adapters.keySet());
    }

    @PostConstruct
    public void init() {
        // 기본 어댑터 설정
        switchTo("openAIEmbeddingAdapter");
    }

    @Override
    public void switchTo(String adapterKey) {
        System.out.println("Switching to: " + adapterKey);
        EmbeddingPort target = adapters.get(adapterKey);
        if (target == null) {
            throw new IllegalArgumentException("Unknown adapter: " + adapterKey);
        }
        this.embeddingPort = target;
    }

    @Override
    public float[] getEmbedding(String text) {
        if (embeddingPort == null) {
            throw new IllegalStateException("No adapter selected");
        }
        return embeddingPort.getEmbedding(text);
    }
}
