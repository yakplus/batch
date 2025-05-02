package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.combine.config;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.combine.dto.TableCombineDto;

import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;

@Configuration
public class CombineStepConfig {
	@Getter
	private final String STEP_NAME = "drugCombineStep";
	private final String READER_NAME = "drugDetailReader";
	private final int PAGE_SIZE = 1000;

	@Bean
	public Step tableCombineStep(
		JobRepository jobRepository,
		PlatformTransactionManager transactionManager,
		ItemReader<TableCombineDto> reader,
		ItemProcessor<TableCombineDto, DrugRawDataEntity> processor,
		ItemWriter<DrugRawDataEntity> writer) {
		return new StepBuilder(STEP_NAME, jobRepository)
			.<TableCombineDto, DrugRawDataEntity>chunk(PAGE_SIZE, transactionManager)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.faultTolerant()
			.retry(Exception.class)
			.retryLimit(3)
			.skip(Exception.class)
			.skipLimit(Integer.MAX_VALUE)
			.build();
	}

	@Bean
	@StepScope
	public JpaPagingItemReader<TableCombineDto> drugDetailReader(EntityManagerFactory entityManagerFactory) {
		JpaPagingItemReader<TableCombineDto> reader = new JpaPagingItemReader<>();
		reader.setEntityManagerFactory(entityManagerFactory);
		reader.setQueryString(getJoinTableSql());
		reader.setPageSize(PAGE_SIZE);
		reader.setSaveState(true);
		reader.setName(READER_NAME);
		return reader;
	}


	private String getJoinTableSql(){
		return """
		SELECT new com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.TableCombineEntity(
			d.drugId,
			d.drugName,
			d.company,
			d.permitDate,
			d.isGeneral,
			d.materialInfo,
			d.storeMethod,
			d.validTerm,
			d.efficacy,
			d.usage,
			d.precaution,
			d.cancelDate,
			d.cancelName,
			d.isHerbal,
			i.productImage,
			i.pillImage
		)
		FROM DrugDetailEntity d
		LEFT JOIN ApiDataDrugImgEntity i ON d.drugId = i.drugId
	""";
	}
}
