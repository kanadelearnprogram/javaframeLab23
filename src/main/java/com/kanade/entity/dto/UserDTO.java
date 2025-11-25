package com.kanade.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String username;

    /**
     * 角色标识: student / teacher / admin / visitor
     */
    private String role;

    /**
     * 所属班级ID (仅student有效)
     */
    private Integer classId;

    /**
     * 职称 (仅teacher有效)
     */
    private String title;
}
