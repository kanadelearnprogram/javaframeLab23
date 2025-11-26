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
 * 成绩表（总评成绩/及格状态通过代码计算更新） 实体类。
 *
 * @author Lenovo
 * @since 2025-11-26
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
     * 关联课程ID（关联course表）
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
     * 最后修改人ID（关联user表）
     */
    private String updateUserId;

    /**
     * 是否删除：1是/0否
     */
    private Boolean isDelete;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
