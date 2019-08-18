package com.sucl.fileupload.service.impl;

import com.sucl.fileupload.configure.FileuploadProperties;
import com.sucl.fileupload.model.FileEntity;
import com.sucl.fileupload.model.FileStore;
import com.sucl.fileupload.service.FileStoreAdapter;
import com.sucl.fileupload.service.FileStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author sucl
 * @since 2019/8/16
 */
@Slf4j
public class FileStoreServiceImpl implements FileStoreService,ApplicationContextAware {

    private static final String PARAMETER_MODE = "$storeMode";

    private List<FileStoreAdapter> fileStoreAdapters;

    private FileuploadProperties properties;

    public FileStoreServiceImpl(FileuploadProperties properties){
        this.properties = properties;
    }

    @Override
    public FileStore store(InputStream in) {
        return this.fileStore(getStoreMode(null),new FileEntity(LocalDateTime.now().toString(),in));
    }

    @Override
    public List<FileStore> store(List<FileEntity> fileEntities) {
        if(properties.isUseThread()){
            CopyOnWriteArrayList<FileStore> fileStores = new CopyOnWriteArrayList<>();

            return null;
        }else{
            List<FileStore> fileStores = new ArrayList<>();
            if(fileEntities!=null && fileEntities.size()>0){
                for(FileEntity fileEntity : fileEntities){
                    fileStores.add( this.fileStore(getStoreMode(null), fileEntity) );
                }
            }
            return fileStores;
        }
    }

    @Override
    public List<FileStore> store(HttpServletRequest request) {
        return this.store(extractFileEntity(request));
    }

    private List<FileEntity> extractFileEntity(HttpServletRequest request) {
        List<FileEntity> fileEntities = new ArrayList<>();
        if(request instanceof MultipartHttpServletRequest){
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultiValueMap<String, MultipartFile> multipartMap = multipartRequest.getMultiFileMap();
            for(Map.Entry<String, List<MultipartFile>> entry : multipartMap.entrySet()){
//                String fieldName = entry.getKey();
                try {
                    List<MultipartFile> multiFiles = entry.getValue();
                    for(MultipartFile multipartFile : multiFiles){
                        fileEntities.add(new FileEntity(multipartFile.getOriginalFilename(),multipartFile.getInputStream(),WebUtils.findParameterValue(request,PARAMETER_MODE)));
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
        return fileEntities;
    }

    private List<FileEntity> servletExtractFileEntity(HttpServletRequest request){
        List<FileEntity> fileEntities = new ArrayList<>();
        try {
            Collection<Part> parts = request.getParts();
            if(parts!=null && parts.size()>0){
                for(Part part : parts){
                    fileEntities.add(new FileEntity(part.getSubmittedFileName(),part.getInputStream(),WebUtils.findParameterValue(request,PARAMETER_MODE)));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return fileEntities;
    }

    private FileStore fileStore(String mode,FileEntity fileEntity){
        if(fileStoreAdapters != null){
            for(FileStoreAdapter fileStoreAdapter : fileStoreAdapters){
                if(fileStoreAdapter.support(mode)){
                    return fileStoreAdapter.store(fileEntity.getFileName(),fileEntity.getInputStream());
                }
            }
            log.debug("no matching adapter from mode : [{}]",mode);
        }
        log.warn("no FileStoreAdapter exist");
        return null;
    }

    private String getStoreMode(String mode){
        if(mode == null){
            return properties.getDefaultMode();
        }
        return  mode;
    }

    @Override
    public InputStream getInputStream(String filePath) {
        return null;
    }

    @Override
    public boolean delete(String filePath) {
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        initFileStoreAdapters(applicationContext);
    }

    private void initFileStoreAdapters(ApplicationContext applicationContext) {
        Map<String, FileStoreAdapter> fileStoreAdapterMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, FileStoreAdapter.class, true, false);
        if(!fileStoreAdapterMap.isEmpty()){
            fileStoreAdapters = new ArrayList<>(fileStoreAdapterMap.values());
            if(Ordered.class.isAssignableFrom(FileStoreAdapter.class)){
                Collections.sort(fileStoreAdapters, OrderComparator.INSTANCE);
            }
        }
    }
}
