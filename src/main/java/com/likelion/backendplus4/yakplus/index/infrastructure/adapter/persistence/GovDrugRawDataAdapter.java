package com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.drug.application.service.exception.ScraperException;
import com.likelion.backendplus4.yakplus.drug.application.service.exception.error.ScraperErrorCode;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper.DrugRawDataMapper;
import com.likelion.backendplus4.yakplus.index.application.port.out.EmbeddingLoadingPort;
import com.likelion.backendplus4.yakplus.index.application.port.out.GovDrugRawDataPort;
import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.index.support.parser.JsonArrayTextParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * 공공 API로부터 조회한 원시 약품 데이터를 JPA를 통해 가져와
 * 도메인 객체인 Drug로 변환하는 어댑터 클래스입니다.
 *
 * @since 2025-04-22
 * @modified 2025-04-24
 */
@Component
@RequiredArgsConstructor
public class GovDrugRawDataAdapter implements GovDrugRawDataPort {
    private final GovDrugJpaRepository drugJpaRepository;
    private final EmbeddingLoadingPort embeddingLoadingPort;

    /**
     * 주어진 Pageable 정보에 따라 DB에서 한 페이지 분량의 GovDrugEntity를 조회하고,
     * 각 엔티티를 도메인 모델(GovDrug)로 변환하여 Page 형태로 반환합니다.
     *
     * @param pageable 조회할 페이지 번호와 크기를 포함하는 Pageable 객체
     * @return 페이지 단위로 변환된 GovDrug 도메인 객체의 Page
     * @author 박찬병
     * @since 2025-04-24
     * @modified 
     * 2025-05-04 - 박찬병: Mapper에 넘겨주기 전 먼저 파싱하도록 변경
     */
    @Override
    public Page<Drug> findAllDrugs(Pageable pageable) {
        log("findAllDrugs() 요청 수신");

        return drugJpaRepository.findByIsGeneralIsTrue(pageable)
                .map(entity -> {
                    try {
                        List<String> parsedEfficacy = JsonArrayTextParser.extractAndClean(entity.getEfficacy());
                        return DrugRawDataMapper.toDomainFromEntity(entity, parsedEfficacy);
                    } catch (IOException ex) {
                        log(LogLevel.ERROR, "효능 정보 파싱 실패 - drugId: " + entity.getDrugId(), ex);
                        throw new ScraperException(ScraperErrorCode.PARSING_ERROR);
                    }
                });
    }

    /**
     * 지정된 페이지 번호와 페이지 크기에 따라 Pageable 객체를 생성하고,
     * 원시 약품 데이터와 임베딩 데이터를 조인하여 한 페이지 분량의 Drug 도메인 객체 리스트를 조회합니다.
     *
     * @param pageNo    조회할 페이지 번호 (0부터 시작)
     * @param numOfRows 한 페이지에 포함할 데이터 개수
     * @return 페이지 범위에 해당하는 Drug 도메인 객체들의 리스트
     * @author 정안식
     * @since 2025-04-24
     * @modified
     * 2025-05-02 - 이해창: numOfRows 파라미터 추가
     */
    @Override
    public List<Drug> fetchRawData(int pageNo, int numOfRows) {
        log("index 서비스 요청 수신");
        Pageable pageable = createPageable(pageNo, numOfRows);
        return embeddingLoadingPort.loadEmbeddingsByPage(pageable);
    }

    /**
     * 페이지 번호와 페이지 크기를 기반으로 drugId 오름차순 정렬이 적용된 Pageable 객체를 생성합니다.
     *
     * @param pageNo    조회할 페이지 번호 (0부터 시작)
     * @param numOfRows 한 페이지에 포함할 데이터 개수
     * @return 지정된 페이지 정보와 정렬 조건을 포함한 Pageable 객체
     * @author 정안식
     * @since 2025-04-24
     * @modified
     * 2025-04-24
     */
    private Pageable createPageable(int pageNo, int numOfRows) {
        log("pageable 생성");
        return PageRequest.of(pageNo, numOfRows, Sort.by(Sort.Direction.ASC, "drugId"));
    }

    /**
     * JPA 레포지토리를 이용해 GovDrugJpaRepository의 전체 데이터 수를 조회합니다.
     *
     * @return GovDrugJpaRepository의 전체 데이터 수
     * @author 이해창
     * @since 2025-05-02
     * @modified
     * 2025-05-02 - 이해창: numOfRows 파라미터 추가
     */
    @Override
    public long getDrugTotalSize() {
        return drugJpaRepository.count();
    }
}