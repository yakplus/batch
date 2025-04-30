package com.likelion.backendplus4.yakplus.drug.application.service.scraper.combiner;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;
import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugCombineUsecase;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugDetailRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugImageRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugRawDataRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.domain.model.DrugImage;
import com.likelion.backendplus4.yakplus.drug.domain.model.DrugRawData;
import com.likelion.backendplus4.yakplus.drug.domain.model.DrugDetail;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * 의약품 상세 정보와 이미지 정보를 결합하여 원시 데이터를 생성하는 컴포넌트입니다.
 * {@link DrugCombineUsecase}를 구현하며, 데이터 병합 및 저장 기능을 제공합니다.
 *
 * @since 2025-04-21
 */
@Component
@RequiredArgsConstructor
public class DrugCombiner implements DrugCombineUsecase {
	private final DrugDetailRepositoryPort drugDetailRepository;
	private final DrugImageRepositoryPort drugImageRepositoryPort;
	private final DrugRawDataRepositoryPort	drugRawDataRepositoryPort;

	public void mergeTable() {
		log("API 요청 결과 테이블 병합 시작: 상세 정보 + 이미지");

		int pageSize = 1_000;
		Page<DrugDetail> firstPage = drugDetailRepository.getGovDrugDetailByPage(PageRequest.of(0, pageSize));
		int totalPages = firstPage.getTotalPages();

		processDrugDetails(firstPage.getContent(), 1);

		for (int i = 1; i < totalPages; i++) {
			Page<DrugDetail> page = drugDetailRepository.getGovDrugDetailByPage(PageRequest.of(i, pageSize));
			processDrugDetails(page.getContent(), i + 1);
		}

		log("API 요청 결과 테이블 병합 완료");
	}

	private void processDrugDetails(List<DrugDetail> drugDetails, int pageNumber) {
		log("Processing Page = " + pageNumber);
		log(LogLevel.DEBUG, "DrugDetail Raw Data (Page " + pageNumber + "): \n" + drugDetails);

		List<DrugRawData> rawData = drugDetails.stream()
			.map(detail -> buildMergeRawData(detail, getImageDataByDrugDetail(detail))).toList();
		drugRawDataRepositoryPort.saveAll(rawData);
	}

	/**
	 * 주어진 의약품 상세 정보에 해당하는 이미지 정보를 조회합니다.
	 *
	 * @param detail 의약품 상세 정보
	 * @return 해당 의약품의 이미지 정보
	 *
	 * @since 2025-04-21
	 */
	private DrugImage getImageDataByDrugDetail(DrugDetail detail) {
		log(LogLevel.DEBUG, "의약품 이미지 정보 요청 \n detail: " + detail);
		DrugImage imageData = drugImageRepositoryPort.getById(detail.getDrugId());
		log(LogLevel.DEBUG, "의약품 이미지 정보 (상세 정보로 Search): " + imageData);
		return imageData;
	}

	/**
	 * 의약품 상세 정보와 이미지 정보를 병합하여 {@link DrugRawData} 객체를 생성합니다.
	 *
	 * @param d 의약품 상세 정보
	 * @param i 의약품 이미지 정보
	 * @return 병합된 의약품 원시 데이터
	 *
	 * @since 2025-04-21
	 */
	private DrugRawData buildMergeRawData(DrugDetail d, DrugImage i) {
		DrugRawData rawData = DrugRawData.builder()
			.drugId(d.getDrugId())
			.drugName(d.getDrugName())
			.company(d.getCompany())
			.permitDate(d.getPermitDate())
			.isGeneral(d.isGeneral())
			.materialInfo(d.getMaterialInfo())
			.storeMethod(d.getStoreMethod())
			.validTerm(d.getValidTerm())
			.efficacy(d.getEfficacy())
			.usage(d.getUsage())
			.precaution(d.getPrecaution())
			.imageUrl(i.getImageUrl())
			.cancelDate(d.getCancelDate())
			.cancelName(d.getCancelName())
			.isHerbal(d.isHerbal())
			.build();
		return rawData;
	}
}
