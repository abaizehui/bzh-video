package video.controller;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import video.common.ApiResult;
import video.domain.model.ImageVideoGenerateModel;
import video.domain.request.GenerateVideRequest;
import video.facade.ImageVideoGenerateFacade;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 图片-视频生成控制器
 * <p>
 * 处理图片生成视频的接口
 * </p >
 */
@RestController
@RequestMapping("/api/generate")
@Validated
public class ImageVideoGenerateController {

    @Resource
    private ImageVideoGenerateFacade imageVideoGenerateFacade;

    /**
     * 生成视频
     * <p>
     * 根据视频ID列表调用火山方舟API生成视频，并保存到生成表
     * </p >
     *
     * @param request request
     * @return 统一响应，data 为保存后的记录列表
     */
    @GetMapping("/video")
    public ApiResult<List<ImageVideoGenerateModel>> generateVideo(@RequestBody GenerateVideRequest request) {
        List<ImageVideoGenerateModel> results = imageVideoGenerateFacade.generateVideo(request);
        return ApiResult.success(results);
    }


}