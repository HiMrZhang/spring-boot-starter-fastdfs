package com.easysoft.fastdfs;

import com.easysoft.fastdfs.autoconfigure.FastDFSProperties;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;

/**
 * @description: TODO
 * @author： zhangyunpeng
 * @date： 2019-04-16 10:42
 * @version： V1.0
 */
@Slf4j
public class FastDFSClient implements IFastDFSClient {


    public FastDFSClient(FastDFSProperties fastDFSProperties) {
        try {
//      ClientGlobal init
            if (fastDFSProperties.getConnectTimeout() < 0) {
                ClientGlobal.setG_connect_timeout(5 * 1000);
            } else {
                ClientGlobal.setG_connect_timeout(fastDFSProperties.getConnectTimeout() * 1000);
            }

            if (fastDFSProperties.getNetworkTimeout() < 0) {
                ClientGlobal.setG_network_timeout(30 * 1000);
            } else {
                ClientGlobal.setG_network_timeout(fastDFSProperties.getNetworkTimeout() * 1000);
            }
            ClientGlobal.setG_charset("ISO8859-1");

            String[] szTrackerServers = fastDFSProperties.getTrackerServers();
            if (szTrackerServers == null || szTrackerServers.length == 0) {
                throw new MyException("item \"tracker_server\"   can not empty");
            } else {
                InetSocketAddress[] trackerServers = new InetSocketAddress[szTrackerServers.length];

                for (int i = 0; i < szTrackerServers.length; ++i) {
                    String[] parts = szTrackerServers[i].split("\\:", 2);
                    if (parts.length != 2) {
                        throw new MyException("the value of item \"tracker_server\" is invalid, the correct format is host:port");
                    }

                    trackerServers[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
                }

                ClientGlobal.setG_tracker_group(new TrackerGroup(trackerServers));
                ClientGlobal.setG_tracker_http_port(fastDFSProperties.getTrackerHttpPort());
                ClientGlobal.setG_anti_steal_token(fastDFSProperties.getAntiStealToken());
                if (fastDFSProperties.getAntiStealToken()) {
                    ClientGlobal.setG_secret_key(fastDFSProperties.getSecretKey());
                }

            }
        } catch (Exception e) {
            log.error("FastDFS Client Init Fail!", e);
        }
    }

    @Override
    public String[] upload(FastDFSFile file) {
        log.info("File Name: " + file.getName() + "File Length:" + file.getContent().length);

        NameValuePair[] metaList = new NameValuePair[1];
        metaList[0] = new NameValuePair("author", file.getAuthor());

        long startTime = System.currentTimeMillis();
        String[] uploadResults = null;
        StorageClient storageClient = null;
        try {
            storageClient = getStorageClient();
            uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), metaList);
        } catch (IOException e) {
            log.error("IO Exception when uploadind the file:" + file.getName(), e);
        } catch (Exception e) {
            log.error("Non IO Exception when uploadind the file:" + file.getName(), e);
        }
        log.info("upload_file time used:" + (System.currentTimeMillis() - startTime) + " ms");

        if (uploadResults == null) {
            if (storageClient != null) {
                log.error("upload file fail, error code:" + storageClient.getErrorCode());
            }
        } else {
            String groupName = uploadResults[0];
            String remoteFileName = uploadResults[1];
            log.info("upload file successfully!!!" + "group_name:" + groupName + ", remoteFileName:" + " " + remoteFileName);
        }
        return uploadResults;
    }

    @Override
    public FileInfo getFile(String groupName, String remoteFileName) {
        try {
            StorageClient storageClient = getStorageClient();
            return storageClient.get_file_info(groupName, remoteFileName);
        } catch (IOException e) {
            log.error("IO Exception: Get File from Fast DFS failed", e);
        } catch (Exception e) {
            log.error("Non IO Exception: Get File from Fast DFS failed", e);
        }
        return null;
    }

    @Override
    public InputStream downFile(String groupName, String remoteFileName) {
        try {
            StorageClient storageClient = getStorageClient();
            byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
            InputStream ins = new ByteArrayInputStream(fileByte);
            return ins;
        } catch (IOException e) {
            log.error("IO Exception: Get File from Fast DFS failed", e);
        } catch (Exception e) {
            log.error("Non IO Exception: Get File from Fast DFS failed", e);
        }
        return null;
    }

    @Override
    public void deleteFile(String groupName, String remoteFileName)
            throws Exception {
        StorageClient storageClient = getStorageClient();
        int i = storageClient.delete_file(groupName, remoteFileName);
        log.info("delete file successfully!!!" + i);
    }

    @Override
    public StorageServer[] getStoreStorages(String groupName)
            throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        return trackerClient.getStoreStorages(null, groupName);
    }

    @Override
    public ServerInfo[] getFetchStorages(String groupName,
                                         String remoteFileName) throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        return trackerClient.getFetchStorages(null, groupName, remoteFileName);
    }

    private StorageClient getStorageClient() throws IOException {
        return new StorageClient();
    }

}