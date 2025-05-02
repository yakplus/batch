package com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "API_DATA_DRUG_IMG")
public class ApiDataDrugImgEntity {
	@Id
	@Column(name = "ITEM_SEQ")
	private Long drugId;

	@Column(name = "PRODUCT_IMAGE", columnDefinition = "LONGTEXT")
	private String productImage;

	@Column(name = "PILL_IMAGE", columnDefinition = "LONGTEXT")
	private String pillImage;
}
