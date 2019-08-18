package com.sucl.fileupload.configure;

import com.sucl.fileupload.service.FileStoreAdapter;
import com.sucl.fileupload.service.FileStoreService;
import com.sucl.fileupload.service.impl.FileStoreServiceImpl;
import com.sucl.fileupload.service.impl.LocalFileStoreAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.MultipartConfigElement;

/**
 * @author sucl
 * @since 2019/8/16
 */
@Configuration
@EnableConfigurationProperties(FileuploadProperties.class)
public class FileuploadConfiguration {
    private final FileuploadProperties properties;

    public FileuploadConfiguration(FileuploadProperties properties){
        this.properties = properties;
    }

    /**
     * 文件上传服务
     * @return
     */
    @Bean
    public FileStoreService fileStoreService(){
        return new FileStoreServiceImpl(properties);
    }

    /**
     * 文件上传具体实现，根据参数适配
     * @return
     */
    @Bean
    public FileStoreAdapter localFileStoreAdapter(){
        return new LocalFileStoreAdapter(properties);
    }

    /**
     * 需要额外配置：
     * spring.http.multipart.enabled=false 或  @EnableWebMvc
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "spring.http.commons.enabled",havingValue = "true")
    public MultipartResolver multipartResolver(){
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
//        commonsMultipartResolver.setResolveLazily(true);
        return commonsMultipartResolver;
    }

    /**
     * 如果没有上面的配置，这个将导致CommonsMultipartResolver不可用
     * @see MultipartAutoConfiguration
     * @return
     */
//    @Bean
    public MultipartConfigElement multipartConfigElement() {
        return new MultipartConfigElement("");
    }
}
