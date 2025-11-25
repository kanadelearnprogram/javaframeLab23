package com.kanade.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  实体类。
 *
 * @author Lenovo
 * @since 2025-11-22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_space")
public class TSpace implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer spaceId;

    /**
     * 关联用户账号
     */
    private String userId;

    /**
     * 总空间(5M)
     */
    private Long totalSize;

    private Long usedSize;

    private Long remainSize;

    /**
     * 0:无, 1:申请中
     */
    private Integer applyStatus;

}
