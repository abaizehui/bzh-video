package video.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 抖音搜索响应 - 兼容API返回的business_data字段
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DouyinSearchResponse {

    @JsonProperty("business_data")
    private List<Object> videos;

    private Integer totalCount;
    private Boolean hasMore;
    private Long nextCursor;
}
