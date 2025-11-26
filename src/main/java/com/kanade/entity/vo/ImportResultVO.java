package com.kanade.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ImportResultVO {
    private int successCount;
    private int failCount;
    private final List<String> failReasons = new ArrayList<>();

    public void addSuccess() {
        successCount++;
    }

    public void addFailure(String reason) {
        failCount++;
        failReasons.add(reason);
    }
}

