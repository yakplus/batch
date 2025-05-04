package com.likelion.backendplus4.yakplus.drug.embed.infrastructure.batch.step.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 의약품 임베딩 결과를 담는 DTO 클래스입니다.
 * drugId는 대상 의약품의 식별자이며, vector는 모델로부터 생성된 임베딩 벡터입니다.
 *
 * @field drugId 의약품의 식별자
 * @field vector 모델로부터 생성된 임베딩 벡터
 * @since 2025-05-02
 */
@Builder
@Getter
public class DrugVectorDto {
    private Long drugId;
    private float[] vector;
}
