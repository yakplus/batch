package com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.persistence.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DrugImageRequest {

	@JsonProperty("ITEM_SEQ")
	private Long drugId;

	private String productImage;

	@JsonProperty("BIG_PRDT_IMG_URL")
	private String pillImageUrl;

	public DrugImageRequest changeProductImageUrl(String productImage){
		this.productImage = productImage;
		return this;
	}
}