package com.kanade.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一用户表 实体类。
 *
 * @author Lenovo
 * @since 2025-11-22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_user")
public class TUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 账号(主键): 存学号(202301)或工号(T001)
     */
    @Id
    private String userId;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String username;

    /**
     * 角色标识: student / teacher / admin
     */
    private String role;

    /**
     * 注册日期
     */
    private LocalDateTime registerDate;

    /**
     * 所属班级ID (仅student有效)
     */
    private Integer classId;

    /**
     * 职称 (仅teacher有效)
     */
    private String title;

}
