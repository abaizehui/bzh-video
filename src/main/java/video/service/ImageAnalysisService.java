package video.service;

import video.domain.model.ImageAnalysisRecordModel;

/**
 * 图片分析服务接口
 */
public interface ImageAnalysisService {

    Long create(ImageAnalysisRecordModel record);

    ImageAnalysisRecordModel getById(Long id);
}
