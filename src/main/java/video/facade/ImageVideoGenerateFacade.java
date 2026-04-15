package video.facade;

import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;

import video.domain.model.ImageVideoGenerateModel;
import video.domain.model.ImageVideoRelationModel;
import video.domain.request.GenerateVideRequest;
import video.service.ImageVideoRelationService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 图片-视频生成门面
 * <p>
 * 处理图片生成视频的业务编排
 * </p >
 */
@Slf4j
@Component
public class ImageVideoGenerateFacade {


    @Resource
    private ImageVideoRelationService imageVideoRelationService;

    /**
     * 根据视频ID列表生成视频
     * <p>
     * 1. 查询视频信息（封面图）
     * 2. 调用火山方舟API生成视频
     * 3. 保存生成的视频URL到数据库
     * </p >
     *
     * @param request request
     * @return 保存后的记录列表（包含生成的ID）
     */
    public List<ImageVideoGenerateModel> generateVideo(GenerateVideRequest request) {
        List<Long> videoIds = request.getVideoIds();
        if (CollectionUtils.isEmpty(videoIds)) {
            throw new IllegalArgumentException("视频ID列表不能为空");
        }

        // 1. 查询视频信息
        List<ImageVideoRelationModel> relations = imageVideoRelationService.queryByIds(videoIds);
        if (CollectionUtils.isEmpty(relations)) {
            return Collections.emptyList();
        }
        return null;
    }

}