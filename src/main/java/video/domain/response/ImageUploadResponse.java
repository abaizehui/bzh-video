package video.domain.response;

import lombok.Data;

/**
 * 图片上传响应
 */
@Data
public class ImageUploadResponse {

    private Long imageId;
    private String imageUrl;
}
