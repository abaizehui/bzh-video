package video.mapper;

import org.apache.ibatis.annotations.Mapper;
import video.domain.model.AuditLogRecordModel;

/**
 * 审计日志数据访问映射器
 */
@Mapper
public interface AuditLogMapper {

    int insertAuditLog(AuditLogRecordModel entity);
}
