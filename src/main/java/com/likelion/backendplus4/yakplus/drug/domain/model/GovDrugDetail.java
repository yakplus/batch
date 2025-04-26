package com.likelion.backendplus4.yakplus.drug.domain.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.drug.domain.model.vo.Material;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GovDrugDetail {
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
	private Map<String,List<String>> precaution;


}
