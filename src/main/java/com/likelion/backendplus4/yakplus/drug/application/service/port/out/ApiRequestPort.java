package com.likelion.backendplus4.yakplus.drug.application.service.port.out;

import com.fasterxml.jackson.databind.JsonNode;

public interface ApiRequestPort {
	JsonNode getAllDetailData(int pageNo);
	JsonNode getAllImageData(int pageNo);
	int getDetailTotalPageCount();
	int getImageTotalPageCount();
}
