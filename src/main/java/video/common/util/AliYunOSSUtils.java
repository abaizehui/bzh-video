package video.common.util;

import com.aliyun.oss.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 阿里云OSS工具
 */
@Component
public class AliYunOSSUtils {

    @Value("${aliyun.aliDomain}")
    private String aliDomain;
    @Value("${aliyun.endPoint}")
    private String endPoint;
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.accessKey}")
    private String accessKey;
    @Value("${aliyun.bucketName}")
    private String bucketName;

    /**
     * 上传MultipartFile到OSS
     * @return 文件访问URL
     */
    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKey);
        ossClient.putObject(bucketName, fileName, file.getInputStream());
        ossClient.shutdown();
        return aliDomain + fileName;
    }

    /**
     * 上传字节数组到OSS
     * @return 文件访问URL
     */
    public String uploadByte(byte[] content, String fileName) throws IOException {
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKey);
        ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(content));
        ossClient.shutdown();
        return aliDomain + fileName;
    }
}
