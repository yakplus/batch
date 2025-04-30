package com.likelion.backendplus4.yakplus.switcher.impl;

import com.likelion.backendplus4.yakplus.switcher.EmbeddingPort;
import org.springframework.stereotype.Component;

@Component("azureEmbeddingAdapter")
public class AzureEmbeddingAdapter implements EmbeddingPort {
    @Override
    public float[] getEmbedding(String text) {
        System.out.println("[Azure] embedding for: " + text);
        return new float[]{4.0f, 5.0f, 6.0f};
    }
}
