package video.domain.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图片-视频生成记录
 * <p>
 * 存储图片生成的视频URL信息
 * 对应数据库表：bzh_image_video_generate
 * </p >
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageVideoGenerateModel {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 图片分析记录ID（关联bzh_image_analysis表）
     */
    private Long imageId;

    /**
     * 视频播放URL
     */
    private String videoUrl;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 记录更新时间
     */
    private LocalDateTime updateTime;
}