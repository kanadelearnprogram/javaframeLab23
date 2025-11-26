package com.kanade.entity.dto;

import com.mybatisflex.annotation.Id;
import lombok.Data;

import java.io.Serial;
import java.sql.Date;
import java.time.LocalDateTime;
@Data
public class StudentDTO {
    /**
     * 学号（主键：2023001格式）
     */
    private String studentId;

    private String studentName;//real
    private String gender;
    private String phone;
    //private String userId;

    /**
     * 所属班级ID（关联class表）
     */
    private String className;

    /**
     * 状态：正常/重修
     */
    private String status;


}
