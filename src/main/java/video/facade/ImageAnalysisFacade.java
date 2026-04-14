package video.facade;

import java.io.IOException;
import javax.annotation.Resource;
import video.common.BzhConfig;
import video.common.util.AliYunOSSUtils;
import video.common.util.DouBaoVisionUtil;
import video.common.util.DouYinApiClient;
import video.common.util.FileNameUtils;
import video.common.util.JsonUtils;
import video.domain.enums.AuditLogActionEnum;
import video.domain.model.AuditLogRecordModel;
import video.domain.model.ImageAnalysisRecordModel;
import video.domain.request.DouyinSearchV2Request;
import video.domain.response.DouyinSearchResponse;
import video.domain.response.ImageUploadResponse;
import video.service.AuditLogService;
import video.service.ImageAnalysisService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

/**
 * 图片分析门面 - 协调上传、OSS、AI识别、落库流程
 */
@Slf4j
@Component
public class ImageAnalysisFacade {

    @Resource
    private ImageAnalysisService imageAnalysisService;
    @Resource
    private AuditLogService auditLogService;
    @Resource
    private DouBaoVisionUtil doubaoVisionUtil;
    @Resource
    private AliYunOSSUtils aliYunOSSUtils;
    @Resource
    private DouYinApiClient douYinApiClient;

    public ImageUploadResponse uploadAndAnalyze(MultipartFile file) throws IOException {
        String uploadPath = BzhConfig.getUploadPath();
        String storedFilename = FileNameUtils.generateWithTimestamp(file.getOriginalFilename());
        String imageUrl = aliYunOSSUtils.uploadFile(file, uploadPath + "/" + storedFilename);
        String recognizeName = doubaoVisionUtil.recognizeName(imageUrl);

        ImageAnalysisRecordModel record = ImageAnalysisRecordModel.builder()
                .fileName(storedFilename)
                .imageUrl(imageUrl)
                .analysisContent(recognizeName)
                .build();

        Long imageId = imageAnalysisService.create(record);

        auditLogService.insert(AuditLogRecordModel.builder()
                .action(AuditLogActionEnum.IMAGE_UPLOAD)
                .detail(JsonUtils.toJson(record))
                .operator("system")
                .build());

        DouyinSearchV2Request douyinRequest = DouyinSearchV2Request.builder()
                .keyword(recognizeName)
                .cursor(0)
                .sortType("1")
                .publishTime("7")
                .contentType("1")
                .build();

        try {
            DouyinSearchResponse searchResult = douYinApiClient.searchVideo(douyinRequest);
            log.info("抖音搜索完成，返回 {} 条结果", searchResult.getVideos() != null ? searchResult.getVideos().size() : 0);
        } catch (Exception e) {
            log.warn("抖音搜索失败，但不影响主流程: {}", e.getMessage());
        }

        ImageUploadResponse response = new ImageUploadResponse();
        response.setImageId(imageId);
        response.setImageUrl(imageUrl);
        return response;
    }
}
