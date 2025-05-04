package com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugKrSbertEmbedEntity;

import java.util.List;

@Repository
public interface GovDrugKrSbertEmbedJpaRepository extends JpaRepository<DrugKrSbertEmbedEntity,Long> {
    String QUERY = """
            SELECT r, e
            FROM DrugKrSbertEmbedEntity e
            JOIN DrugRawDataEntity  r
                ON r.drugId = e.drugId
            WHERE e.krSbertVector IS NOT NULL
                    AND r.isHerbal is FALSE
        """;
    @Query(QUERY)
    List<Object[]> findRawAndEmbed(Pageable pageable);
}
