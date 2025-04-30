package com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.drug.domain.model.DrugDetail;
import com.likelion.backendplus4.yakplus.drug.domain.model.vo.Material;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugDetailEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;

public class DrugDetailMapper {

	public static DrugDetail toDomainFromEntity(DrugDetailEntity e){
		DrugDetail domain = DrugDetail.builder()
			.drugId(e.getDrugId())
			.drugName(e.getDrugName())
			.company(e.getCompany())
			.permitDate(e.getPermitDate())
			.isGeneral(e.isGeneral())
			.materialInfo(convertMaterialInfo(e.getMaterialInfo()))
			.storeMethod(e.getStoreMethod())
			.validTerm(e.getValidTerm())
			.efficacy(convertEfficacy(e.getEfficacy()))
			.usage(getUsage(e.getUsage()))
			.precaution(getPrecaution(e.getPrecaution()))
			.cancelDate(e.getCancelDate())
			.cancelName(e.getCancelName())
			.isHerbal(e.isHerbal())
			.build();
		return domain;
	}

	public static Drug toDomainFromEntity(DrugRawDataEntity e){
		return Drug.builder()
			.drugId(e.getDrugId())
			.drugName(e.getDrugName())
			.company(e.getCompany())
			.permitDate(e.getPermitDate())
			.isGeneral(e.isGeneral())
			.materialInfo(convertMaterialInfo(e.getMaterialInfo()))
			.storeMethod(e.getStoreMethod())
			.validTerm(e.getValidTerm())
			.efficacy(convertEfficacy(e.getEfficacy()))
			.usage(getUsage(e.getUsage()))
			.precaution(getPrecaution(e.getPrecaution()))
			.cancelDate(e.getCancelDate())
			.cancelName(e.getCancelName())
			.isHerbal(e.isHerbal())
			.build();

	}

	private static float[] toArraysFromFloatString(String floatString){
		try {
			return new ObjectMapper().readValue(floatString, float[].class);
		} catch (Exception e) {
			throw new RuntimeException("float 배열로 변환 실패");
		}
	}
	private static List<String> getUsage(String usage){
		List<String> usages = new ArrayList<>();
		JsonNode json = toJsonNodeFromString(usage);
		if(!json.isNull() && json.has("sections")){
			for (JsonNode section : json.get("sections")) {
				for (JsonNode article : section.get("articles")) {
					for (JsonNode paragraph : article.get("paragraphs")) {
						usages.add(paragraph.get("text").asText());
					}
				}
			}
		}
		return usages;
	}

	private static List<Material> convertMaterialInfo(String material) {
		JsonNode json = toJsonNodeFromString(material);
		if (json.isArray()) {
			return mapFromMaterialJson(json);
		}
		return null;
	}

	private static List<Material> mapFromMaterialJson(JsonNode json) {
		List<Material> matrerials = new ArrayList<>();

		ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			for (JsonNode node : json) {
				Material ingredient = objectMapper.treeToValue(node, Material.class);
				matrerials.add(ingredient);
			}
			return matrerials;
		} catch (Exception e){
			log(LogLevel.ERROR, "객체 맵핑 실패", e);
			return null;
		}
	}

	private static JsonNode toJsonNodeFromString(String json) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readTree(json);
		} catch (Exception e) {
			log(LogLevel.ERROR, "json 객체 생성 에러", e);
			return null;
		}
	}

	private static List<String> convertEfficacy(String efficacy) {
		JsonNode json = toJsonNodeFromString(efficacy);
		List<String> efficacys = new ArrayList<>();
		tryParseParagraphs(json, efficacys);

		if(efficacys.size() == 0){
			tryParseTitle(json, efficacys);
		}
		return efficacys;
	}

	private static List<String> tryParseTitle(JsonNode json, List<String> efficacys) {
		if(json.has("sections")){
			for (JsonNode section : json.get("sections")) {
				for (JsonNode article : section.get("articles")) {
					efficacys.add(article.get("title").asText());
				}
			}
		}

		return efficacys;
	}

	private static List<String> tryParseParagraphs(JsonNode json, List<String> efficacys) {
		if(json.has("sections")){
			for (JsonNode section : json.get("sections")) {
				for (JsonNode article : section.get("articles")) {
					for (JsonNode paragraph : article.get("paragraphs")) {
						String text = paragraph.get("text").asText();
						if(text != null && !text.isEmpty()){
							efficacys.add(paragraph.get("text").asText());
						}

					}
				}
			}
		}

		return efficacys;
	}

	private static Map<String, List<String>> getPrecaution(String precaution) {
		Map<String, List<String>> result = new LinkedHashMap<>();

		JsonNode json = toJsonNodeFromString(precaution);
		if(json.has("sections")){
			JsonNode articles = json.get("sections").get(0).get("articles");
			for (JsonNode article : articles) {
				String title = article.get("title").asText();
				List<String> texts = new ArrayList<>();
				for (JsonNode paragraph : article.get("paragraphs")) {
					texts.add(paragraph.get("text").asText());
				}
				result.put(title, texts);
			}
		}

		return result;
	}

	public JsonNode toJson(String json) {
		try {
			return new ObjectMapper().readValue(json, JsonNode.class);
		} catch (JsonProcessingException e) {
			//TODO 에러 로그 처리 필요합니다.
			throw new RuntimeException(e);
		}
	}

}
