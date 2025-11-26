package com.kanade.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 班级表 实体类。
 *
 * @author Lenovo
 * @since 2025-11-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("class")
public class Class implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 班级ID
     */
    @Id(keyType = KeyType.Auto)
    private Integer classId;

    /**
     * 班级名称（软件工程2301）
     */
    private String className;

    /**
     * 年级（2023）
     */
    private String grade;

    /**
     * 所属院系
     */
    private String department;

    /**
     * 班主任ID（关联user表教师）
     */
    private String teacherId;

    /**
     * 班级状态：正常/已毕业/已解散
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
