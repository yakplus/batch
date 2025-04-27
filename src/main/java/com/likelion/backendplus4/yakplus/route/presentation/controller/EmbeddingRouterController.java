package com.likelion.backendplus4.yakplus.route.presentation.controller;

import com.likelion.backendplus4.yakplus.route.application.port.in.EmbeddingRoutingUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/embeddings")
public class EmbeddingRouterController {

    private final EmbeddingRoutingUseCase embeddingRoutingUseCase;

    public EmbeddingRouterController(EmbeddingRoutingUseCase embeddingRoutingUseCase) {
        this.embeddingRoutingUseCase = embeddingRoutingUseCase;
    }

    @PostMapping("/switch/{name}")
    public ResponseEntity<String> switchEmbedding(@PathVariable String name) {
        embeddingRoutingUseCase.switchEmbedding(name);
        return ResponseEntity.ok("Switched embedding adapter to: " + name);
    }
}
