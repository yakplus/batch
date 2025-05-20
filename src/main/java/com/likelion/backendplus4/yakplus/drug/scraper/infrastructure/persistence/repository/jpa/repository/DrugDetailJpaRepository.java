package com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.persistence.repository.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.persistence.repository.entity.DrugDetailEntity;

@Repository
public interface DrugDetailJpaRepository extends JpaRepository<DrugDetailEntity,Long> {
}