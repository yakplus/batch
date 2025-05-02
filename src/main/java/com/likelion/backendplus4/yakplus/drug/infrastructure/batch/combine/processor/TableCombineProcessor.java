package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.combine.processor;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.drug.domain.model.vo.Material;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.combine.dto.TableCombineDto;

import lombok.RequiredArgsConstructor;

@Component
@StepScope
@RequiredArgsConstructor
public class TableCombineProcessor implements ItemProcessor<TableCombineDto, DrugRawDataEntity> {

	@Override
	public DrugRawDataEntity process(TableCombineDto entity) throws Exception {
		String imgUrl = null;

		String productImage = entity.getProductImage();
		if (productImage != null && entity.getProductImage().length() > 10) {
			imgUrl = entity.getProductImage();
		} else {
			imgUrl = entity.getPillImage();
		}

		return DrugRawDataEntity.builder()
			.drugId(entity.getDrugId())
			.drugName(entity.getDrugName())
			.company(entity.getCompany())
			.permitDate(entity.getPermitDate())
			.isGeneral(entity.isGeneral())
			.materialInfo(
				toStringFromObj(
					convertMaterialInfo(entity.getMaterialInfo())
				)
			)
			.storeMethod(entity.getStoreMethod())
			.validTerm(entity.getValidTerm())
			.efficacy(
				toStringFromObj(
					convertEfficacy(entity.getEfficacy())
				)
			)
			.usage(toStringFromObj(
					getUsage(entity.getUsage())
				)
			)
			.precaution(toStringFromObj(
					getPrecaution(entity.getPrecaution())
				)
			)
			.imageUrl(imgUrl)
			.cancelDate(entity.getCancelDate())
			.cancelName(entity.getCancelName())
			.isHerbal(entity.getIsHerbal())
			.build();
	}

	private static List<String> getUsage(String usage) {
		List<String> usages = new ArrayList<>();
		JsonNode json = toJsonNodeFromString(usage);
		if (!json.isNull() && json.has("sections")) {
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
		} catch (Exception e) {
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

		if (efficacys.size() == 0) {
			tryParseTitle(json, efficacys);
		}
		return efficacys;
	}

	private static List<String> tryParseTitle(JsonNode json, List<String> efficacys) {
		if (json.has("sections")) {
			for (JsonNode section : json.get("sections")) {
				for (JsonNode article : section.get("articles")) {
					efficacys.add(article.get("title").asText());
				}
			}
		}

		return efficacys;
	}

	private static List<String> tryParseParagraphs(JsonNode json, List<String> efficacys) {
		if (json.has("sections")) {
			for (JsonNode section : json.get("sections")) {
				for (JsonNode article : section.get("articles")) {
					for (JsonNode paragraph : article.get("paragraphs")) {
						String text = paragraph.get("text").asText();
						if (text != null && !text.isEmpty()) {
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
		if (json.has("sections")) {
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

	private static String toStringFromObj(Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("변환 실패");
			return null;
		}
	}

}
