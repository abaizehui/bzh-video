package video.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import video.domain.model.ImageAnalysisRecordModel;

/**
 * 图片分析数据访问映射器
 */
@Mapper
public interface ImageAnalysisMapper {

    int insertImageAnalysis(ImageAnalysisRecordModel entity);
    
    ImageAnalysisRecordModel selectImageAnalysisById(@Param("id") Long id);
}
