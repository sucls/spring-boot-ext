spring-boot version 1.5.*.RELEASE 
    MultipartResolver：在dispatcherServlet中，如果有该配置，则针对multipart/*的请求全部会转换成MultipartHttpServletRequest，通过该请求可以完成文件的上传。
    在系统中默认有两个实现：
    StandardMultipartHttpServletRequest(系统默认)
    CommonsMultipartResolver(需要添加依赖commons-io,commons-fileupload，并需要主动去配置，但是spring.http.multipart相关配置无效)
    如果设置spring.http.multipart.enabled=false，同时没有配置CommonsMultipartResolver，那么请求将转换成普通的HttpServletRequest


问题：
    其中StandardMultipartHttpServletRequest主要采用了servlet3的特性，通过request.getParts()消费掉请求，而一段调用了该方法，之后通过commons的API获取的，
    但是添加了@EnableWebMvc则可以使用commons（前提是MultipartResolver设置为CommonsMultipartResolver），难道不用@EnableWebMvc则在哪里调用了request.getParts()?
    使用commins 在application中的配置无效，需要自己处理（MultipartAutoConfiguration）
    MultipartConfigElement导致以上问题！！！
    
实现：
    @EnableWebMvc + CommonsMultipartResolver 通过MultipartHttpServletRequest可获取文件
    StandardMultipartHttpServletRequest 通过MultipartHttpServletRequest可获取文件
    spring.http.multipart.enabled=false + CommonsMultipartResolver正常
    spring.http.multipart.enabled=false + Servlet3自己处理


扩展：
 1、通过适配器模式实现多存储策略
 2、通过webupload组件实现文件大文件的分片上传与断点续传
 3、通过http range实现文件断点下载
 4、通过appache commons实现文件上传进度监听