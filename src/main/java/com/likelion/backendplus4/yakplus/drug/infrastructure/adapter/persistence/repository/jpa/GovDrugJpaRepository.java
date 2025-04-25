package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.GovDrugEntity;

import jakarta.transaction.Transactional;

public interface GovDrugJpaRepository extends JpaRepository<GovDrugEntity, Long> {
	@Modifying
	@Transactional
	@Query(value = """
         INSERT INTO gov_drug_raw_data
            (
                etc_otc_code,
                item_permit_date,
                item_seq,
                ee_doc_data,
                entp_name,
                item_name,
                material_name,
                nb_doc_data,
                storage_method,
                ud_doc_data,
                valid_term,
                img_url,
                gpt_vector,
                kr_sbert_vector,
                km_bert_vector
            )
            SELECT
                d.etc_otc_code,
                d.item_permit_date,
                d.item_seq,
                d.ee_doc_data,
                d.entp_name,
                d.item_name,
                d.material_name,
                d.nb_doc_data,
                d.storage_method,
                d.ud_doc_data,
                d.valid_term,
                i.img_url,
                e.gpt_vector,
                e.kr_sbert_vector,
                e.km_bert_vector
            FROM
                gov_drug_detail AS d
            LEFT JOIN
                api_data_drug_img AS i ON d.item_seq = i.seq
            LEFT JOIN
                gov_embed_data AS e ON d.item_seq = e.item_seq
			 ON DUPLICATE KEY UPDATE
					 etc_otc_code     = VALUES(etc_otc_code),
					 item_permit_date = VALUES(item_permit_date),
					 ee_doc_data      = VALUES(ee_doc_data),
					 entp_name        = VALUES(entp_name),
					 item_name        = VALUES(item_name),
					 material_name    = VALUES(material_name),
					 nb_doc_data      = VALUES(nb_doc_data),
					 storage_method   = VALUES(storage_method),
					 ud_doc_data      = VALUES(ud_doc_data),
					 valid_term       = VALUES(valid_term),
					 img_url          = VALUES(img_url),
					 gpt_vector       = VALUES(gpt_vector),
					 kr_sbert_vector  = VALUES(kr_sbert_vector),
					 km_bert_vector   = VALUES(km_bert_vector)
        """, nativeQuery = true)
	void createRawDataByApiTable();
}
