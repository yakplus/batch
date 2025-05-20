package com.likelion.backendplus4.yakplus.drug.embed.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DRUG_EMBED_KR-SBERT")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DrugKrSBertEmbedEntity implements EmbeddingEntity{
	@Id
	@Column( name= "ITEM_SEQ")
	private Long drugId;

	@Column( name= "KR_SBERT_VECTOR", columnDefinition = "JSON")
	private String krSBertVector;

	@Override
	public String getVector() {
		return krSBertVector;
	}
}
