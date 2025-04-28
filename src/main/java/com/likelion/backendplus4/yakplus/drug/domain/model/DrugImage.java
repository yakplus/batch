package com.likelion.backendplus4.yakplus.drug.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 의약품 이미지 정보를 담는 객체입니다.
 */

@Builder
@Getter
@ToString
//TODO swagger / @ApiModel(description = "의약품 이미지 정보")
public class DrugImage {
	@JsonProperty("ITEM_SEQ")
	//TODO @ApiModelProperty(value = "의약품 ID", example = "12345")
	private Long drugId;

	@JsonProperty("BIG_PRDT_IMG_URL")
	//TODO @ApiModelProperty(value = "의약품 이미지 URL", example = "http://example.com/image.jpg")
	private String imageUrl;
}
