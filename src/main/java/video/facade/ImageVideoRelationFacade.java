package video.facade;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import video.common.util.DouYinApiClient;
import video.domain.model.ImageAnalysisRecordModel;
import video.domain.model.ImageVideoRelationModel;
import video.domain.request.DouyinSearchV2Request;
import video.domain.response.DouyinSearchResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import video.service.ImageAnalysisService;
import video.service.ImageVideoRelationService;

/**
 * 图片-视频关联门面
 * <p>
 * 处理图片与抖音视频关联关系的查询、统计等业务逻辑
 * </p >
 */
@Slf4j
@Component
public class ImageVideoRelationFacade {

    @Resource
    private ImageAnalysisService imageAnalysisService;

    @Resource
    private ImageVideoRelationService imageVideoRelationService;

    @Resource
    private DouYinApiClient douYinApiClient;

    /**
     * 根据图片ID进行AI分析并搜索相关视频
     *
     * @param imageId 图片记录ID
     * @return 关联的视频列表
     */
    public List<ImageVideoRelationModel> searchByImage(Long imageId) {
        // 查询图片记录
        ImageAnalysisRecordModel record = imageAnalysisService.getById(imageId);
        if (record == null) {
            throw new IllegalArgumentException("图片记录不存在，imageId=" + imageId);
        }
        // 调用抖音搜索
        DouyinSearchV2Request douyinRequest = DouyinSearchV2Request.builder()
                .keyword(record.getAnalysisContent())
                .cursor(0)
                .sortType("1")
                .publishTime("7")
                .contentType("1")
                .build();
        DouyinSearchResponse searchResult = douYinApiClient.searchVideo(douyinRequest);
        // 将搜索结果落库并返回
        return saveVideoRelations(imageId, searchResult);
    }

    /**
     * 批量保存图片与视频的关联关系
     *
     * @param imageId      图片ID
     * @param searchResult 抖音搜索结果
     * @return 保存后的关联列表
     */
    private List<ImageVideoRelationModel> saveVideoRelations(Long imageId, DouyinSearchResponse searchResult) {
        List<ImageVideoRelationModel> relations = extractVideoRelations(imageId, searchResult);
        if (CollectionUtils.isEmpty(relations)) {
            return Collections.emptyList();
        }
        imageVideoRelationService.batchSave(relations);
        log.info("成功保存图片与视频的关联关系，imageId={}，共{}条视频", imageId, relations.size());
        return relations;
    }

    /**
     * 从搜索结果中提取视频关联信息
     */
    private List<ImageVideoRelationModel> extractVideoRelations(Long imageId, DouyinSearchResponse searchResult) {
        if (searchResult == null || CollectionUtils.isEmpty(searchResult.getBusinessData())) {
            return Collections.emptyList();
        }

        return searchResult.getBusinessData().stream()
                .map(DouyinSearchResponse.BusinessData::getData)
                .filter(Objects::nonNull)
                .map(DouyinSearchResponse.DataDetail::getAwemeInfo)
                .filter(Objects::nonNull)
                .map(awemeInfo -> buildRelation(imageId, awemeInfo))
                .collect(Collectors.toList());
    }

    /**
     * 构建单条关联记录
     */
    private ImageVideoRelationModel buildRelation(Long imageId, DouyinSearchResponse.AwemeInfo awemeInfo) {
        DouyinSearchResponse.Video video = awemeInfo.getVideo();
        DouyinSearchResponse.Statistics stats = awemeInfo.getStatistics();

        return ImageVideoRelationModel.builder()
                .imageId(imageId)
                .videoId(awemeInfo.getAwemeId())
                .videoDesc(awemeInfo.getDesc())
                .videoCoverUrl(extractFirstUrl(video, v -> v.getCover().getUrlList()))
                .videoPlayUrl(extractFirstUrl(video, v -> v.getPlayAddr().getUrlList()))
                .diggCount(getOrZero(stats, DouyinSearchResponse.Statistics::getDiggCount))
                .commentCount(getOrZero(stats, DouyinSearchResponse.Statistics::getCommentCount))
                .shareCount(getOrZero(stats, DouyinSearchResponse.Statistics::getShareCount))
                .collectCount(getOrZero(stats, DouyinSearchResponse.Statistics::getCollectCount))
                .duration(Optional.ofNullable(video).map(DouyinSearchResponse.Video::getDuration).orElse(null))
                .build();
    }

    /**
     * 安全提取URL列表中的第一个URL
     */
    private String extractFirstUrl(DouyinSearchResponse.Video video,
                                   java.util.function.Function<DouyinSearchResponse.Video,
                                           List<String>> urlExtractor) {
        return Optional.ofNullable(video)
                .flatMap(v -> Optional.ofNullable(urlExtractor.apply(v)))
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0))
                .orElse(null);
    }

    /**
     * 安全获取统计数值，默认为0
     */
    private Integer getOrZero(DouyinSearchResponse.Statistics stats,
                              java.util.function.Function<DouyinSearchResponse.Statistics, Integer> getter) {
        return Optional.ofNullable(stats).map(getter).orElse(0);
    }

    /**
     * 根据图片ID查询关联的视频列表
     * <p>
     * 按点赞数降序排序，返回该图片关联的所有抖音视频
     * </p >
     *
     * @param imageId 图片记录ID
     * @return 视频关联列表，无关联时返回空列表
     */
    public List<ImageVideoRelationModel> queryVideosByImageId(Long imageId) {
        if (imageId == null) {
            return Collections.emptyList();
        }
        List<ImageVideoRelationModel> relations = imageVideoRelationService.queryByImageId(imageId);
        if (CollectionUtils.isEmpty(relations)) {
            return Collections.emptyList();
        }
        return relations;
    }


}