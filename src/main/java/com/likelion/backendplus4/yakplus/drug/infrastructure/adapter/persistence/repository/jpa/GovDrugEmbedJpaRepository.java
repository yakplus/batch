package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.GovDrugEmbedEntity;

@Repository
public interface GovDrugEmbedJpaRepository  extends JpaRepository<GovDrugEmbedEntity,Long> {
}
