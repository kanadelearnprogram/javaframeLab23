package com.kanade.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.sql.Date;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表（所有注册角色） 实体类。
 *
 * @author Lenovo
 * @since 2025-11-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID（工号/学号：ADMIN001/T001/2023001）
     */
    @Id
    private String userId;

    /**
     * 登录用户名
     */
    private String username;

    /**
     * 密码（BCrypt加密）
     */
    private String password;

    /**
     * 角色：管理员/教务/教师/学生
     */
    private String role;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 注册日期
     */
    private Date registerDate;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 账号状态：1正常/0冻结
     */
    private Boolean status;

}
