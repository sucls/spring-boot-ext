package com.sucl.fileupload.service;

import com.sucl.fileupload.model.FileEntity;
import com.sucl.fileupload.model.FileStore;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

/**
 * 文件存储服务,支持多种存储策略
 * @author sucl
 * @since 2019/8/16
 */
public interface FileStoreService {

    FileStore store(InputStream in);

    List<FileStore> store(List<FileEntity> fileEntities);

    List<FileStore> store(HttpServletRequest request);

    InputStream getInputStream(String filePath);

    boolean delete(String filePath);
}
