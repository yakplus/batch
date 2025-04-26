package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.GovDrugEntity;

import jakarta.transaction.Transactional;

public interface GovDrugJpaRepository extends JpaRepository<GovDrugEntity, Long> {
}
