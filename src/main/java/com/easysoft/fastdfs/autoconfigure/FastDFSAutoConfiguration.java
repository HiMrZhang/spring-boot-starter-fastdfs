package com.easysoft.fastdfs.autoconfigure;

import com.easysoft.fastdfs.FastDFSClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean({FastDFSClient.class})
@EnableConfigurationProperties(FastDFSProperties.class)
public class FastDFSAutoConfiguration {
    @Bean
    public FastDFSClient hdfsClient(FastDFSProperties fastDFSProperties) {
        return new FastDFSClient(fastDFSProperties);
    }
}
