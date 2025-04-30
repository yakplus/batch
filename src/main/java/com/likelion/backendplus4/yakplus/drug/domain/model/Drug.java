package com.likelion.backendplus4.yakplus.drug.domain.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.likelion.backendplus4.yakplus.drug.domain.model.vo.Material;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 의약품 정보를 담는 도메인 객체입니다.
 */

//TODO swagger/ @ApiModel(description = "의약품 정보")
@Builder
@Getter
@ToString
public class Drug {

	//TODO  @ApiModelProperty(value = "의약품 ID", example = "12345")
	private Long drugId;

	//TODO @ApiModelProperty(value = "의약품명", example = "타이레놀")
	private String drugName;

	//TODO @ApiModelProperty(value = "제조사명", example = "한국얀센")
	private String company;

	//TODO @ApiModelProperty(value = "허가일자", example = "2023-01-01")
	private LocalDate permitDate;

	//TODO @ApiModelProperty(value = "일반의약품 여부", example = "true")
	private boolean isGeneral;

	//TODO  @ApiModelProperty(value = "성분 정보 리스트")
	private List<Material> materialInfo;

	//TODO @ApiModelProperty(value = "보관 방법", example = "밀폐용기, 실온 보관")
	private String storeMethod;

	//TODO @ApiModelProperty(value = "유효 기간", example = "36개월")
	private String validTerm;

	//TODO @ApiModelProperty(value = "효능 효과", example = "[\"해열\", \"진통\"]")
	private List<String> efficacy;

	//TODO @ApiModelProperty(value = "사용 방법", example = "[\"1일 3회\", \"식후 복용\"]")
	private List<String> usage;

	//TODO @ApiModelProperty(value = "주의 사항", example = "{\"주의사항\": [\"임산부 주의\", \"운전 금지\"]}")
	private Map<String, List<String>> precaution;

	//TODO @ApiModelProperty(value = "의약품 이미지 URL", example = "http://example.com/image.jpg")
	private String imageUrl;

	//TODO @ApiModelProperty(value = "[float 배열]")
	private float[] vector;

	private LocalDate cancelDate;

	private String cancelName;

	private boolean isHerbal;
}
