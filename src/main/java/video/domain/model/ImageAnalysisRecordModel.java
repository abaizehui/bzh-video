package video.domain.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图片分析记录
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageAnalysisRecordModel {

    private Long id;
    private String fileName;
    private String imageUrl;
    private String analysisContent;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
