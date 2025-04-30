package com.likelion.backendplus4.yakplus.temp.infrastructure.adapter.persistence;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence.repository.DrugSymptomRepository;
import com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence.repository.document.DrugSymptomDocument;
import com.likelion.backendplus4.yakplus.index.support.mapper.SymptomMapper;
import com.likelion.backendplus4.yakplus.temp.application.port.out.TempDrugIndexRepositoryPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TempElasticsearchDrugAdapter implements TempDrugIndexRepositoryPort {

    private final DrugSymptomRepository symptomRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAllSymptom(Page<Drug> drugs) {
        //  도메인 → ES Document 변환
        log("saveAllSymptom() 요청 수신");
        List<DrugSymptomDocument> docs = drugs.stream()
                .map(SymptomMapper::toDocument)  // 내부에서 예외 처리 됨
                .toList();
        log("  문서 변환 완료: count=" + docs.size());

        symptomRepository.saveAll(docs);
        log("  ES 색인 완료: count=" + docs.size());
    }

}
