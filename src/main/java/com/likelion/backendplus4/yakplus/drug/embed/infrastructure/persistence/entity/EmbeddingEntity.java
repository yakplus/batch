package com.likelion.backendplus4.yakplus.drug.embed.infrastructure.persistence.entity;

/**
 * 임베딩 데이터를 JSON 문자열 형태로 제공하는 공통 인터페이스입니다.
 *
 * <p>이 인터페이스를 구현하는 엔티티는 내부에 저장된 임베딩 벡터를
 * 문자열로 반환하는 메서드를 반드시 제공해야 합니다.</p>
 *
 * @since 2025-05-03
 */
public interface EmbeddingEntity {
    /**
     * <p>각 엔티티가 내부적으로 사용하는 컬럼명이 달라도
     * 이 메서드를 통해 항상 동일한 방식으로 벡터 정보를 가져올 수 있습니다.</p>
     * @return 임베딩 벡터 문자열
     */
    String getVector();
}