package com.likelion.backendplus4.yakplus.drug.domain.model;

import java.time.LocalDate;
import java.util.*;

import com.likelion.backendplus4.yakplus.drug.domain.model.vo.Material;

import com.likelion.backendplus4.yakplus.drug.index.support.parser.SymptomTextParser;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 의약품 정보를 담는 도메인 객체입니다.
 */
@Builder
@Getter
@ToString
public class Drug {
	private Long drugId;
	private String drugName;
	private String company;
	private LocalDate permitDate;
	private boolean isGeneral;
	private List<Material> materialInfo;
	private String storeMethod;
	private String validTerm;
	private List<String> efficacy;
	private List<String> usage;
	private Map<String, List<String>> precaution;
	private String imageUrl;
	private float[] vector;
	private LocalDate cancelDate;
	private String cancelName;
	private boolean isHerbal;

	/**
	 * 효능 설명 리스트(`efficacy`)를 자동완성에 적합한 토큰 리스트로 변환합니다.
	 * 내부적으로 효능 내용을 하나의 문자열로 평탄화한 후,
	 * 검색 자동완성에 적합한 형태로 토큰화합니다.
	 *
	 * @return 자동완성용 효능 토큰 리스트 (비어있을 경우 빈 리스트 반환)
	 * @author 박찬병
	 * @since 2025-05-03
	 */
	public List<String> generateEfficacySuggestions() {
		if (efficacy == null || efficacy.isEmpty()) return List.of();
		String flat = SymptomTextParser.flattenLines(efficacy);
		return SymptomTextParser.tokenizeForSuggestion(flat);
	}

	/**
	 * 의약품 성분 정보(`materialInfo`)에서 성분명을 추출하여
	 * 자동완성에 사용할 성분명 리스트를 생성합니다.
	 *
	 * null 값이나 이름이 없는 성분은 자동으로 제외되며,
	 * 중복 제거는 호출 측에서 수행합니다.
	 *
	 * @return 자동완성용 성분명 리스트 (비어있을 경우 빈 리스트 반환)
	 * @author 박찬병
	 * @since 2025-05-03
	 */
	public List<String> generateIngredientSuggestions() {
		return Optional.ofNullable(materialInfo)
				.orElse(Collections.emptyList())
				.stream()
				.map(Material::getName)
				.filter(Objects::nonNull)
				.toList();
	}

}
