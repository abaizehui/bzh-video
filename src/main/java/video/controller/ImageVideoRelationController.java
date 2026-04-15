package video.controller;

import java.util.List;
import javax.annotation.Resource;

import video.common.ApiResult;
import video.domain.model.ImageVideoRelationModel;
import video.facade.ImageVideoRelationFacade;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 图片-视频关联控制器
 * <p>
 * 处理图片与抖音视频关联关系的查询接口
 * </p >
 */
@RestController
@RequestMapping("/api/video")
@Validated
public class ImageVideoRelationController {

    @Resource
    private ImageVideoRelationFacade imageVideoRelationFacade;

    /**
     * 触发搜索：根据图片ID搜索相关视频
     * <p>
     * 对图片进行AI分析并使用分析结果搜索抖音视频，结果自动保存关联关系
     * </p >
     *
     * @param imageId 图片记录ID
     * @return 统一响应，data 为关联的视频列表
     */
    @GetMapping("/search")
    public ApiResult<List<ImageVideoRelationModel>> searchByImage(@RequestParam("imageId") Long imageId) {
        List<ImageVideoRelationModel> videos = imageVideoRelationFacade.searchByImage(imageId);
        return ApiResult.success(videos);
    }

    /**
     * 查询已保存的关联视频
     * <p>
     * 从数据库查询该图片已关联的视频列表（不触发新的搜索）
     * </p >
     *
     * @param imageId 图片记录ID
     * @return 统一响应，data 为已保存的视频关联列表
     */
    @GetMapping("/list")
    public ApiResult<List<ImageVideoRelationModel>> listByImage(@RequestParam("imageId") Long imageId) {
        List<ImageVideoRelationModel> videos = imageVideoRelationFacade.queryVideosByImageId(imageId);
        return ApiResult.success(videos);
    }


}