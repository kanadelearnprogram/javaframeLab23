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
 * 文件表 实体类。
 *
 * @author Lenovo
 * @since 2025-11-25
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
     * 访问URL
     */
    private String fileUrl;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件类型（application/pdf/image/jpg）
     */
    private String fileType;

    /**
     * 上传用户ID
     */
    private String userId;

    /**
     * 关联分类ID
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
     * 是否审批通过：1是/0否
     */
    private Boolean isApproved;

}
