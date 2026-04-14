package video.domain.enums;

import lombok.Getter;

/**
 * 审计日志动作类型枚举
 */
@Getter
public enum AuditLogActionEnum {

    IMAGE_UPLOAD("图片上传");

    private final String description;

    AuditLogActionEnum(String description) {
        this.description = description;
    }
}
