package com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugDetailEntity;

import jakarta.transaction.Transactional;

@Repository
public interface GovDrugDetailJpaRepository extends JpaRepository<DrugDetailEntity,Long> {

	@Override
	@Transactional
	<S extends DrugDetailEntity> List<S> saveAllAndFlush(Iterable<S> entities);

	List<DrugDetailEntity> findByDrugIdGreaterThanOrderByDrugIdAsc(Long drugIdIsGreaterThan, Pageable pageable);
}