package com.sucl.fileupload.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件分片
 * @author sucl
 * @since 2019/8/16
 */
@Data
public class FilePart {

    private String id;//此次上传的标记

    private String fileName;// 文件名

    private String md5;//文件MD5

    private int partSize;//分片大小

    private int index;//分片索引

    private int totalSize;//分片总数

    private MultipartFile file;//分片文件
}
