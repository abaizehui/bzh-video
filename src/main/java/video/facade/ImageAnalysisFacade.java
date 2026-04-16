package video.facade;

import java.io.IOException;
import javax.annotation.Resource;

import video.common.BzhConfig;
import video.common.util.AliYunOSSUtils;
import video.common.util.DouBaoVisionUtil;
import video.common.util.FileNameUtils;
import video.domain.model.ImageAnalysisRecordModel;
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
    private DouBaoVisionUtil doubaoVisionUtil;
    @Resource
    private AliYunOSSUtils aliYunOSSUtils;

    /**
     * 仅上传图片，不进行AI分析
     *
     * @param file 上传的图片文件
     * @return 图片记录ID
     * @throws IOException 上传失败时抛出
     */
    public ImageAnalysisRecordModel uploadImage(MultipartFile file) throws IOException {
        String uploadPath = BzhConfig.getUploadPath();
        String storedFilename = FileNameUtils.generateWithTimestamp(file.getOriginalFilename());
        String imageUrl = aliYunOSSUtils.uploadFile(file, uploadPath + "/" + storedFilename);
        ImageAnalysisRecordModel record = ImageAnalysisRecordModel.builder()
                .fileName(storedFilename)
                .imageUrl(imageUrl)
                .analysisName(doubaoVisionUtil.recognizeName(imageUrl))
                .analysisContent(doubaoVisionUtil.recognizeContent(imageUrl))  // 暂不分析，后续填充
                .build();
        imageAnalysisService.create(record);
        log.info("图片上传成功，imageId={}", record.getId());
        return record;
    }

}