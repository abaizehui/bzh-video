package video.facade;

import java.util.List;
import javax.annotation.Resource;
import video.common.util.DouBaoVisionUtil;
import video.domain.model.ImageAnalysisRecordModel;
import video.domain.model.ImageVideoRelationModel;
import video.domain.request.GenerateVideRequest;
import video.service.ImageAnalysisService;
import video.service.ImageVideoRelationService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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
    @Resource
    private ImageAnalysisService imageAnalysisService;
    @Resource
    private DouBaoVisionUtil douBaoVisionUtil;

    /**
     * 根据视频ID列表生成视频
     * <p>
     * 1. 查询视频信息（播放地址）
     * 2. 调用火山方舟API生成视频
     * 3. 保存生成的视频URL到数据库
     * </p >
     *
     * @param request request
     * @return 保存后的记录列表（包含生成的ID）
     */
    public String generateVideo(GenerateVideRequest request) {
        List<Long> videoIds = request.getVideoIds();
        if (CollectionUtils.isEmpty(videoIds)) {
            throw new IllegalArgumentException("视频ID列表不能为空");
        }
        log.info("开始生成视频, videoIds={}", videoIds);

        // 1. 查询视频信息
        List<ImageVideoRelationModel> relations = imageVideoRelationService.queryByIds(videoIds);
        if (CollectionUtils.isEmpty(relations)) {
            return null;
        }
        ImageVideoRelationModel first = relations.get(0);
        if (!StringUtils.hasText(first.getVideoCoverUrl()) && !StringUtils.hasText(first.getVideoPlayUrl())) {
            throw new IllegalArgumentException("封面图URL和视频URL都为空，无法调用豆包模型");
        }

        if (!StringUtils.hasText(first.getVideoPlayUrl())) {
            throw new IllegalArgumentException("视频播放URL为空，无法先生成文案");
        }
        ImageAnalysisRecordModel imageAnalysisRecordModel = imageAnalysisService.getById(first.getImageId());
        log.info("命中视频记录, imageId={}, videoId={}, playUrl={}, coverUrl={}",
                first.getImageId(), first.getVideoId(), first.getVideoPlayUrl(), first.getVideoCoverUrl());
        String analyzePrompt = douBaoVisionUtil.buildVideoPrompt(first);
        log.info("视频文案生成提示词: {}", analyzePrompt);
        String videoCopy = douBaoVisionUtil.analyzeVideo(first.getVideoPlayUrl(), analyzePrompt);
        log.info("视频文案生成结果: {}", videoCopy);
        String seedancePrompt = douBaoVisionUtil.buildSeedancePromptFromCopy(videoCopy);
        log.info("Seedance 生成提示词: {}", seedancePrompt);
        String generatedVideoUrl = douBaoVisionUtil.generateVideoBySeedance(imageAnalysisRecordModel.getImageUrl(), seedancePrompt);
        log.info("生成视频完成, generatedVideoUrl={}", generatedVideoUrl);
        return generatedVideoUrl;
    }

}