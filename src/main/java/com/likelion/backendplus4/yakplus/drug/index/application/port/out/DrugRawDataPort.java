package com.likelion.backendplus4.yakplus.drug.index.application.port.out;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DrugRawDataPort {
    List<Drug> fetchRawData(int pageNo, int numOfRows);


    /**
     * 주어진 Pageable 정보에 따라 DB에서 한 페이지 분량의 GovDrugEntity를 조회하고,
     * 각 엔티티를 도메인 모델(GovDrug)로 변환하여 Page 형태로 반환합니다.
     *
     * @param pageable 조회할 페이지 번호와 크기를 포함하는 Pageable 객체
     * @return 페이지 단위로 변환된 GovDrug 도메인 객체의 Page
     * @author 박찬병
     * @since 2025-04-24
     * @modified 2025-04-25
     */
    Page<Drug> findAllDrugs(Pageable pageable);

    /**
     * JPA 레포지토리를 이용해 GovDrugJpaRepository의 전체 데이터 수를 조회합니다.
     *
     * @return GovDrugJpaRepository의 전체 데이터 수
     * @author 이해창
     * @since 2025-05-02
     * @modified
     */
    long getDrugTotalSize();
}