package com.likelion.backendplus4.yakplus.drug.domain.model.vo;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

/**
 * 의약품의 성분 총량 및 개별 성분 정보를 포함하는 객체입니다.
 */

//TODO: swagger/ @ApiModel(description = "의약품 성분 정보 집합")
@Getter
@ToString
public class MaterialInfo {

	//TODO @ApiModelProperty(value = "총량", example = "1000")
	private String totalAmount;

	//TODO @ApiModelProperty(value = "성분 리스트", example = "[{name: '아세트아미노펜', amount: '500', unit: '밀리그램'}]")
	private List<Material> ingredients;
}