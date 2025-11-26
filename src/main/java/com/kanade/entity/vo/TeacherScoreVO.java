package com.kanade.entity.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TeacherScoreVO {
    private Long scoreId;
    private String studentId;
    private String studentName;
    private Integer courseId;
    private String courseName;
    private BigDecimal dailyScore;
    private BigDecimal examScore;
    private BigDecimal totalScore;
    private Boolean isPass;
    private String updaterName;
    private LocalDateTime updateTime;
}

