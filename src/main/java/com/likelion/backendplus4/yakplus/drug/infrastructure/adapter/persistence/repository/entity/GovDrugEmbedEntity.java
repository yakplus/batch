package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "GOV_EMBED_DATA")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GovDrugEmbedEntity {
	@Id
	@Column( name= "ITEM_SEQ")
	private Long drugId;

	@Column( name= "GPT_VECTOR", columnDefinition = "JSON")
	private String gptVector;

	@Column( name= "KR_SBERT_VECTOR", columnDefinition = "JSON")
	private String krSbertVector;

	@Column( name= "KM_BERT_VECTOR", columnDefinition = "JSON")
	private String kmBertVector;
}
