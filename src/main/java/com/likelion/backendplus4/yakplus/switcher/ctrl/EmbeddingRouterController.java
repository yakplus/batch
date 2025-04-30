package com.likelion.backendplus4.yakplus.switcher.ctrl;

import com.likelion.backendplus4.yakplus.switcher.EmbeddingPort;
import com.likelion.backendplus4.yakplus.switcher.svc.in.EmbeddingRoutingUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/test/embeddings")
public class EmbeddingRouterController {
    private final EmbeddingRoutingUseCase routerUseCase;
    private final Map<String, EmbeddingPort> adapters;

    public EmbeddingRouterController(EmbeddingRoutingUseCase routerUseCase,
                                     Map<String, EmbeddingPort> adapters) {
        this.routerUseCase = routerUseCase;
        this.adapters = adapters;
    }

    // 사용 가능한 어댑터 명단 확인
    @GetMapping("/adapters")
    public ResponseEntity<Set<String>> listAdapters() {
        return ResponseEntity.ok(adapters.keySet());
    }

    // 스위치 테스트
    @PostMapping("/switch/{name}")
    public ResponseEntity<String> switchAdapter(@PathVariable String name) {
        routerUseCase.switchEmbedding(name);
        return ResponseEntity.ok("Switched to: " + name);
    }

    // 임베딩 테스트 (더미 sysout 확인)
    @GetMapping("/embed")
    public ResponseEntity<float[]> embed(@RequestParam String text) {
        float[] vec = adapters.get("embeddingRouterAdapter").getEmbedding(text);
        return ResponseEntity.ok(vec);
    }
}
