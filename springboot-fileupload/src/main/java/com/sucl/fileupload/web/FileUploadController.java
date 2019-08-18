package com.sucl.fileupload.web;

import com.sucl.fileupload.model.FileStore;
import com.sucl.fileupload.service.FileStoreService;
import com.sucl.fileupload.support.FileUpload;
import com.sucl.fileupload.util.UserAgentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author sucl
 * @since 2019/6/19
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {
    @Autowired
    private FileStoreService fileStoreService;

    /**
     * 普通的文件上传,支持多策略存储
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity fileUpload(HttpServletRequest request){
        List<FileStore> fileStore = fileStoreService.store(request);
        return new ResponseEntity(fileStore, HttpStatus.OK);
    }

    /**
     * 内部处理上传
     * @param fileStores
     * @return
     */
    @PostMapping("/internalupload")
    public Object fileUpload(@FileUpload List<FileStore> fileStores){
        return fileStores;
    }

    /**
     * 分片上传与断点续传
     * @return
     */
    @PostMapping("/chunkupload")
    public Object chunkUpload(){
        return null;
    }

    @GetMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response){
        String fileName = "";
        configResponse(request,response,fileName);

    }

    private void configResponse(HttpServletRequest request,HttpServletResponse response,String fileName) {
        response.setContentType("utf-8");
        response.setHeader(HttpHeaders.CONTENT_TYPE,MediaType.MULTIPART_FORM_DATA_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachement;filename="+UserAgentUtils.encodeFileName(request,fileName));
    }
}
