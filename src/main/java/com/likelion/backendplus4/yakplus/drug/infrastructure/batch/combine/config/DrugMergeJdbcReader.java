package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.combine.config;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.combine.dto.DrugMergeDto;

@Component
@StepScope
public class DrugMergeJdbcReader extends JdbcPagingItemReader<DrugMergeDto> {

	public DrugMergeJdbcReader(DataSource dataSource) {
		super();
		setDataSource(dataSource);
		setPageSize(500);
		setRowMapper(new BeanPropertyRowMapper<>(DrugMergeDto.class));
		setQueryProvider(buildQueryProvider());
	}

	private MySqlPagingQueryProvider buildQueryProvider() {
		MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
		provider.setSelectClause("""
			    d.item_seq, d.etc_otc_code, d.item_permit_date, d.ee_doc_data,
			    d.entp_name,
			    CASE
			        WHEN i.product_image IS NOT NULL AND i.product_image != ''
			            THEN i.product_image
			        ELSE i.pill_image
			    END AS img_url,
			    d.material_name, d.item_name, d.nb_doc_data, d.storage_method,
			    d.ud_doc_data, d.valid_term, d.cancel_date, d.cancel_name, d.is_herbal
			""");
		provider.setFromClause("FROM gov_drug_detail d LEFT JOIN api_data_drug_img i ON d.item_seq = i.drug_id");
		provider.setSortKeys(Map.of("item_seq", Order.ASCENDING));
		return provider;
	}
}
