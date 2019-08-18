package com.sucl.fileupload.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author sucl
 * @since 2019/8/16
 */
@Data
@ConfigurationProperties(prefix = "http.fileupload")
public class FileuploadProperties {

    private String defaultStorePath = "/fileupload";

    private String defaultMode = "local";

    private boolean useThread = false;

}
