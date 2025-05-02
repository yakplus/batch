package com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugGptEmbedEntity;

import java.util.List;

@Repository
public interface GovDrugGptEmbedJpaRepository extends JpaRepository<DrugGptEmbedEntity,Long> {
    @Query(
            value = """
            SELECT r, e
            FROM DrugGptEmbedEntity e
            JOIN DrugRawDataEntity  r
                ON r.drugId = e.drugId
            WHERE e.gptVector IS NOT NULL
        """
    )
    List<Object[]> findRawAndEmbed(Pageable pageable);
}
