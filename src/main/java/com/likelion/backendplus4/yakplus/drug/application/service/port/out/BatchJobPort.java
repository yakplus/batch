package com.likelion.backendplus4.yakplus.drug.application.service.port.out;

public interface BatchJobPort {
	String allJobStart();

	String allJobStop();

	String allJobResume();

	String allJobStatus();

	String detailScrapJobStart();

	String detailScrapJobStop();

	String detailScrapJobStatus();

	String imageScrapJobStart();

	String imageScrapJobStop();

	String imageScrapJobStatus();

	String tableCombineJobStart();

	String tableCombineJobStop();

	String tableCombineJobStatus();
}
