package com.sucl.fileupload.model;

import lombok.Data;

/**
 * @author sucl
 * @since 2019/8/16
 */
@Data
public class FileStore {

    private String fileName;

    private String storeName;

    private String filePath;

    private long fileSize;

    private String storeMode;

    private long timestamp;
}
