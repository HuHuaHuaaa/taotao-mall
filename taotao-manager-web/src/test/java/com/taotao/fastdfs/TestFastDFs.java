package com.taotao.fastdfs;

import com.taotao.util.FastDFSClient;
import org.csource.fastdfs.*;
import org.junit.Test;

public class TestFastDFs {

    @Test
    public void uploadFile() throws Exception{
        ClientGlobal.init("D:/Java/Javaproject/taotao-parent/taotao-manager-web/src/main/resources/resource/client.conf");
        TrackerClient trackerClient=new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer=null;
        StorageClient storageClient=new StorageClient(trackerServer,storageServer);
        String[] jpgs = storageClient.upload_file("C:/Users/Silence/OneDrive/图片/Saved Pictures/QQ图片20201016100712.jpg", "jpg", null);
        for (String jpg : jpgs) {
            System.out.println(jpg);
        }
    }

    @Test
    public void testFastClient() throws Exception {
        FastDFSClient fastDFSClient=new FastDFSClient("D:/Java/Javaproject/taotao-parent/taotao-manager-web/src/main/resources/resource/client.conf");
        String s = fastDFSClient.uploadFile("C:/Users/Silence/OneDrive/图片/Saved Pictures/QQ图片20201022163001.jpg");
        System.out.println(s);

    }

}
