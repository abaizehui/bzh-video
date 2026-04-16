package video.domain.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图片分析记录
 * <p>
 * 存储用户上传的图片信息及其AI分析结果
 * 通过AI识别图片内容后，用于搜索相关抖音视频
 * </p >
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageAnalysisRecordModel {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 原始文件名
     * <p>用户上传文件时的原始名称</p >
     */
    private String fileName;

    /**
     * 图片URL
     * <p>存储在阿里云OSS上的访问地址</p >
     */
    private String imageUrl;

    /**
     * 名称
     * <p>AI识别图片后生成的文本描述，用于抖音视频搜索的关键词</p >
     */
    private String analysisName;

    /**
     * 解析内容
     * <p>AI识别图片后生成的文本描述，用于抖音视频搜索的关键词</p >
     */
    private String analysisContent;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 记录更新时间
     */
    private LocalDateTime updateTime;
}