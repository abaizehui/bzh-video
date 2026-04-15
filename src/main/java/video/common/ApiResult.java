package video.common;

import lombok.Data;

/**
 * 统一API响应包装类
 * <p>
 * 所有接口返回的统一格式，包含状态码、消息和数据
 * </p >
 *
 * @param <T> 数据类型
 */
@Data
public class ApiResult<T> {

    /**
     * 状态码
     * <p>0 表示成功，其他表示失败</p >
     */
    private Integer code;

    /**
     * 响应消息
     * <p>成功时通常为 "success"，失败时为错误描述</p >
     */
    private String message;

    /**
     * 响应数据
     * <p>成功时返回具体数据，失败时可能为 null</p >
     */
    private T data;

    /**
     * 时间戳
     * <p>响应生成的时间戳，便于日志追踪</p >
     */
    private Long timestamp;

    public ApiResult() {
        this.timestamp = System.currentTimeMillis();
    }

    public ApiResult(Integer code, String message, T data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return ApiResult
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(0, "success", data);
    }

    /**
     * 成功响应（无数据）
     *
     * @param <T> 数据类型
     * @return ApiResult
     */
    public static <T> ApiResult<T> success() {
        return new ApiResult<>(0, "success", null);
    }

    /**
     * 失败响应
     *
     * @param code    错误码
     * @param message 错误消息
     * @param <T>     数据类型
     * @return ApiResult
     */
    public static <T> ApiResult<T> error(Integer code, String message) {
        return new ApiResult<>(code, message, null);
    }

    /**
     * 失败响应（默认错误码）
     *
     * @param message 错误消息
     * @param <T>     数据类型
     * @return ApiResult
     */
    public static <T> ApiResult<T> error(String message) {
        return new ApiResult<>(500, message, null);
    }

    /**
     * 参数错误
     *
     * @param message 错误消息
     * @param <T>     数据类型
     * @return ApiResult
     */
    public static <T> ApiResult<T> badRequest(String message) {
        return new ApiResult<>(400, message, null);
    }

    /**
     * 判断是否成功
     *
     * @return true 表示成功
     */
    public boolean isSuccess() {
        return code != null && code == 0;
    }
}