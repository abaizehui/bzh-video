package video.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import video.domain.model.ImageVideoGenerateModel;

import java.util.List;

/**
 * 图片-视频生成数据访问映射器
 * <p>
 * 管理图片生成的视频URL信息
 * 对应数据库表：bzh_image_video_generate
 * </p >
 */
@Mapper
public interface ImageVideoGenerateMapper {

    /**
     * 插入单条记录
     *
     * @param entity 记录实体
     * @return 影响行数
     */
    int insert(ImageVideoGenerateModel entity);

    /**
     * 批量插入记录
     *
     * @param list 记录列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<ImageVideoGenerateModel> list);

    /**
     * 根据ID查询记录
     *
     * @param id 主键ID
     * @return 记录实体
     */
    ImageVideoGenerateModel selectById(@Param("id") Long id);

    /**
     * 根据图片ID查询生成的视频列表
     *
     * @param imageId 图片ID
     * @return 视频URL列表
     */
    List<ImageVideoGenerateModel> selectByImageId(@Param("imageId") Long imageId);

    /**
     * 根据图片ID删除记录
     *
     * @param imageId 图片ID
     * @return 影响行数
     */
    int deleteByImageId(@Param("imageId") Long imageId);

    /**
     * 根据ID删除记录
     *
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

}