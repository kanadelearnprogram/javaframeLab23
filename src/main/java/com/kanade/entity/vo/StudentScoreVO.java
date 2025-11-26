package com.kanade.entity.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StudentScoreVO {
    private Long scoreId;
    private Integer courseId;
    private String courseName;
    private BigDecimal dailyScore;
    private BigDecimal examScore;
    private BigDecimal totalScore;
    private Boolean isPass;
    private BigDecimal dailyRatio;
    private BigDecimal examRatio;
    private LocalDateTime updateTime;
}

