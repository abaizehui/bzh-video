package video.service;

import video.domain.model.AuditLogRecordModel;

/**
 * 审计日志服务接口
 */
public interface AuditLogService {

    void insert(AuditLogRecordModel record);
}
