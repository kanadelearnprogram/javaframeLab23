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
 * 文件表（file_url=域名+file_path动态拼接） 实体类。
 *
 * @author Lenovo
 * @since 2025-11-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("file")
public class File implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 文件ID
     */
    @Id(keyType = KeyType.Auto)
    private Long fileId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 存储路径（/upload/2023001/xxx.pdf）
     */
    private String filePath;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件类型（application/pdf/image/jpg）
     */
    private String fileType;

    /**
     * 文件MD5（防重复上传）
     */
    private String fileMd5;

    /**
     * 上传用户ID（关联user表）
     */
    private String userId;

    /**
     * 关联分类ID（关联file_category表）
     */
    private Integer categoryId;

    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;

    /**
     * 状态：待审批/正常/冻结
     */
    private String status;

    /**
     * 下载量
     */
    private Integer downloadCount;

    /**
     * 是否置顶：1是/0否
     */
    private Boolean isTop;

    /**
     * 文件版本（默认1，更新时递增）
     */
    private Integer version;

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
