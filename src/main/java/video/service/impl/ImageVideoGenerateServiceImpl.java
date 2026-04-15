package video.service.impl;

import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;

import video.domain.model.ImageVideoGenerateModel;
import video.mapper.ImageVideoGenerateMapper;
import video.service.ImageVideoGenerateService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 图片-视频生成服务实现
 * <p>
 * 处理图片生成视频的业务逻辑
 * </p >
 */
@Slf4j
@Service
public class ImageVideoGenerateServiceImpl implements ImageVideoGenerateService {

    @Resource
    private ImageVideoGenerateMapper imageVideoGenerateMapper;

    @Override
    public ImageVideoGenerateModel save(ImageVideoGenerateModel entity) {
        if (entity == null) {
            return null;
        }
        imageVideoGenerateMapper.insert(entity);
        log.info("保存图片-视频生成记录成功，id={}", entity.getId());
        return entity;
    }

    @Override
    public List<ImageVideoGenerateModel> batchSave(List<ImageVideoGenerateModel> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        imageVideoGenerateMapper.batchInsert(list);
        log.info("批量保存图片-视频生成记录成功，共{}条", list.size());
        return list;
    }

    @Override
    public ImageVideoGenerateModel getById(Long id) {
        if (id == null) {
            return null;
        }
        return imageVideoGenerateMapper.selectById(id);
    }

    @Override
    public List<ImageVideoGenerateModel> listByImageId(Long imageId) {
        if (imageId == null) {
            return Collections.emptyList();
        }
        List<ImageVideoGenerateModel> list = imageVideoGenerateMapper.selectByImageId(imageId);
        if (CollectionUtils.isEmpty(list)) {
            log.debug("图片无生成视频记录，imageId={}", imageId);
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public int deleteByImageId(Long imageId) {
        if (imageId == null) {
            return 0;
        }
        int count = imageVideoGenerateMapper.deleteByImageId(imageId);
        log.info("删除图片生成视频记录成功，imageId={}，删除{}条", imageId, count);
        return count;
    }

    @Override
    public int deleteById(Long id) {
        if (id == null) {
            return 0;
        }
        int count = imageVideoGenerateMapper.deleteById(id);
        log.info("删除视频生成记录成功，id={}", id);
        return count;
    }

}