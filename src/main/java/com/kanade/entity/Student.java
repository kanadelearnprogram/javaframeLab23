package com.kanade.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生表（教务专用，关联用户表获取基础信息） 实体类。
 *
 * @author Lenovo
 * @since 2025-11-25
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
     * 学号（主键，不自增，教务专用标识）
     */
    @Id
    private String studentId;

    /**
     * 关联用户表ID（学生账号）
     */
    private String userId;

    /**
     * 所属班级ID
     */
    private Integer classId;

    /**
     * 状态：正常/重修
     */
    private String status;

}
