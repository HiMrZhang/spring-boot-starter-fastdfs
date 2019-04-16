package com.easysoft.fastdfs.test;

import com.easysoft.fastdfs.FastDFSFile;
import com.easysoft.fastdfs.IFastDFSClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class FastDFSTest {

    @Autowired
    private IFastDFSClient fastDFSClient;


    @Test()
    public void uploadLocalFile() throws IOException {
        InputStream inputStream = null;
        try {


            File file = new File("/Users/zhangyunpeng/Downloads/1.jpeg");
            String fileName = file.getName();
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            byte[] fileBuff = null;
            inputStream = new FileInputStream(file);
            if (inputStream != null) {
                int len1 = inputStream.available();
                fileBuff = new byte[len1];
                inputStream.read(fileBuff);
            }
            FastDFSFile fastDFSFile = new FastDFSFile(fileName, fileBuff, ext);
            String[] fileAbsolutePath = fastDFSClient.upload(fastDFSFile);
            String path = fastDFSClient.getTrackerUrl() + fileAbsolutePath[0] + "/" + fileAbsolutePath[1];
            log.info(path);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @Test
    public void delete() {
        try {
            fastDFSClient.deleteFile("group1", "M00/00/01/L2EQuVy1elSALBZ_AADQ_ahjdHQ63.jpeg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void download() throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = fastDFSClient.downFile("group1", "M00/00/01/L2EQuVy1fViAOy4QAADQ_ahjdHQ65.jpeg");
            output = new FileOutputStream("/Users/zhangyunpeng/Downloads/2.jpeg");
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }

}
