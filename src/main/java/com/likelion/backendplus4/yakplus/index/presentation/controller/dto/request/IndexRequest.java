package com.likelion.backendplus4.yakplus.index.presentation.controller.dto.request;

/**
 * 인덱싱 요청 정보 DTO
 *
 * @since 2025-04-22
 * @modified 2025-04-28
 * 25.04.27 - itemSeq -> drugId로 수정
 *          - 페이징 처리 로직 수정
 * 25.04.28 - DrugController에서 사용하지 않도록 수정(추후 필요한 필드로 변경하여 사용할 예정)
 */
public record IndexRequest(
        Long lastDrugId,
        int limit) {
}