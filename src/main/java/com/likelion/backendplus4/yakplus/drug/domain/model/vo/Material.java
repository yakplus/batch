package com.likelion.backendplus4.yakplus.drug.domain.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

/**
 * 의약품 성분 정보를 나타내는 값 객체(Value Object)입니다.
 */
//TODO: swagger/ @ApiModel(description = "의약품 성분 정보")
@Getter
@ToString
public class Material {
	//TODO  @ApiModelProperty(value = "성분명", example = "아세트아미노펜")
	@JsonProperty("성분명")
	private String name;

	//TODO @ApiModelProperty(value = "분량", example = "500")
	@JsonProperty("분량")
	private String amount;

	//TODO @ApiModelProperty(value = "단위", example = "mg")
	@JsonProperty("단위")
	private String unit;

	//TODO @ApiModelProperty(value = "총량", example = "1000")
	@JsonProperty("총량")
	private String totalAmount;

	//TODO @ApiModelProperty(value = "규격", example = "USP")
	@JsonProperty("규격")
	private String standard;

	//TODO @ApiModelProperty(value = "비고", example = "해열진통제")
	@JsonProperty("비고")
	private String note;

	//TODO @ApiModelProperty(value = "성분정보", example = "기타 부가 정보")
	@JsonProperty("성분정보")
	private String info;
}
