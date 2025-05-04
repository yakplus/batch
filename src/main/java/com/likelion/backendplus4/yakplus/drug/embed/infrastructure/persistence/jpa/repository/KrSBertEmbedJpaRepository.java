package com.likelion.backendplus4.yakplus.drug.embed.infrastructure.persistence.jpa.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.likelion.backendplus4.yakplus.drug.embed.infrastructure.persistence.entity.DrugKrSBertEmbedEntity;

@Repository
public interface KrSBertEmbedJpaRepository extends JpaRepository<DrugKrSBertEmbedEntity, Long> {
	String QUERY = """
		    SELECT r, e
		    FROM DrugKrSBertEmbedEntity e
		    JOIN DrugRawDataEntity  r
		        ON r.drugId = e.drugId
		    WHERE e.krSBertVector IS NOT NULL
		            AND r.isHerbal is FALSE
		""";

	@Query(QUERY)
	List<Object[]> findRawAndEmbed(Pageable pageable);
}
