package com.kanade.entity.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Data
public class ScoreStatsVO {
    private Integer totalCount;
    private Integer passCount;
    private Integer failCount;
    private BigDecimal passRate;
    private BigDecimal averageScore;
    private BigDecimal maxScore;
    private BigDecimal minScore;
    private List<FailedStudentVO> failedDetails = Collections.emptyList();
}

