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
 * @since 2025-11-26
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
     * 关注者类型：注册用户/游客
     */
    private String followerType;

    /**
     * 被关注者ID（关联user表注册用户）
     */
    private String followedId;

    /**
     * 关注时间
     */
    private LocalDateTime followTime;

    /**
     * 取消关注时间
     */
    private LocalDateTime unfollowTime;

    /**
     * 关注时长（天，取消关注后计算）
     */
    private Integer followDuration;

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
