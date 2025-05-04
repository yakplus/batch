package com.likelion.backendplus4.yakplus.common.batch.infrastructure.combine.processor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.common.batch.infrastructure.combine.dto.TableCombineDto;
import com.likelion.backendplus4.yakplus.common.batch.infrastructure.exception.ParserBatchError;
import com.likelion.backendplus4.yakplus.common.batch.infrastructure.exception.ParserBatchException;
import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.drug.domain.model.vo.Material;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.StreamSupport;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

/**
 * TableCombineDto를 DrugRawDataEntity로 변환하는 Spring Batch ItemProcessor입니다.
 * <p>
 * 상세 정보와 이미지 정보를 병합하여 최종 저장 가능한 구조로 가공하며,
 * 효능, 사용법, 주의사항, 성분 정보는 JSON 파싱 및 구조 변환을 통해 정제됩니다.
 *
 * @modified 2025-05-02 함예정
 * - 스프링 배치로 수정
 * @since 2025-04-21
 */
@Component
@RequiredArgsConstructor
public class TableCombineProcessor implements ItemProcessor<TableCombineDto, DrugRawDataEntity> {

    /**
     * TableCombineDto를 DrugRawDataEntity로 변환합니다.
     * <p>
     * 이미지 정보는 productImage가 유효할 경우 우선 사용하며, 그렇지 않으면 pillImage를 사용합니다.
     * 효능, 사용법, 주의사항, 성분 정보는 JSON 문자열을 구조화된 형태로 파싱 후 직렬화합니다.
     *
     * @param dto 병합된 의약품 상세 + 이미지 DTO
     * @return DrugRawDataEntity   저장 가능한 형태의 엔티티 객체
     * @author 함예정
     * @since 2025-05-02
     */
    @Override
    public DrugRawDataEntity process(TableCombineDto dto) {
        String imgUrl = getCoverImageFromProductAndPill(dto);

        return DrugRawDataEntity.builder()
                .drugId(dto.getDrugId())
                .drugName(dto.getDrugName())
                .company(dto.getCompany())
                .permitDate(dto.getPermitDate())
                .isGeneral(dto.isGeneral())
                .materialInfo(toStringFromObj(convertMaterialInfo(dto.getMaterialInfo())))
                .storeMethod(dto.getStoreMethod())
                .validTerm(dto.getValidTerm())
                .efficacy(toStringFromObj(convertEfficacy(dto.getEfficacy())))
                .usage(toStringFromObj(getUsage(dto.getUsage())))
                .precaution(toStringFromObj(getPrecaution(dto.getPrecaution())))
                .imageUrl(imgUrl)
                .cancelDate(dto.getCancelDate())
                .cancelName(dto.getCancelName())
                .isHerbal(dto.getIsHerbal())
                .build();
    }

    /**
     * 제품 이미지가 유효할 경우 우선 사용하고, 그렇지 않으면 알약 이미지를 반환합니다.
     *
     * @param dto TableCombineDto 병합된 의약품 상세 + 이미지 DTO
     */
    private String getCoverImageFromProductAndPill(TableCombineDto dto) {
        String imgUrl = (dto.getProductImage() != null && dto.getProductImage().length() > 10)
                ? dto.getProductImage()
                : dto.getPillImage();
        return imgUrl;
    }

    /**
     * 사용법 텍스트를 파싱하여 문단별 설명 리스트를 추출합니다.
     *
     * @param usage 사용법 JSON 문자열
     * @return List<String> 사용법 문장 리스트
     * @author 함예정
     * @since 2025-05-02
     */
    private List<String> getUsage(String usage) {
        JsonNode json = toJsonNodeFromString(usage);

        if (json.isNull() || !json.has("sections")) {
            return Collections.emptyList();
        }

        return StreamSupport.stream(json.get("sections").spliterator(), false)
                .flatMap(section -> StreamSupport.stream(section.get("articles").spliterator(), false))
                .flatMap(article -> StreamSupport.stream(article.get("paragraphs").spliterator(), false))
                .map(paragraph -> paragraph.get("text").asText())
                .toList();
    }

    /**
     * 성분 정보를 JSON 문자열로 변환합니다.
     *
     * @param material 성분 정보 JSON 문자열
     * @return List<Material> 성분 리스트
     * @author 함예정
     * @since 2025-05-02
     */
    private List<Material> convertMaterialInfo(String material) {
        JsonNode json = toJsonNodeFromString(material);
        if (json.isArray()) {
            return mapFromMaterialJson(json);
        }
        return null;
    }

    /**
     * JSON 노드를 Material 객체 리스트로 변환합니다.
     *
     * @param json JSON 노드
     * @return List<Material> 성분 리스트
     * @author 함예정
     * @since 2025-05-02
     */
    private List<Material> mapFromMaterialJson(JsonNode json) {
        List<Material> materials = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            for (JsonNode node : json) {
                Material ingredient = objectMapper.treeToValue(node, Material.class);
                materials.add(ingredient);
            }
            return materials;
        } catch (Exception e) {
            log(LogLevel.ERROR, "객체 맵핑 실패", e);
            return null;
        }
    }

    /**
     * JSON 문자열을 JsonNode 객체로 변환합니다.
     *
     * @param json JSON 문자열
     * @return JsonNode 변환된 JSON 노드
     * @author 함예정
     * @since 2025-05-02
     */
    private JsonNode toJsonNodeFromString(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(json);
        } catch (Exception e) {
            log(LogLevel.ERROR, "json 객체 생성 에러", e);
            return null;
        }
    }

    /**
     * 효능 정보를 JSON 문자열로 변환합니다.
     *
     * @param efficacyJsonString 효능 정보 JSON 문자열
     * @return List<String> 효능 리스트
     * @author 함예정
     * @since 2025-05-02
     */
    private List<String> convertEfficacy(String efficacyJsonString) {
        JsonNode json = toJsonNodeFromString(efficacyJsonString);
        List<String> efficacy = new ArrayList<>();
        tryParseParagraphs(json, efficacy);

        if (efficacy.isEmpty()) {
            tryParseTitle(json, efficacy);
        }
        return efficacy;
    }

    /**
     * JSON 노드에서 제목을 추출하여 효능 리스트에 추가합니다.
     *
     * @param json      JSON 노드
     * @param efficacy 효능 리스트
     * @author 함예정
     * @since 2025-05-02
     */
    private void tryParseTitle(JsonNode json, List<String> efficacy) {
        if (json.has("sections")) {
            for (JsonNode section : json.get("sections")) {
                for (JsonNode article : section.get("articles")) {
                    efficacy.add(article.get("title").asText());
                }
            }
        }
    }

    /**
     * JSON 노드에서 문단을 추출하여 효능 리스트에 추가합니다.
     *
     * @param json     JSON 노드
     * @param efficacy 효능 리스트
     * @author 함예정
     * @since 2025-05-02
     */
    private void tryParseParagraphs(JsonNode json, List<String> efficacy) {
        if (json.has("sections")) {
            List<String> parsed = StreamSupport.stream(json.get("sections").spliterator(), false)
                    .flatMap(section -> StreamSupport.stream(section.get("articles").spliterator(), false))
                    .flatMap(article -> StreamSupport.stream(article.get("paragraphs").spliterator(), false))
                    .map(paragraph -> paragraph.get("text").asText())
                    .filter(text -> text != null && !text.isEmpty())
                    .toList();

            efficacy.addAll(parsed);
        }
    }

    /**
     * 주의사항 정보를 JSON 문자열로 변환합니다.
     *
     * @param precaution 주의사항 JSON 문자열
     * @return Map<String, List < String>> 주의사항 맵
     * @author 함예정
     * @since 2025-05-02
     */
    private Map<String, List<String>> getPrecaution(String precaution) {
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

    /**
     * 객체를 JSON 문자열로 변환합니다.
     *
     * @param obj 변환할 객체
     * @return JSON 문자열
     * @author 함예정
     * @since 2025-05-02
     */
    private String toStringFromObj(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            log(LogLevel.ERROR, "JSON 변환 에러", e);
            throw new ParserBatchException(ParserBatchError.JSON_TYPE_CHANGE_FAIL);
        }
    }

}
