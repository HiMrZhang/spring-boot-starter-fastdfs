package com.easysoft.fastdfs.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.fastdfs")
public class FastDFSProperties {
    private Integer connectTimeout = 5;
    private Integer networkTimeout = 30;
    private String charset = "ISO8859-1";
    private String[] trackerServers;
    private Integer trackerHttpPort = 80;
    private Boolean antiStealToken = false;
    private String secretKey;

}
