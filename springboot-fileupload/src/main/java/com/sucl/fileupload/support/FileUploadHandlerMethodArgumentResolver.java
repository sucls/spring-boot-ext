package com.sucl.fileupload.support;

import com.sucl.fileupload.model.FileStore;
import com.sucl.fileupload.service.FileStoreService;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * @author sucl
 * @since 2019/8/16
 */
public class FileUploadHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private FileStoreService fileStoreService;

    public FileUploadHandlerMethodArgumentResolver(FileStoreService fileStoreService){
        this.fileStoreService = fileStoreService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if(parameter.getParameterAnnotation(FileUpload.class)!=null) {
            ResolvableType resolvableType = ResolvableType.forMethodParameter(parameter);
            return FileStore.class.isAssignableFrom(resolvableType.getRawClass())||
                    (resolvableType.getGeneric(0)!=null && FileStore.class.isAssignableFrom(resolvableType.getGeneric(0).getRawClass()));
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        List<FileStore> fileStores = fileStoreService.store(request);
        if(fileStores==null || fileStores.size()==0){
            return null;
        }
        if(FileStore.class.isAssignableFrom(parameter.getParameterType())){
            return fileStores.get(0);
        }
        if(Collection.class.isAssignableFrom(parameter.getParameterType())){
            return fileStores;
        }
        return null;
    }
}
