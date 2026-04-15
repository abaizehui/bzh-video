package video.service;

import video.domain.model.ImageVideoRelationModel;

import java.util.List;

/**
 * 图片-视频关联服务接口
 * <p>
 * 处理图片与抖音视频关联关系的业务逻辑
 * </p >
 */
public interface ImageVideoRelationService {

    /**
     * 批量保存关联记录
     * <p>
     * 保存后会回填生成的ID到传入的对象中
     * </p >
     *
     * @param relations 关联记录列表
     * @return 保存后的记录列表（包含生成的ID）
     */
    List<ImageVideoRelationModel> batchSave(List<ImageVideoRelationModel> relations);

    /**
     * 根据图片ID查询关联的视频列表
     *
     * @param imageId 图片记录ID
     * @return 视频关联列表
     */
    List<ImageVideoRelationModel> queryByImageId(Long imageId);

    /**
     * 根据ID列表批量查询视频
     *
     * @param ids ID列表
     * @return 视频关联列表
     */
    List<ImageVideoRelationModel> queryByIds(List<Long> ids);


    /**
     * 根据ID查询视频
     *
     * @param id ID列表
     * @return 视频关联列表
     */
    ImageVideoRelationModel queryById(Long id);

}