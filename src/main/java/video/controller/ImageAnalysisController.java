package video.controller;

import java.io.IOException;
import javax.annotation.Resource;

import video.common.ApiResult;
import video.facade.ImageAnalysisFacade;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片分析控制器
 * <p>
 * 处理图片上传相关接口
 * </p >
 */
@RestController
@RequestMapping("/api/images")
@Validated
public class ImageAnalysisController {

    @Resource
    private ImageAnalysisFacade imageAnalysisFacade;

    /**
     * 仅上传图片，返回图片ID
     *
     * @param file 图片文件
     * @return 统一响应，data 为图片记录ID
     */
    @PostMapping("/upload")
    public ApiResult<Long> upload(@RequestParam("file") MultipartFile file) throws IOException {
        Long imageId = imageAnalysisFacade.uploadImage(file);
        return ApiResult.success(imageId);
    }

}