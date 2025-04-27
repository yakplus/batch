package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.DrugRawDataEntity;

public interface GovDrugJpaRepository extends JpaRepository<DrugRawDataEntity, Long> {
}
