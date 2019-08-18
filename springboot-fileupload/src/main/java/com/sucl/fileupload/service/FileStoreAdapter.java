package com.sucl.fileupload.service;

import com.sucl.fileupload.model.FileEntity;
import com.sucl.fileupload.model.FileStore;

import java.io.InputStream;

/**
 * @author sucl
 * @since 2019/8/16
 */
public interface FileStoreAdapter {

    boolean support(String mode);

    FileStore store(String fileName, InputStream inputStream);

}
