package com.kanade.entity.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScoreUpdateRequest {
    private BigDecimal dailyScore;
    private BigDecimal examScore;
}

