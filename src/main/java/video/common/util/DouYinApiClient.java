package video.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import video.domain.request.DouyinSearchV2Request;
import video.domain.response.DouyinApiResponse;
import video.domain.response.DouyinSearchResponse;

import java.nio.charset.StandardCharsets;

/**
 * 抖音API客户端工具类
 */
@Slf4j
@Component
public class DouYinApiClient {

    @Value("${douyin.api.base-url}")
    private String baseUrl;

    @Value("${douyin.api.key}")
    private String apiKey;

    public DouyinSearchResponse searchVideo(DouyinSearchV2Request request) {
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            log.error("抖音API配置错误: baseUrl未配置");
            throw new RuntimeException("抖音API配置错误: baseUrl未配置");
        }
        if (apiKey == null || apiKey.trim().isEmpty()) {
            log.error("抖音API配置错误: apiKey未配置");
            throw new RuntimeException("抖音API配置错误: apiKey未配置");
        }

        String url = baseUrl + "/api/v1/douyin/search/fetch_video_search_v2";
        log.info("发起抖音视频搜索请求: keyword={}", request.getKeyword());

        try (CloseableHttpClient client = HttpClients.custom().disableRedirectHandling().build()) {

            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("accept", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + apiKey.trim());
            httpPost.setHeader("Content-Type", "application/json");

            String jsonBody = JsonUtils.toJson(request);
            httpPost.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

                if (statusCode != 200) {
                    log.error("抖音搜索请求失败: status={}, body={}", statusCode, responseBody);
                    throw new RuntimeException("抖音搜索请求失败: " + statusCode);
                }

                log.info("抖音API响应: {}", responseBody);

                DouyinApiResponse<DouyinSearchResponse> apiResponse = JsonUtils.fromJson(responseBody, 
                        new com.fasterxml.jackson.core.type.TypeReference<DouyinApiResponse<DouyinSearchResponse>>() {});

                if (apiResponse == null) {
                    log.error("解析抖音API响应失败");
                    throw new RuntimeException("解析抖音API响应失败");
                }

                if (apiResponse.getData() == null) {
                    log.warn("抖音搜索无数据返回，keyword={}", request.getKeyword());
                    return new DouyinSearchResponse();
                }

                return apiResponse.getData();
            }
        } catch (Exception e) {
            log.error("抖音搜索请求异常", e);
            throw new RuntimeException("抖音搜索请求异常: " + e.getMessage(), e);
        }
    }
}
