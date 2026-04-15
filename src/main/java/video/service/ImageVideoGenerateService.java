package video.service;

import video.domain.model.ImageVideoGenerateModel;

import java.util.List;

/**
 * 图片-视频生成服务接口
 * <p>
 * 处理图片生成视频的业务逻辑
 * </p >
 */
public interface ImageVideoGenerateService {

    /**
     * 保存单条记录
     *
     * @param entity 记录实体
     * @return 保存后的实体（包含生成的ID）
     */
    ImageVideoGenerateModel save(ImageVideoGenerateModel entity);

    /**
     * 批量保存记录
     * <p>
     * 保存后会回填生成的ID到传入的对象中
     * </p >
     *
     * @param list 记录列表
     * @return 保存后的记录列表（包含生成的ID）
     */
    List<ImageVideoGenerateModel> batchSave(List<ImageVideoGenerateModel> list);

    /**
     * 根据ID查询记录
     *
     * @param id 主键ID
     * @return 记录实体
     */
    ImageVideoGenerateModel getById(Long id);

    /**
     * 根据图片ID查询生成的视频列表
     *
     * @param imageId 图片ID
     * @return 视频URL列表
     */
    List<ImageVideoGenerateModel> listByImageId(Long imageId);

    /**
     * 根据图片ID删除记录
     *
     * @param imageId 图片ID
     * @return 删除的记录数量
     */
    int deleteByImageId(Long imageId);

    /**
     * 根据ID删除记录
     *
     * @param id 主键ID
     * @return 删除的记录数量
     */
    int deleteById(Long id);

}