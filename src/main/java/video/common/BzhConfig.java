package video.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 项目配置 - 读取application.yml中bzh前缀的配置
 */
@Component
@ConfigurationProperties(prefix = "bzh")
public class BzhConfig {

    private static String profile;

    public static String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        BzhConfig.profile = profile;
    }

    /**
     * 获取OSS上传路径
     * @return {profile}/video
     */
    public static String getUploadPath() {
        return getProfile() + "/video";
    }

}
