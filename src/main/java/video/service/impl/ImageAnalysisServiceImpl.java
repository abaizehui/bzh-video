package video.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import video.domain.model.ImageAnalysisRecordModel;
import video.mapper.ImageAnalysisMapper;
import video.service.ImageAnalysisService;

/**
 * 图片分析服务实现
 */
@Service
public class ImageAnalysisServiceImpl implements ImageAnalysisService {

    @Resource
    private ImageAnalysisMapper imageAnalysisMapper;

    @Override
    public Long create(ImageAnalysisRecordModel record) {
        imageAnalysisMapper.insertImageAnalysis(record);
        return record.getId();
    }

    @Override
    public ImageAnalysisRecordModel getById(Long id) {
        return imageAnalysisMapper.selectImageAnalysisById(id);
    }
}
