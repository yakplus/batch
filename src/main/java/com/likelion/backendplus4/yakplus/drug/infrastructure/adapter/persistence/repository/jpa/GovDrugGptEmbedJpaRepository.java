package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.DrugGptEmbedEntity;

@Repository
public interface GovDrugGptEmbedJpaRepository extends JpaRepository<DrugGptEmbedEntity,Long> {
}
