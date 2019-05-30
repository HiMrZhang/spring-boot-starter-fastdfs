package com.easysoft.fastdfs;

import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ServerInfo;
import org.csource.fastdfs.StorageServer;

import java.io.IOException;
import java.io.InputStream;

/**
 * @description: TODO
 * @author： zhangyunpeng
 * @date： 2019-04-16 10:42
 * @version： V1.0
 */
public interface IFastDFSClient {

    String[] upload(FastDFSFile file);

    FileInfo getFile(String groupName, String remoteFileName);

    InputStream downFile(String groupName, String remoteFileName);

    void deleteFile(String groupName, String remoteFileName) throws Exception;

    StorageServer[] getStoreStorages(String groupName) throws IOException;

    ServerInfo[] getFetchStorages(String groupName, String remoteFileName) throws IOException;

}