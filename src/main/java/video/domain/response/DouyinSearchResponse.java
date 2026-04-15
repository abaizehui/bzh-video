package video.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 抖音搜索响应 - 完整映射API返回的business_data字段
 * <p>
 * 数据结构说明：
 * - businessData[]: 搜索返回的数据列表
 * - hasMore: 是否有更多数据（1=是，0=否）
 * - searchId: 搜索ID（用于翻页）
 * - cursor: 当前游标位置
 * </p >
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DouyinSearchResponse {

    /**
     * 搜索返回的数据列表
     */
    @JsonProperty("business_data")
    private List<BusinessData> businessData;


    /**
     * 业务数据项
     * <p>
     * 字段说明：
     * - dataId: 数据编号（字符串，如 "0"）
     * - type: 数据类型（1=视频，66668=相关搜索词）
     * - data: 数据详情（包含aweme_info或related_word_list）
     * </p >
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class BusinessData {

        /**
         * 数据编号（字符串，如 "0"）
         */
        @JsonProperty("data_id")
        private String dataId;

        /**
         * 数据类型（1=视频，66668=相关搜索词）
         */
        @JsonProperty("type")
        private Integer type;

        /**
         * 数据详情
         */
        @JsonProperty("data")
        private DataDetail data;
    }

    /**
     * 数据详情
     * <p>
     * 字段说明：
     * - type: 数据类型（1=视频）
     * - awemeInfo: 视频详细信息
     * - relatedWordList: 相关搜索词列表（当type=66668时）
     * </p >
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class DataDetail {

        /**
         * 数据类型（1=视频）
         */
        @JsonProperty("type")
        private Integer type;

        /**
         * 视频详细信息
         */
        @JsonProperty("aweme_info")
        private AwemeInfo awemeInfo;

    }

    /**
     * 视频信息（Aweme）
     * <p>
     * 字段说明：
     * - awemeId: 视频ID
     * - desc: 视频描述/标题
     * - createTime: 发布时间（Unix时间戳）
     * - author: 作者信息
     * - video: 视频播放信息
     * - music: 背景音乐信息
     * - statistics: 统计数据（点赞/评论/分享等）
     * - status: 状态信息
     * </p >
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class AwemeInfo {

        /**
         * 视频ID
         */
        @JsonProperty("aweme_id")
        private String awemeId;

        /**
         * 视频描述/标题
         */
        @JsonProperty("desc")
        private String desc;

        /**
         * 发布时间（Unix时间戳）
         */
        @JsonProperty("create_time")
        private Long createTime;

        /**
         * 视频播放信息（包含播放地址、封面等）
         */
        @JsonProperty("video")
        private Video video;

        /**
         * 背景音乐信息
         */
        @JsonProperty("music")
        private Music music;

        /**
         * 统计数据（点赞/评论/分享/播放次数等）
         */
        @JsonProperty("statistics")
        private Statistics statistics;

        /**
         * 视频时长（毫秒）
         */
        @JsonProperty("duration")
        private Long duration;

        /**
         * 视频类型
         */
        @JsonProperty("aweme_type")
        private Integer awemeType;

        /**
         * 是否电商视频（1=是）
         */
        @JsonProperty("is_ecom_aweme")
        private Integer isEcomAweme;

    }

    /**
     * 图片信息
     * <p>
     * 字段说明：
     * - uri: 图片资源URI
     * - urlList: 图片URL列表（通常包含多个CDN地址）
     * - width/height: 图片尺寸
     * </p >
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class ImageInfo {

        /**
         * 图片资源URI
         */
        @JsonProperty("uri")
        private String uri;

        /**
         * 图片URL列表（通常包含多个CDN地址）
         */
        @JsonProperty("url_list")
        private List<String> urlList;

        /**
         * 图片宽度
         */
        @JsonProperty("width")
        private Integer width;

        /**
         * 图片高度
         */
        @JsonProperty("height")
        private Integer height;
    }

    /**
     * 视频播放信息
     * <p>
     * 字段说明：
     * - playAddr: 播放地址（play_addr.url_list）
     * - cover: 封面图片（cover.url_list）
     * - dynamicCover: 动态封面
     * - originCover: 原始封面
     * - duration: 时长（毫秒）
     * - ratio: 分辨率（如"720p"）
     * - bitRate: 多码率播放信息列表
     * - hasWatermark: 是否有水印
     * </p >
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Video {

        /**
         * 播放地址信息（包含url_list播放地址列表）
         */
        @JsonProperty("play_addr")
        private PlayAddr playAddr;

        /**
         * 封面图片信息
         */
        @JsonProperty("cover")
        private ImageInfo cover;

        /**
         * 视频高度
         */
        @JsonProperty("height")
        private Integer height;

        /**
         * 视频宽度
         */
        @JsonProperty("width")
        private Integer width;

        /**
         * 分辨率（如"720p"）
         */
        @JsonProperty("ratio")
        private String ratio;

        /**
         * 视频时长（毫秒）
         */
        @JsonProperty("duration")
        private Long duration;

    }

    /**
     * 播放地址详情
     * <p>
     * 字段说明：
     * - uri: 资源URI
     * - urlList: 播放地址列表（支持高清播放，包含多个CDN地址）
     * - dataSize: 文件大小
     * - fileHash: 文件哈希
     * </p >
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class PlayAddr {

        /**
         * 播放地址列表（支持高清播放，包含多个CDN地址）
         */
        @JsonProperty("url_list")
        private List<String> urlList;

        /**
         * 视频宽度
         */
        @JsonProperty("width")
        private Integer width;

        /**
         * 视频高度
         */
        @JsonProperty("height")
        private Integer height;

        /**
         * 数据大小（字节）
         */
        @JsonProperty("data_size")
        private Long dataSize;
    }

    /**
     * 背景音乐信息
     * <p>
     * 字段说明：
     * - idStr: 音乐ID
     * - title: 音乐标题
     * - author: 音乐创作者昵称
     * - playUrl: 音乐播放链接
     * - duration: 音乐时长
     * - isOriginalSound: 是否原创声音
     * </p >
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Music {

        /**
         * 音乐ID（字符串型）
         */
        @JsonProperty("id_str")
        private String idStr;

        /**
         * 音乐标题
         */
        @JsonProperty("title")
        private String title;

        /**
         * 音乐创作者昵称
         */
        @JsonProperty("author")
        private String author;

        /**
         * 音乐播放URL
         */
        @JsonProperty("play_url")
        private PlayUrl playUrl;

    }

    /**
     * 音乐播放URL
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class PlayUrl {

        /**
         * 资源URI
         */
        @JsonProperty("uri")
        private String uri;

        /**
         * 音乐播放链接列表
         */
        @JsonProperty("url_list")
        private List<String> urlList;

        /**
         * 宽度
         */
        @JsonProperty("width")
        private Integer width;

        /**
         * 高度
         */
        @JsonProperty("height")
        private Integer height;

        /**
         * URL键值
         */
        @JsonProperty("url_key")
        private String urlKey;
    }

    /**
     * 统计数据
     * <p>
     * 字段说明：
     * - commentCount: 评论数
     * - diggCount: 点赞数
     * - shareCount: 分享数
     * - playCount: 播放次数
     * - collectCount: 收藏次数
     * - downloadCount: 下载次数
     * </p >
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Statistics {

        /**
         * 视频ID
         */
        @JsonProperty("aweme_id")
        private String awemeId;

        /**
         * 评论数
         */
        @JsonProperty("comment_count")
        private Integer commentCount;

        /**
         * 点赞数
         */
        @JsonProperty("digg_count")
        private Integer diggCount;

        /**
         * 播放次数（通常为0，需要额外接口获取）
         */
        @JsonProperty("play_count")
        private Integer playCount;

        /**
         * 分享数
         */
        @JsonProperty("share_count")
        private Integer shareCount;

        /**
         * 转发数
         */
        @JsonProperty("forward_count")
        private Integer forwardCount;

        /**
         * 收藏数
         */
        @JsonProperty("collect_count")
        private Integer collectCount;
    }


}