package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

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
@Table(name="API_DATA_DRUG_IMG")
public class ApiDataDrugImgEntity {
	@Id
	private Long drugId;

	private String imgUrl;


}
