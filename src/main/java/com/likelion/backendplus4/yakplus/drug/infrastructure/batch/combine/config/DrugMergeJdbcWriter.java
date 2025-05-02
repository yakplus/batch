package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.combine.config;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.common.util.log.LogUtil;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.combine.dto.DrugMergeDto;

import lombok.RequiredArgsConstructor;

@Component
@StepScope
@RequiredArgsConstructor
public class DrugMergeJdbcWriter implements ItemWriter<DrugMergeDto> {

	private final JdbcTemplate jdbcTemplate;

	@Override
	public void write(Chunk<? extends DrugMergeDto> chunk) {
		List<? extends DrugMergeDto> items = chunk.getItems();
		String sql = """
            INSERT INTO gov_drug_raw_data (
                item_seq, etc_otc_code, item_permit_date, ee_doc_data, entp_name, img_url,
                material_name, item_name, nb_doc_data, storage_method, ud_doc_data,
                valid_term, cancel_date, cancel_name, is_herbal
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                etc_otc_code = VALUES(etc_otc_code),
                item_permit_date = VALUES(item_permit_date),
                ee_doc_data = VALUES(ee_doc_data),
                entp_name = VALUES(entp_name),
                img_url = VALUES(img_url),
                material_name = VALUES(material_name),
                item_name = VALUES(item_name),
                nb_doc_data = VALUES(nb_doc_data),
                storage_method = VALUES(storage_method),
                ud_doc_data = VALUES(ud_doc_data),
                valid_term = VALUES(valid_term),
                cancel_date = VALUES(cancel_date),
                cancel_name = VALUES(cancel_name),
                is_herbal = VALUES(is_herbal)
        """;

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				DrugMergeDto dto = items.get(i);
				ps.setLong(1, dto.getItemSeq());
				ps.setObject(2, dto.getEtcOtcCode());
				ps.setString(3, dto.getItemPermitDate());
				ps.setString(4, dto.getEeDocData());
				ps.setString(5, dto.getEntpName());
				ps.setString(6, dto.getImgUrl());
				ps.setString(7, dto.getMaterialName());
				ps.setString(8, dto.getItemName());
				ps.setString(9, dto.getNbDocData());
				ps.setString(10, dto.getStorageMethod());
				ps.setString(11, dto.getUdDocData());
				ps.setString(12, dto.getValidTerm());
				ps.setString(13, dto.getCancelDate());
				ps.setString(14, dto.getCancelName());
				ps.setObject(15, dto.getIsHerbal());
			}

			@Override
			public int getBatchSize() {
				return items.size();
			}
		});
	}
}
