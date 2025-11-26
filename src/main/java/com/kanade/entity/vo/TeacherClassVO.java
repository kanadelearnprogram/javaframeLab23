package com.kanade.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeacherClassVO {
    private Integer classId;
    private String className;
    private String grade;
    private String department;
    private String status;
    private Integer studentCount;
    private LocalDateTime createTime;
}

