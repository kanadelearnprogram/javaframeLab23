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
 *  实体类。
 *
 * @author Lenovo
 * @since 2025-11-22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_file")
public class TFile implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer fileId;

    /**
     * 上传者账号
     */
    private String userId;

    private Integer categoryId;

    private String fileName;

    private String filePath;

    private Long fileSize;

    private Integer isFrozen;

    private Integer isTop;

    private Integer auditStatus;

    private Integer downloadCount;

    private LocalDateTime uploadTime;

}
