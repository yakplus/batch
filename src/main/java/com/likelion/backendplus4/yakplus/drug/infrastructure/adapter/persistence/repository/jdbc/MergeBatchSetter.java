package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.DrugRawDataEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MergeBatchSetter implements BatchPreparedStatementSetter {

	private final List<DrugRawDataEntity> entities;

	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		DrugRawDataEntity e = entities.get(i);
		ps.setLong   (1, e.getDrugId());
		ps.setString (2, e.getDrugName());
		ps.setString (3, e.getCompany());

		LocalDate permit = e.getPermitDate();
		if (permit != null) {
			ps.setDate(4, Date.valueOf(permit));
		} else {
			ps.setNull(4, Types.DATE);
		}

		ps.setBoolean(5, e.isGeneral());
		ps.setString (6, e.getMaterialInfo());
		ps.setString (7, e.getStoreMethod());
		ps.setString (8, e.getValidTerm());
		ps.setString (9, e.getEfficacy());
		ps.setString (10, e.getUsage());
		ps.setString (11, e.getPrecaution());
		ps.setString(12, e.getImageUrl());
	}

	@Override
	public int getBatchSize() {
		return entities.size();
	}
}
