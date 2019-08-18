package com.sucl.fileupload.model;

import lombok.Data;

import java.io.InputStream;

/**
 * @author sucl
 * @since 2019/8/16
 */
@Data
public class FileEntity {

    private String fileName;

    private InputStream inputStream;

    private String mode;

    public FileEntity(){}

    public FileEntity(String fileName,InputStream inputStream,String mode){
        this.fileName = fileName;
        this.inputStream = inputStream;
        this.mode = mode;
    }

    public FileEntity(String fileName,InputStream inputStream){
        this(fileName,inputStream,null);
    }

}
