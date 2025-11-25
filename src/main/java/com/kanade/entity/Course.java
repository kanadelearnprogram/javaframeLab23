package com.kanade.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.math.BigDecimal;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课程表（权重之和=1.0需通过代码校验） 实体类。
 *
 * @author Lenovo
 * @since 2025-11-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("course")
public class Course implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 课程ID
     */
    @Id(keyType = KeyType.Auto)
    private Integer courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 平时成绩占比（如0.3）
     */
    private BigDecimal dailyRatio;

    /**
     * 期末成绩占比（如0.7）
     */
    private BigDecimal examRatio;

    /**
     * 关联班级ID
     */
    private Integer classId;

    /**
     * 授课教师ID（关联user表）
     */
    private String teacherId;

}
