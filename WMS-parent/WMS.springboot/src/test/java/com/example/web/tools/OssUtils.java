package com.example.web.tools;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class OssUtils {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    /**
     * 上传文件到 OSS
     * @param file 上传的文件
     * @return 文件的访问 URL
     */
    public String uploadFile(MultipartFile file) {
        // 创建 OSSClient 实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 生成唯一的文件名
            String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

            // 获取文件输入流
            InputStream inputStream = file.getInputStream();

            // 创建 PutObjectRequest 对象
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream);

            // 上传文件
            ossClient.putObject(putObjectRequest);

            // 拼接文件的访问 URL
            return "https://" + bucketName + "." + endpoint + "/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            // 关闭 OSSClient
            ossClient.shutdown();
        }
    }
}