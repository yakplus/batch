package com.likelion.backendplus4.yakplus.temp.infrastructure.adapter.persistence;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugJpaRepository;
import com.likelion.backendplus4.yakplus.temp.application.port.out.TempRawDataPort;
import com.likelion.backendplus4.yakplus.temp.support.mapper.TempDrugRawDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TempGovDrugRawDataAdapter implements TempRawDataPort {

    private final GovDrugJpaRepository drugJpaRepository;

    public Page<Drug> findAllDrugs(Pageable pageable) {
        log("findAllDrugs() 요청 수신");
        return drugJpaRepository.findByIsGeneralIsTrue(pageable)
                .map(TempDrugRawDataMapper::toDomainFromEntity);
    }

}
