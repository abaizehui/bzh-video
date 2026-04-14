package video.domain.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import video.domain.enums.AuditLogActionEnum;

/**
 * 审计日志记录
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogRecordModel {

    private Long id;
    private AuditLogActionEnum action;
    private String detail;
    private String operator;
    private LocalDateTime createTime;
}
