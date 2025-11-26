package com.kanade.entity.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StudentCourseVO {
    private Integer courseId;
    private String courseName;
    private String teacherName;
    private String teacherId;
    private String className;
    private String grade;
    private String courseStatus;
    private BigDecimal dailyRatio;
    private BigDecimal examRatio;
    private LocalDateTime selectTime;
}

