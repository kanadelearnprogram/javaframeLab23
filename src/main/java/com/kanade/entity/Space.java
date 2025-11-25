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
 * 个人空间表 实体类。
 *
 * @author Lenovo
 * @since 2025-11-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("space")
public class Space implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 空间ID
     */
    @Id(keyType = KeyType.Auto)
    private Long spaceId;

    /**
     * 关联用户ID
     */
    private String userId;

    /**
     * 总空间（默认5M=5*1024*1024字节）
     */
    private Long totalSize;

    /**
     * 已用空间
     */
    private Long usedSize;

    /**
     * 扩展申请状态
     */
    private String applyStatus;

    /**
     * 申请扩展大小（字节）
     */
    private Long applySize;

    /**
     * 审批管理员ID
     */
    private String approveAdminId;

    /**
     * 审批日期
     */
    private LocalDateTime approveDate;

}
