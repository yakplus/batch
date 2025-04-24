package com.likelion.backendplus4.yakplus.drug.infrastructure.support.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/***
 * API 요청 URI 객체 생성 빌더
 *
 * application.yml에서 주입되는 속성 값으로,
 * API HOST, PATH를 확인해 URI 객체를 만듭니다.
 *
 * @since 2025-04-15
 * @author 함예정
 */
@Component
public class ApiUriCompBuilder {
    private final String SERVICE_KEY;
    private final String HOST;
    private final String API_DETAIL_PATH;
    private final String API_IMG_PATH ;
    private final int NUM_OF_ROWS;
    private final String API_KM_BERT;
    private final String API_KR_SBERT;

    public ApiUriCompBuilder(@Value("${gov.host}") String host,
                            @Value("${gov.serviceKey}") String serviceKey,
                            @Value("${gov.path.detail}") String pathDetail,
                            @Value("${gov.path.img}") String pathImg,
                            @Value("${gov.numOfRows}") int numOfRows,
                            @Value("${embed.kmbert") String API_KM_BERT,
                            @Value("${embed.krsbert") String API_KR_SBERT) {
        this.HOST = host;
        this.SERVICE_KEY = serviceKey;
        this.API_DETAIL_PATH = pathDetail;
        this.API_IMG_PATH = pathImg;
        this.NUM_OF_ROWS = numOfRows;
        this.API_KM_BERT = API_KM_BERT;
        this.API_KR_SBERT = API_KR_SBERT;
    }

    /***
     * 입력 받은 path를 반영해 URI 객체를 생성, 반환
     *
     * @param path API 요청 경로
     * @return URI
     *
     * @since 2025-04-15
     * @author 함예정
     */
    private URI getUri(String path, int pageNo) {
        return UriComponentsBuilder.newInstance()
            .scheme("https")
            .host(HOST)
            .port(443)
            .path(path)
            .queryParam("serviceKey", SERVICE_KEY)
            .queryParam("type", "json")
            .queryParam("pageNo", pageNo)
            .queryParam("numOfRows", NUM_OF_ROWS)
            .build(true)
            .toUri();
    }

    /***
     * 식품의약품안전처 의약품 제품 허가 상세 정보 URI 반환
     * @return URI 제품 허가 상세 정보
     * 
     * @since 2025-04-15
     * @author 함예정
     */
    public URI getUriForDetailApi(int pageNo) {
        return getUri(API_DETAIL_PATH, pageNo);
    }

    /***
     * 식품의약품안전처 의약품 제품 허가 목록 URI 반환
     * @return URI 제품 허가 목록
     *
     * @since 2025-04-15
     * @author 함예정
     */
    public URI getUriForImgApi(int pageNo) {
        return getUri(API_IMG_PATH, pageNo);
    }

    public URI getUriForDetailApiShort() {
        return UriComponentsBuilder.newInstance()
            .scheme("https")
            .host(HOST)
            .port(443)
            .path(API_DETAIL_PATH)
            .queryParam("serviceKey", SERVICE_KEY)
            .queryParam("type", "json")
            .queryParam("pageNo", 1)
            .queryParam("numOfRows", 1)
            .build(true)
            .toUri();
    }

    public URI getUriForImgApiShort() {
        return UriComponentsBuilder.newInstance()
            .scheme("https")
            .host(HOST)
            .port(443)
            .path(API_IMG_PATH)
            .queryParam("serviceKey", SERVICE_KEY)
            .queryParam("type", "json")
            .queryParam("pageNo", 1)
            .queryParam("numOfRows", 1)
            .build(true)
            .toUri();
    }

    public URI getUriForKmbertEmbeding() {
        return UriComponentsBuilder.newInstance()
            .scheme("https")
            .host(HOST)
            .port(443)
            .path(API_KM_BERT)
            .build(true)
            .toUri();
    }

    public URI getUriForKrSbertEmbeding() {
        return UriComponentsBuilder.newInstance()
            .scheme("https")
            .host(HOST)
            .port(443)
            .path(API_KR_SBERT)
            .build(true)
            .toUri();
    }
}
