package com.likelion.backendplus4.yakplus.drug.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DrugImage {
	@JsonProperty("ITEM_SEQ")
	private Long drugId;

	@JsonProperty("BIG_PRDT_IMG_URL")
	private String imageUrl;
}
