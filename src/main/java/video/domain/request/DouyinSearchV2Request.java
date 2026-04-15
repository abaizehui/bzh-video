package video.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 抖音视频搜索V2请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DouyinSearchV2Request {

    /** 搜索关键词 */
    private String keyword;

    /** 游标/偏移量，默认0 */
    @Builder.Default
    private Integer cursor = 0;

    /** 排序类型: 0综合 1最多点赞 2最新发布 */
    @Builder.Default
    private String sortType = "0";

    /** 发布时间: 0不限 1一天 7一周 180半年 */
    @Builder.Default
    private String publishTime = "0";

    /** 视频时长筛选: 0不限 1一分钟内 2五分钟以内 */
    @Builder.Default
    private String filterDuration = "0";

    /** 内容类型: 0不限 1视频 2图文 */
    @Builder.Default
    private String contentType = "1";

    /** 搜索ID（翻页时传入） */
    @Builder.Default
    private String searchId = "";

    /** 回溯标记（翻页时传入） */
    @Builder.Default
    private String backtrace = "";
}