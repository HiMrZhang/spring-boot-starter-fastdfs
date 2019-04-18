# fastdfs操作工具类

作者：掌少

## 1. 概述

支持上传、下载、删除等操作

## 2. 使用方法

## 2.1 导入依赖包

    compile "com.easysoft:spring-boot-starter-fastdfs:xxx"


## 2.2 配置

	spring:
      fastdfs:
        connectTimeout: 60
        networkTimeout: 60
        charset: UTF-8
        trackerHttpPort: 8888
        antiStealToken: false
        secretKey: test1234
        trackerServers:
          - 172.16.155.103:22122
          - 172.16.155.104:22122

### 2.3 使用方法

    ......
    @Autowired
    private IFastDFSClient fastDFSClient;
