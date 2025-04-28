package com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugKmBertEmbedEntity;

@Repository
public interface GovDrugKmBertEmbedJpaRepository extends JpaRepository<DrugKmBertEmbedEntity,Long> {
}
