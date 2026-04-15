package video.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import video.domain.model.ImageVideoRelationModel;

import java.util.List;

/**
 * 图片-视频关联数据访问映射器
 * <p>
 * 管理图片与抖音视频的关联关系
 * 一个图片可对应多个视频
 * 对应数据库表：bzh_image_video_relation
 * </p >
 */
@Mapper
public interface ImageVideoRelationMapper {

    /**
     * 单条插入关联记录
     *
     * @param entity 关联记录
     * @return 影响行数
     */
    int insertRelation(ImageVideoRelationModel entity);

    /**
     * 批量插入关联记录
     *
     * @param list 关联记录列表
     * @return 影响行数
     */
    int batchInsertRelations(@Param("list") List<ImageVideoRelationModel> list);

    /**
     * 根据图片ID查询关联的视频列表
     *
     * @param imageId 图片ID
     * @return 视频关联列表
     */
    List<ImageVideoRelationModel> selectRelationsByImageId(@Param("imageId") Long imageId);

    /**
     * 根据ID列表批量查询视频
     *
     * @param ids ID列表
     * @return 视频关联列表
     */
    List<ImageVideoRelationModel> selectByIds(@Param("ids") List<Long> ids);

    /**
     * 根据ID查询视频
     *
     * @param id ID
     * @return 视频关联列表
     */
    ImageVideoRelationModel selectById(@Param("id") Long id);


}