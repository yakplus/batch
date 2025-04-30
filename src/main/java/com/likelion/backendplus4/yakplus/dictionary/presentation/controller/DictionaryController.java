package com.likelion.backendplus4.yakplus.dictionary.presentation.controller;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

import com.likelion.backendplus4.yakplus.dictionary.application.port.in.DictionaryUseCase;
import com.likelion.backendplus4.yakplus.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionary")
@RequiredArgsConstructor
public class DictionaryController {

    private final DictionaryUseCase dictionaryUseCase;

    @PostMapping("/set")
    public ResponseEntity<ApiResponse<Void>> setDictionary() {
        log("setDictionary() 호출");
        dictionaryUseCase.setDictionary();

        return ApiResponse.success();
    }

}
