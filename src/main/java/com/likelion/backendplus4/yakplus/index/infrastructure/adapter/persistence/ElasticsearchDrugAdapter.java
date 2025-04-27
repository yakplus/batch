package com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.index.application.port.out.DrugIndexRepositoryPort;
import com.likelion.backendplus4.yakplus.index.domain.model.Drug;
import com.likelion.backendplus4.yakplus.index.exception.IndexException;
import com.likelion.backendplus4.yakplus.index.exception.error.IndexErrorCode;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

/**
 * Elasticsearch를 통해 Drug 도메인 객체의 색인 기능을 제공하는 어댑터 클래스입니다.
 * DrugIndexRepositoryPort를 구현하여
 * Elasticsearch 원격 호출을 캡슐화합니다.
 *
 * @modified 2025-04-27
 * 25.04.27 - saveAll()를 Bulk 요청으로 전환
 * - buildBulkRequestBody(), createBulkRequest() 메서드 추가
 * @since 2025-04-22
 */
@Component
public class ElasticsearchDrugAdapter implements DrugIndexRepositoryPort {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public ElasticsearchDrugAdapter(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    /**
     * 주어진 Drug 목록을 Elasticsearch에 Bulk API로 일괄 저장합니다.
     *
     * @param esIndexName Bulk 대상 ES 인덱스 이름
     * @param drugs       저장할 Drug 객체 리스트
     * @throws IndexException 색인 처리 중 오류 발생 시
     * @author 정안식
     * @modified 2025-04-27
     * 25.04.27 - esIndexname을 인자로 받아 Bulk API로 일괄 저장하도록 수정
     * @since 2025-04-22
     */
    @Override
    public void saveAll(String esIndexName, List<Drug> drugs) {
        log("saveAll() 메서드 호출, 인덱스 이름: " + esIndexName + ", Drug 개수: " + drugs.size());
        try {
            String bulkBody = buildBulkRequestBody(esIndexName, drugs);
            Request bulkRequest = createBulkRequest(esIndexName, bulkBody);
            restClient.performRequest(bulkRequest);
            log("saveAll() 메서드 완료, 인덱스 이름: " + esIndexName + ", Drug 개수: " + drugs.size());
        } catch (Exception e) {
            throw new IndexException(IndexErrorCode.ES_SAVE_ERROR);
        }
    }

    /**
     * Bulk API 요청용 NDJSON 바디를 생성합니다.
     *
     * @param esIndexName Bulk 대상 ES 인덱스 이름
     * @param drugs       색인할 Drug 리스트
     * @return NDJSON 형식의 문자열
     * @throws Exception JSON 직렬화 오류 시
     * @author 정안식
     * @since 2025-04-27
     */
    private String buildBulkRequestBody(String esIndexName, List<Drug> drugs) throws Exception {
        log("buildBulkRequestBody() 메서드 호출, 인덱스 이름: " + esIndexName + ", Drug 개수: " + drugs.size());
        StringBuilder sb = new StringBuilder();
        for (Drug drug : drugs) {
            Map<String, Object> action = Map.of("index", Map.of("_index", esIndexName, "_id", drug.getDrugId().toString()));
            sb.append(objectMapper.writeValueAsString(action)).append("\n");
            sb.append(objectMapper.writeValueAsString(createDrugDocument(drug))).append("\n");
        }
        return sb.toString();
    }

    /**
     * Bulk 요청을 위한 Request 객체를 생성합니다.
     *
     * @param esIndexName Bulk 엔드포인트에 사용할 ES 인덱스 이름
     * @param bulkBody    NDJSON 형식의 Bulk 요청 바디
     * @return Bulk 용 Request
     * @author 정안식
     * @since 2025-04-27
     */
    private Request createBulkRequest(String esIndexName, String bulkBody) {
        log("createBulkRequest() 메서드 호출, 인덱스 이름: " + esIndexName);
        Request request = new Request("POST", "/" + esIndexName + "/_bulk");
        request.setEntity(new NStringEntity(bulkBody, ContentType.APPLICATION_JSON));
        return request;
    }

    /**
     * Drug 객체와 임베딩 벡터를 기반으로 Elasticsearch 색인용 문서 필드 맵을 생성한다.
     *
     * @param drug 색인할 Drug 도메인 객체
     * @return Elasticsearch에 저장할 문서 필드 맵
     * @author 정안식
     * @modified 2025-04-27
     * 25.04.27 - 변경된 Drug 도메인 객체 내부 필드에 맞춰 수정
     * @since 2025-04-22
     */
    private Map<String, Object> createDrugDocument(Drug drug) {
        return Map.of("drugId", drug.getDrugId(), "drugName", drug.getDrugName(), "company", drug.getCompany(), "efficacy", drug.getEfficacy(), "imageUrl", drug.getImageUrl(), "vector", drug.getVector());
    }
}