package com.kanade.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 成绩表 实体类。
 *
 * @author Lenovo
 * @since 2025-11-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("score")
public class Score implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 成绩ID
     */
    @Id(keyType = KeyType.Auto)
    private Long scoreId;

    /**
     * 关联学生表学号
     */
    private String studentId;

    /**
     * 关联课程ID
     */
    private Integer courseId;

    /**
     * 平时成绩（0-100）
     */
    private BigDecimal dailyScore;

    /**
     * 期末成绩（0-100）
     */
    private BigDecimal examScore;

    /**
     * 总评成绩（自动计算）
     */
    private BigDecimal totalScore;

    /**
     * 是否及格：1是/0否
     */
    private Boolean isPass;

    /**
     * 录入时间
     */
    private LocalDateTime inputTime;

}
