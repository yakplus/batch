package com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.persistence.repository.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.persistence.repository.entity.DrugRawDataEntity;

public interface DrugJpaRepository extends JpaRepository<DrugRawDataEntity, Long> {

	@Query("""
           SELECT d
           FROM DrugRawDataEntity d
           WHERE d.isGeneral = true
           AND d.isHerbal = false
           """)
    Page<DrugRawDataEntity> findByIsGeneral(Pageable pageable);

	@Query("SELECT MIN(d.drugId) FROM DrugRawDataEntity d")
	Long findMinDrugId();

	@Query("SELECT MAX(d.drugId) FROM DrugRawDataEntity d")
	Long findMaxDrugId();
}
