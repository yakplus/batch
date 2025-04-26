package com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.index.application.port.out.DrugIndexRepositoryPort;
import com.likelion.backendplus4.yakplus.index.application.port.out.EmbeddingPort;
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

/**
 * Elasticsearch를 통해 Drug 도메인 객체의 색인 기능을 제공하는 어댑터 클래스입니다.
 * DrugIndexRepositoryPort를 구현하여
 * Elasticsearch 원격 호출을 캡슐화합니다.
 *
 * @modified 2025-04-24
 * @since 2025-04-22
 */
@Component
public class ElasticsearchDrugAdapter implements DrugIndexRepositoryPort {
	private final RestClient restClient;
	private final ObjectMapper objectMapper;
	private final EmbeddingPort embeddingPort;

	public ElasticsearchDrugAdapter(RestClient restClient, ObjectMapper objectMapper, EmbeddingPort embeddingPort) {
		this.restClient = restClient;
		this.objectMapper = objectMapper;
		this.embeddingPort = embeddingPort;
	}

	/**
	 * 주어진 Drug 목록을 Elasticsearch에 일괄 저장한다.
	 *
	 * @param drugs 저장할 Drug 객체 리스트
	 * @author 정안식
	 * @modified 2025-04-24
	 * @since 2025-04-22
	 */
	@Override
	public void saveAll(String drugsIndex, List<Drug> drugs) {
		drugs.forEach(drug -> saveDrug(drugsIndex, drug));
	}

	/**
	 * 단일 Drug 객체를 Elasticsearch에 색인한다.
	 * 임베딩을 생성한 뒤, 문서 형태로 변환하여 색인 요청을 수행한다.
	 * 실패 시 SearchException을 발생시킨다.
	 *
	 * @param drug 저장할 Drug 도메인 객체
	 * @throws IndexException 색인 처리 중 오류 발생 시
	 * @author 정안식
	 * @modified 2025-04-24
	 * @since 2025-04-22
	 */
	private void saveDrug(String drugsIndex, Drug drug) {

		try {

			Map<String, Object> source = createDrugDocument(drug);
			String json = objectMapper.writeValueAsString(source);
			//TODO: 인덱스 추가 필요
			Request request = createIndexRequest("drugsIndex", drug.getDrugId(), json);
			restClient.performRequest(request);
		} catch (Exception e) {
			//TODO: LOG ERROR 처리 요망
			//            log(LogLevel.ERROR, "Ealsticsearch 저장 실패", e);
			throw new IndexException(IndexErrorCode.ES_SAVE_ERROR);
		}
	}

	/**
	 * Drug 객체와 임베딩 벡터를 기반으로 Elasticsearch 색인용 문서 필드 맵을 생성한다.
	 *
	 * @param drug   색인할 Drug 도메인 객체
	 * @param vector 해당 객체에 대한 임베딩 벡터
	 * @return Elasticsearch에 저장할 문서 필드 맵
	 * @author 정안식
	 * @modified 2025-04-24
	 * @since 2025-04-22
	 */
	private Map<String, Object> createDrugDocument(Drug drug) {
		return Map.of("itemSeq", drug.getDrugId().toString(), "itemName", drug.getDrugName(), "entpName",
			drug.getCompany(), "eeText", drug.getEfficacy(), "searchAll", drug.getEfficacy(), "eeVector", drug.getVector());
	}

	/**
	 * 지정된 인덱스와 문서 ID로 Elasticsearch 색인 요청 객체를 생성한다.
	 *
	 * @param itemSeq 문서 ID로 사용할 itemSeq 값
	 * @param json    색인할 JSON 문자열
	 * @return 색인 요청을 수행할 Request 객체
	 * @author 정안식
	 * @modified 2025-04-24
	 * @since 2025-04-22
	 */
	private Request createIndexRequest(String drugsIndex, Long itemSeq, String json) {
		//TODO: 인덱스 추가 필요
		Request request = new Request("POST", "/" + drugsIndex + "/_doc/" + itemSeq);
		request.setEntity(new NStringEntity(json, ContentType.APPLICATION_JSON));
		return request;
	}
}