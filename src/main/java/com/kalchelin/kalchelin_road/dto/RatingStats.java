package com.kalchelin.kalchelin_road.dto;

/**
 * 집계 결과를 담는 projection
 * 스프링 데이터가 인터페이스를 보고 구현체를 만들어준다
 */
public interface RatingStats {
    Double getAverage();    // 글이 하나도 없으면 null
    Long getCount();
}
