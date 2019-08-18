package com.sucl.fileupload.service.impl;

import java.util.concurrent.*;

/**
 * @author sucl
 * @since 2019/8/16
 */
public class FileUploadExectors {

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,5,
            30,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(),Executors.defaultThreadFactory());

    public FileUploadExectors(){

    }

    public void run(Callable callable){
        Future future = threadPoolExecutor.submit(callable);
    }

    public void destory(){
        threadPoolExecutor.shutdown();
    }
}
