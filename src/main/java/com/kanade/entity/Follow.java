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
 * 关注表 实体类。
 *
 * @author Lenovo
 * @since 2025-11-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("follow")
public class Follow implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 关注ID
     */
    @Id(keyType = KeyType.Auto)
    private Long followId;

    /**
     * 关注者ID（游客/用户）
     */
    private String followerId;

    /**
     * 被关注者ID（注册用户）
     */
    private String followedId;

    /**
     * 关注时间
     */
    private LocalDateTime followTime;

}
