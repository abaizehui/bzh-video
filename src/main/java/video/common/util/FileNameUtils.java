package video.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

/**
 * 文件名生成工具 - 生成带时间戳的唯一文件名
 */
public class FileNameUtils {

    /**
     * 生成唯一文件名，格式：yyyyMMddHHmmss_UUID前8位.后缀
     * @param originalFilename 原始文件名，用于提取后缀
     * @return 生成的文件名
     */
    public static String generateWithTimestamp(String originalFilename) {
        String extension = Optional.ofNullable(originalFilename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf(".")))
                .orElse("");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = UUID.randomUUID().toString().substring(0, 8);
        return timestamp + "_" + random + extension;
    }
}
