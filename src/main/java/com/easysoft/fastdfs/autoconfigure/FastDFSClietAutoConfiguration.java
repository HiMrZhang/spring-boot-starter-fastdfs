package com.easysoft.fastdfs.autoconfigure;

import com.easysoft.fastdfs.FastDFSClient;
import com.easysoft.fastdfs.IFastDFSClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean({IFastDFSClient.class})
@EnableConfigurationProperties(FastDFSProperties.class)
public class FastDFSClietAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(FastDFSClient.class)
    public IFastDFSClient fastDFSClient(FastDFSProperties fastDFSProperties) {
        return new FastDFSClient(fastDFSProperties);
    }
}
