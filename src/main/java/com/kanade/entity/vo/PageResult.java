package com.kanade.entity.vo;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 分页响应体。
 */
@Data
public class PageResult<T> {

    private long total;
    private int pages;
    private int current;
    private int size;
    private List<T> records;

    public static <T> PageResult<T> empty(int current, int size) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(0);
        result.setPages(0);
        result.setCurrent(current);
        result.setSize(size);
        result.setRecords(Collections.emptyList());
        return result;
    }

    public static <T> PageResult<T> of(long total, int current, int size, List<T> records) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(total);
        result.setCurrent(current);
        result.setSize(size);
        int pages = (int) Math.ceil(total * 1.0 / size);
        result.setPages(pages);
        result.setRecords(records);
        return result;
    }
}

