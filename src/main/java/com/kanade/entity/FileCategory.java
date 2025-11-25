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
 * 文件分类表 实体类。
 *
 * @author Lenovo
 * @since 2025-11-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("file_category")
public class FileCategory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @Id(keyType = KeyType.Auto)
    private Integer categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类描述
     */
    private String categoryDesc;

}
