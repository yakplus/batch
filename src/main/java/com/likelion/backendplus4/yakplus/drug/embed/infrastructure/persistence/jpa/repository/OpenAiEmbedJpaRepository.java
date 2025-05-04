package com.likelion.backendplus4.yakplus.drug.embed.infrastructure.persistence.jpa.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.likelion.backendplus4.yakplus.drug.embed.infrastructure.persistence.entity.DrugGptEmbedEntity;

import java.util.List;

@Repository
public interface OpenAiEmbedJpaRepository extends JpaRepository<DrugGptEmbedEntity,Long> {
    String QUERY = """
            SELECT r, e
            FROM DrugGptEmbedEntity e
            JOIN DrugRawDataEntity  r
                ON r.drugId = e.drugId
            WHERE e.gptVector IS NOT NULL
                    AND r.isHerbal is FALSE
        """;
    @Query(QUERY)
    List<Object[]> findRawAndEmbed(Pageable pageable);
}
