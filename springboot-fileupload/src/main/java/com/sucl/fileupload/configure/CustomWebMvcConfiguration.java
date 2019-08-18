package com.sucl.fileupload.configure;

import com.sucl.fileupload.service.FileStoreService;
import com.sucl.fileupload.support.FileUploadHandlerMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author sucl
 * @since 2019/8/16
 */
@Configuration
public class CustomWebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private FileStoreService fileStoreService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new FileUploadHandlerMethodArgumentResolver(fileStoreService));
    }
}
