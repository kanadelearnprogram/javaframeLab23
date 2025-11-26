package com.kanade.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FailedStudentVO {
    private String studentId;
    private String studentName;
    private BigDecimal dailyScore;
    private BigDecimal examScore;
    private BigDecimal totalScore;
}

