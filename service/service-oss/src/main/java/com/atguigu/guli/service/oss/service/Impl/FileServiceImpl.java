package com.atguigu.guli.service.oss.service.Impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.atguigu.guli.service.oss.service.FileService;
import com.atguigu.guli.service.oss.util.OssProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private OssProperties ossProperties;


    /**
     *
     * @param inputStream
     * @param module 所属模块
     * @param filename 原始文件名
     * @return 文件的url地址
     */
    @Override
    public String upload(InputStream inputStream, String module, String filename) {

        String endpoint = ossProperties.getEndpoint();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();
        String bucketname = ossProperties.getBucketname();

        //创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint,keyid,keysecret);

        //文件名: uuid.扩展名
        String newFileName = UUID.randomUUID().toString();
        String fileExtention = filename.substring(filename.lastIndexOf("."));

        //构建日期路径：/avatar/2020/02/19/文件名
        String dateFolder = new DateTime().toString("yyyy/MM/dd");

        //判断oss实例是否存在: 如果不存在则创建，如果存在则获取
        if (!ossClient.doesBucketExist(bucketname)){
            //创建bucket
            ossClient.createBucket(bucketname);
            //设置oss实例的访问权限： 公共读
            ossClient.setBucketAcl(bucketname, CannedAccessControlList.PublicRead);
        }

        String yourObjectName = new StringBuffer()
                .append(module)
                .append("/")
                .append(dateFolder)
                .append("/")
                .append(newFileName)
                .append(fileExtention)
                .toString();

        //上传文件至阿里云
        //yourProjectName : buket下的[路径 + 文件名]
        ossClient.putObject(ossProperties.getBucketname(),yourObjectName,inputStream);

        //关闭OSSClient
        ossClient.shutdown();

        //返回url地址
        return new StringBuffer().append("https://")
                .append(bucketname)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(yourObjectName)
                .toString();
    }

    @Override
    public void removeFile(String url) {
        String endpoint = ossProperties.getEndpoint();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();
        String bucketname = ossProperties.getBucketname();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);

        //url：
        //https://inline-teach-file-190805.oss-cn-beijing.aliyuncs.com/avatar/2020/02/19/09eea761-032b-48f0-8541-dcb9ad2a53f1.png
        //yourObjectName：avatar/2020/02/19/09eea761-032b-48f0-8541-dcb9ad2a53f1.png
        String host = "https://" + bucketname + "." + endpoint + "/";
        String objectName = url.substring(host.length());
        System.out.println("objectName: " + objectName);
        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketname, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
