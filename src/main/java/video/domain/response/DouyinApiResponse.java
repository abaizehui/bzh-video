package video.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 抖音API通用响应
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DouyinApiResponse<T> {

    @JsonProperty("code")
    private Integer statusCode;
    private String message;
    private T data;
    private Long cursor;
    private Boolean hasMore;
}
