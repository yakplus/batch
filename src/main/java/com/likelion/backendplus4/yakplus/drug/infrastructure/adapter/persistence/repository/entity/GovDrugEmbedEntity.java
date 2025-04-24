package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "GOV_EMBED_DATA")
public class GovDrugEmbedEntity {
	@Id
	@Column( name= "ITEM_SEQ")
	private Long drugId;

	@Column( name= "GPT_VECTOR")
	private Long gptVector;

	@Column( name= "KR_SBERT_VECTOR")
	private Long krSbertVector;

	@Column( name= "KM_BERT_VECTOR")
	private Long kmBertVector;
}
