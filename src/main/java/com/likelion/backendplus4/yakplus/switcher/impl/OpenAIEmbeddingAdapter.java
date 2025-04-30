package com.likelion.backendplus4.yakplus.switcher.impl;

import com.likelion.backendplus4.yakplus.switcher.EmbeddingPort;
import org.springframework.stereotype.Component;

@Component("openAIEmbeddingAdapter")
public class OpenAIEmbeddingAdapter implements EmbeddingPort {
    @Override
    public float[] getEmbedding(String text) {
        System.out.println("[OpenAI] embedding for: " + text);
        return new float[]{1.0f, 2.0f, 3.0f};
    }
}