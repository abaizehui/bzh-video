package video.domain.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 生成视频请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateVideRequest {


    private List<Long> videoIds;

}
