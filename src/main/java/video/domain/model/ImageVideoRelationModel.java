package video.domain.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图片-视频关联记录
 * <p>
 * 存储图片分析后关联的抖音视频信息
 * 一个图片ID可对应多个视频（一对多关系）
 * 对应数据库表：bzh_image_video_relation
 * </p >
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageVideoRelationModel {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 图片分析记录ID（关联bzh_image_analysis表）
     */
    private Long imageId;

    /**
     * 抖音视频ID（aweme_id）
     */
    private String videoId;

    /**
     * 视频描述/标题
     */
    private String videoDesc;

    /**
     * 视频封面URL
     */
    private String videoCoverUrl;

    /**
     * 视频播放URL（主地址）
     */
    private String videoPlayUrl;

    /**
     * 点赞数
     */
    private Integer diggCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 分享数
     */
    private Integer shareCount;

    /**
     * 收藏数
     */
    private Integer collectCount;

    /**
     * 视频时长（毫秒）
     */
    private Long duration;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 记录更新时间
     */
    private LocalDateTime updateTime;
}