package com.sucl.fileupload.service.impl;

import com.sucl.fileupload.configure.FileuploadProperties;
import com.sucl.fileupload.model.FileStore;
import com.sucl.fileupload.service.FileStoreAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * @author sucl
 * @since 2019/8/16
 */
@Slf4j
public class LocalFileStoreAdapter implements FileStoreAdapter {

    private static final String MODE_LOCAL = "local";

    private FileuploadProperties properties;

    private String dest;

    public LocalFileStoreAdapter(FileuploadProperties properties){
        this.properties = properties;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    @Override
    public boolean support(String mode) {
        return StringUtils.isEmpty(mode) || MODE_LOCAL.equals(mode);
    }

    @Override
    public FileStore store(String fileName, InputStream inputStream) {
        FileStore fileStore = new FileStore();
        String extension = FilenameUtils.getExtension(fileName);
        String storeName = UUID.randomUUID().toString() + FilenameUtils.EXTENSION_SEPARATOR + extension;
        File file = new File(getFilePath(), storeName);
        try {
            FileUtils.forceMkdirParent(file);
            fileStore.setFileSize(inputStream.available());
            FileCopyUtils.copy(inputStream, new FileOutputStream(file));
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            return null;
        }
        fileStore.setFileName(fileName);
        fileStore.setFilePath(getFilePath());
        fileStore.setStoreName(storeName);
        fileStore.setStoreMode(MODE_LOCAL);
        fileStore.setTimestamp(new Date().getTime());
        return fileStore;
    }

    private String getFilePath() {
        if(dest == null){
            dest = properties.getDefaultStorePath();
        }
        if(dest == null){
            return "";
        }
        return dest;
    }
}
