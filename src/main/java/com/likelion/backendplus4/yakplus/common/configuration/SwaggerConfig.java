package com.likelion.backendplus4.yakplus.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("YakPlus API")
                        .description("YakPlus 프로젝트의 API 문서입니다.")
                        .version("1.0.0"));
    }
}
