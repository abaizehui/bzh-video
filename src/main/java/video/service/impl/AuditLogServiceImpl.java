package video.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import video.domain.model.AuditLogRecordModel;
import video.mapper.AuditLogMapper;
import video.service.AuditLogService;

/**
 * 审计日志服务实现
 */
@Service
public class AuditLogServiceImpl implements AuditLogService {

    @Resource
    private AuditLogMapper auditLogMapper;

    @Override
    public void insert(AuditLogRecordModel record) {
        auditLogMapper.insertAuditLog(record);
    }
}
