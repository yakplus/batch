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
public class DrugGptEmbedEntity {
	@Id
	@Column( name= "ITEM_SEQ")
	private Long drugId;

	@Column( name= "GPT_VECTOR", columnDefinition = "JSON")
	private String gptVector;
}
