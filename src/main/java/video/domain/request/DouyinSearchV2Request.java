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

    private String keyword;
    @Builder.Default
    private Integer cursor = 0;
    @Builder.Default
    private String sortType = "1";
    @Builder.Default
    private String publishTime = "7";
    @Builder.Default
    private String contentType = "1";
}
