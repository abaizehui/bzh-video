package video.controller;

import java.io.IOException;
import javax.annotation.Resource;
import video.domain.response.ImageUploadResponse;
import video.facade.ImageAnalysisFacade;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片分析控制器
 */
@RestController
@RequestMapping("/api/images")
@Validated
public class ImageAnalysisController {

    @Resource
    private ImageAnalysisFacade imageAnalysisFacade;

    @PostMapping("/upload")
    public ImageUploadResponse upload(@RequestParam("file") MultipartFile file) throws IOException {
        return imageAnalysisFacade.uploadAndAnalyze(file);
    }
}
