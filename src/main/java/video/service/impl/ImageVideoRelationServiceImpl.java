package video.service.impl;

import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;

import video.domain.model.ImageVideoRelationModel;
import video.mapper.ImageVideoRelationMapper;
import video.service.ImageVideoRelationService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 图片-视频关联服务实现
 * <p>
 * 处理图片与抖音视频关联关系的业务逻辑
 * </p >
 */
@Slf4j
@Service
public class ImageVideoRelationServiceImpl implements ImageVideoRelationService {

    @Resource
    private ImageVideoRelationMapper imageVideoRelationMapper;

    @Override
    public List<ImageVideoRelationModel> batchSave(List<ImageVideoRelationModel> relations) {
        if (CollectionUtils.isEmpty(relations)) {
            return Collections.emptyList();
        }
        imageVideoRelationMapper.batchInsertRelations(relations);
        log.info("批量保存图片-视频关联成功，共{}条", relations.size());
        return relations;
    }

    @Override
    public List<ImageVideoRelationModel> queryByImageId(Long imageId) {
        if (imageId == null) {
            return Collections.emptyList();
        }

        List<ImageVideoRelationModel> relations = imageVideoRelationMapper.selectRelationsByImageId(imageId);
        if (CollectionUtils.isEmpty(relations)) {
            log.debug("图片无关联视频，imageId={}", imageId);
            return Collections.emptyList();
        }

        log.debug("查询图片关联视频成功，imageId={}，共{}条", imageId, relations.size());
        return relations;
    }

    @Override
    public List<ImageVideoRelationModel> queryByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }

        List<ImageVideoRelationModel> relations = imageVideoRelationMapper.selectByIds(ids);
        if (CollectionUtils.isEmpty(relations)) {
            log.debug("未找到对应的视频记录，ids={}", ids);
            return Collections.emptyList();
        }

        log.debug("根据ID列表查询视频成功，共{}条", relations.size());
        return relations;
    }

    @Override
    public ImageVideoRelationModel queryById(Long id) {
        return imageVideoRelationMapper.selectById(id);
    }

}