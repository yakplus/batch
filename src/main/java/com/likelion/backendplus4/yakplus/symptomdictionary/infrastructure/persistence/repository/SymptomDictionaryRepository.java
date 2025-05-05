package com.likelion.backendplus4.yakplus.symptomdictionary.infrastructure.persistence.repository;

import com.likelion.backendplus4.yakplus.symptomdictionary.infrastructure.persistence.repository.entity.SymptomDictionary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymptomDictionaryRepository extends JpaRepository<SymptomDictionary, Long> {
}
