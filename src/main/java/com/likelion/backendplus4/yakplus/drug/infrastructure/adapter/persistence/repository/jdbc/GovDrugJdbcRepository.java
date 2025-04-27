package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jdbc;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.DrugDetailEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.DrugRawDataEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GovDrugJdbcRepository {
	private final JdbcTemplate jdbc;

	@Transactional
	public void saveAll(List<DrugDetailEntity> entities) {
		String sql = ""
			+ "INSERT INTO gov_drug_detail "
			+ "  (ITEM_SEQ, ITEM_NAME, ENTP_NAME, "
			+ "   ITEM_PERMIT_DATE, ETC_OTC_CODE, MATERIAL_NAME, "
			+ "   STORAGE_METHOD, VALID_TERM, EE_DOC_DATA, "
			+ "   UD_DOC_DATA, NB_DOC_DATA) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
			+ "ON DUPLICATE KEY UPDATE "
			+ "  ITEM_NAME         = VALUES(ITEM_NAME), "
			+ "  ENTP_NAME         = VALUES(ENTP_NAME), "
			+ "  ITEM_PERMIT_DATE  = VALUES(ITEM_PERMIT_DATE), "
			+ "  ETC_OTC_CODE      = VALUES(ETC_OTC_CODE), "
			+ "  MATERIAL_NAME     = VALUES(MATERIAL_NAME), "
			+ "  STORAGE_METHOD    = VALUES(STORAGE_METHOD), "
			+ "  VALID_TERM        = VALUES(VALID_TERM), "
			+ "  EE_DOC_DATA       = VALUES(EE_DOC_DATA), "
			+ "  UD_DOC_DATA       = VALUES(UD_DOC_DATA), "
			+ "  NB_DOC_DATA       = VALUES(NB_DOC_DATA)";
		jdbc.batchUpdate(sql, new JdbcBatchSetter(entities));
	}

	@Transactional
	public void mergeAll(List<DrugRawDataEntity> entities) {
		String sql = ""
			+ "INSERT INTO gov_drug_raw_data "
			+ "  (ITEM_SEQ, ITEM_NAME, ENTP_NAME, "
			+ "   ITEM_PERMIT_DATE, ETC_OTC_CODE, MATERIAL_NAME, "
			+ "   STORAGE_METHOD, VALID_TERM, EE_DOC_DATA, "
			+ "   UD_DOC_DATA, NB_DOC_DATA, IMG_URL, "
			+ "   GPT_VECTOR, KR_SBERT_VECTOR, KM_BERT_VECTOR) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
			+ "ON DUPLICATE KEY UPDATE "
			+ "  ITEM_NAME         = VALUES(ITEM_NAME), "
			+ "  ENTP_NAME         = VALUES(ENTP_NAME), "
			+ "  ITEM_PERMIT_DATE  = VALUES(ITEM_PERMIT_DATE), "
			+ "  ETC_OTC_CODE      = VALUES(ETC_OTC_CODE), "
			+ "  MATERIAL_NAME     = VALUES(MATERIAL_NAME), "
			+ "  STORAGE_METHOD    = VALUES(STORAGE_METHOD), "
			+ "  VALID_TERM        = VALUES(VALID_TERM), "
			+ "  EE_DOC_DATA       = VALUES(EE_DOC_DATA), "
			+ "  UD_DOC_DATA       = VALUES(UD_DOC_DATA), "
			+ "  NB_DOC_DATA       = VALUES(NB_DOC_DATA), "
			+ "  IMG_URL       = VALUES(IMG_URL), "
			+ "  GPT_VECTOR       = VALUES(GPT_VECTOR), "
			+ "  KR_SBERT_VECTOR       = VALUES(KR_SBERT_VECTOR), "
			+ "  KM_BERT_VECTOR       = VALUES(KM_BERT_VECTOR) ";
		jdbc.batchUpdate(sql, new MergeBatchSetter(entities));
	}
}
