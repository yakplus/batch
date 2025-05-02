package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.combine.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.api.ApiRequestManager;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.combine.dto.DrugMergeDto;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.common.util.MdcTaskDecorator;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.detail.reader.DetailPageNumberReader;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.mapper.ApiResponseMapper;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugDetailEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.TableCombineEntity;

import jakarta.persistence.EntityManagerFactory;

@Configuration
public class CombineStepConfig {
	private final DetailPageNumberReader detailPageNumberReader;
	private final ApiRequestManager apiRequestManager;
	private final ApiResponseMapper apiResponseMapper;
	private final TaskExecutor taskExecutor;
	private final DrugMergeJdbcReader reader;
	public CombineStepConfig(DetailPageNumberReader detailPageNumberReader,
							ApiRequestManager apiRequestManager,
							MdcTaskDecorator mdcTaskDecorator,
							ApiResponseMapper apiResponseMapper,
							@Qualifier("batchExecutor")
							TaskExecutor taskExecutor,
							DrugMergeJdbcReader reader) {
		this.detailPageNumberReader = detailPageNumberReader;
		this.apiRequestManager = apiRequestManager;
		this.apiResponseMapper = apiResponseMapper;
		this.taskExecutor = taskExecutor;
		this.reader = reader;
	}

	@Bean
	public Step tableCombineStep(
		JobRepository jobRepository,
		PlatformTransactionManager transactionManager,
		ItemReader<TableCombineEntity> reader,
		ItemProcessor<TableCombineEntity, DrugRawDataEntity> processor,
		ItemWriter<DrugRawDataEntity> writer) {
		return new StepBuilder("drugCombineStep", jobRepository)
			.<TableCombineEntity, DrugRawDataEntity>chunk(1000, transactionManager)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.build();
	}

	@Bean
	@StepScope
	public JpaPagingItemReader<TableCombineEntity> drugDetailReader(EntityManagerFactory entityManagerFactory) {
		JpaPagingItemReader<TableCombineEntity> reader = new JpaPagingItemReader<>();
		reader.setEntityManagerFactory(entityManagerFactory);
		reader.setQueryString("""
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
	""");
		reader.setPageSize(1000);
		reader.setSaveState(true);
		reader.setName("drugDetailReader");
		return reader;
	}
}
