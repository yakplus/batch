package com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.ApiDataDrugImgEntity;

@Repository
public interface ApiDataDrugImgRepo extends JpaRepository<ApiDataDrugImgEntity, Long> {
}
