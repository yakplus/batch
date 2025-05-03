package com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa;

import aj.org.objectweb.asm.commons.Remapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;

public interface GovDrugJpaRepository extends JpaRepository<DrugRawDataEntity, Long> {

    Page<DrugRawDataEntity> findByIsGeneralIsTrue(Pageable pageable);

	@Query("SELECT MIN(d.drugId) FROM DrugRawDataEntity d")
	Long findMinDrugId();

	@Query("SELECT MAX(d.drugId) FROM DrugRawDataEntity d")
	Long findMaxDrugId();
}
