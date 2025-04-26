package com.likelion.backendplus4.yakplus.drug.domain.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DrugImage {
	private Long drugId;
	private String imageUrl;
}
