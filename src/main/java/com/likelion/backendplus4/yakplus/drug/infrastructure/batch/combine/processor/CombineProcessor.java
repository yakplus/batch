package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.combine.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CombineProcessor implements Tasklet {

	private final JdbcTemplate jdbcTemplate;

	private static final int CHUNK_SIZE = 1000;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		int totalCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM gov_drug_detail", Integer.class);
		int offset = 0;

		while (offset < totalCount) {
			String sql = """
                INSERT INTO gov_drug_raw_data (
                    item_seq, etc_otc_code, item_permit_date, ee_doc_data, entp_name, img_url,
                    material_name, item_name, nb_doc_data, storage_method, ud_doc_data,
                    valid_term, cancel_date, cancel_name, is_herbal
                )
                SELECT
                    d.item_seq,
                    d.etc_otc_code,
                    d.item_permit_date,
                    d.ee_doc_data,
                    d.entp_name,
                    CASE
                        WHEN i.product_image IS NOT NULL AND i.product_image != ''
                            THEN i.product_image
                        ELSE i.pill_image
                    END AS img_url,
                    d.material_name,
                    d.item_name,
                    d.nb_doc_data,
                    d.storage_method,
                    d.ud_doc_data,
                    d.valid_term,
                    d.cancel_date,
                    d.cancel_name,
                    d.is_herbal
                FROM gov_drug_detail d
                LEFT JOIN api_data_drug_img i ON d.item_seq = i.drug_id
                ORDER BY d.item_seq
                LIMIT %d OFFSET %d
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
                """.formatted(CHUNK_SIZE, offset);

			jdbcTemplate.update(sql);
			offset += CHUNK_SIZE;
		}

		return RepeatStatus.FINISHED;
	}
}
