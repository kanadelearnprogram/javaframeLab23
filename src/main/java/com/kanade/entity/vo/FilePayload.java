package com.kanade.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilePayload {
    private byte[] content;
    private String filename;
    private String contentType;
}

