package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.embed.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DrugVectorDto {
	private Long drugId;
	private float[] vector;
}
