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
 * 课程表 实体类。
 *
 * @author Lenovo
 * @since 2025-11-26
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
     * 关联班级ID（关联class表）
     */
    private Integer classId;

    /**
     * 授课教师ID（关联user表）
     */
    private String teacherId;

    /**
     * 课程状态：未开始/进行中/已结束
     */
    private String status;

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
