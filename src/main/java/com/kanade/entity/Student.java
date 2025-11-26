package com.kanade.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生表（教务专用，关联用户表获取基础信息） 实体类。
 *
 * @author Lenovo
 * @since 2025-11-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("student")
public class Student implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 学号（主键：2023001格式）
     */
    @Id
    private String studentId;

    /**
     * 关联用户表ID（学生账号）
     */
    private String userId;

    /**
     * 所属班级ID（关联class表）
     */
    private Integer classId;

    /**
     * 状态：正常/重修
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
