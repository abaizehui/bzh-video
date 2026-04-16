package video.common.util;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionContentPart;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.model.content.generation.CreateContentGenerationTaskRequest;
import com.volcengine.ark.runtime.model.content.generation.CreateContentGenerationTaskResult;
import com.volcengine.ark.runtime.model.content.generation.GetContentGenerationTaskRequest;
import com.volcengine.ark.runtime.model.content.generation.GetContentGenerationTaskResponse;
import com.volcengine.ark.runtime.service.ArkService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import video.domain.model.ImageVideoRelationModel;

/**
 * 豆包视觉识别工具 - 调用火山引擎ARK API识别图片
 */
@Component
public class DouBaoVisionUtil {

    @Value("${ark.api-key:}")
    private String apiKey;
    @Value("${ark.base-url:https://ark.cn-beijing.volces.com/api/v3}")
    private String baseUrl;
    @Value("${ark.model:doubao-1.5-vision-pro-250328}")
    private String model;

    /**
     * 识别图片，返回商品名称
     * @param imageUrl 图片URL
     * @return 识别结果
     */
    public String recognizeName(String imageUrl) {
        ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
        Dispatcher dispatcher = new Dispatcher();
        ArkService service = ArkService.builder()
                .dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .build();

        ArrayList<ChatCompletionContentPart> parts = new ArrayList<>();
        parts.add(ChatCompletionContentPart.builder()
                .type("image_url")
                .imageUrl(new ChatCompletionContentPart.ChatCompletionContentPartImageURL(imageUrl))
                .build());
        String prompt = "请识别图片中的商品，只返回标准产品名称，不要任何多余内容";
        parts.add(ChatCompletionContentPart.builder().type("text").text(prompt).build());

        ChatMessage message = ChatMessage.builder()
                .role(ChatMessageRole.USER)
                .multiContent(parts)
                .build();

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(Collections.singletonList(message))
                .build();

        String content = String.join("、", service.createChatCompletion(request)
                .getChoices()
                .stream()
                .map(choice -> String.valueOf(choice.getMessage().getContent()))
                .toArray(String[]::new));
        service.shutdownExecutor();
        return content;
    }

    /**
     * 识别图片，返回商品名称
     * @param imageUrl 图片URL
     * @return 识别结果
     */
    public String recognizeContent(String imageUrl) {
        ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
        Dispatcher dispatcher = new Dispatcher();
        ArkService service = ArkService.builder()
                .dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .build();

        ArrayList<ChatCompletionContentPart> parts = new ArrayList<>();
        parts.add(ChatCompletionContentPart.builder()
                .type("image_url")
                .imageUrl(new ChatCompletionContentPart.ChatCompletionContentPartImageURL(imageUrl))
                .build());
        String prompt = "解析图片内容、风格、主体特征";
        parts.add(ChatCompletionContentPart.builder().type("text").text(prompt).build());

        ChatMessage message = ChatMessage.builder()
                .role(ChatMessageRole.USER)
                .multiContent(parts)
                .build();

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(Collections.singletonList(message))
                .build();

        String content = String.join("、", service.createChatCompletion(request)
                .getChoices()
                .stream()
                .map(choice -> String.valueOf(choice.getMessage().getContent()))
                .toArray(String[]::new));
        service.shutdownExecutor();
        return content;
    }


    /**
     * 根据图片URL创建图生视频任务
     *
     * @param relations relations
     * @return 任务结果，包含生成的视频URL
     */
    public String generateVideoFromImage(List<ImageVideoRelationModel> relations) {
        ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
        Dispatcher dispatcher = new Dispatcher();
        ArkService service = ArkService.builder()
                .dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .apiKey(apiKey)
                .build();
        List<CreateContentGenerationTaskRequest.Content> contents = new ArrayList<>();
        // 图生视频功能
        // 文本提示词与参数组合
        contents.add(CreateContentGenerationTaskRequest.Content.builder()
                .type("text")
                .text("无人机以极快速度穿越复杂障碍或自然奇观，带来沉浸式飞行体验  --resolution 1080p  --duration 5 --camerafixed false --watermark true")
                .build());

        relations.forEach(relation -> {
            // 首帧图片 (若仅需使用文本生成视频功能，可将此部分内容进行注释处理。)
            contents.add(CreateContentGenerationTaskRequest.Content.builder()
                    .type("image_url")
                    .imageUrl(CreateContentGenerationTaskRequest.ImageUrl.builder()
                            .url(relation.getVideoCoverUrl()) // 请上传可以访问的图片URL
                            .build())
                    .build());
        });


        // 创建视频生成任务
        CreateContentGenerationTaskRequest createRequest = CreateContentGenerationTaskRequest.builder()
                .model(model)
                .content(contents)
                .build();

        CreateContentGenerationTaskResult createResult = service.createContentGenerationTask(createRequest);
        System.out.println(createResult);
        // 获取任务详情
        String taskId = createResult.getId();
        GetContentGenerationTaskRequest getRequest = GetContentGenerationTaskRequest.builder()
                .taskId(taskId)
                .build();
        // 轮询查询部分
        System.out.println("----- polling task status -----");
        while (true) {
            try {
                GetContentGenerationTaskResponse getResponse = service.getContentGenerationTask(getRequest);
                String status = getResponse.getStatus();
                if ("succeeded".equalsIgnoreCase(status)) {
                    System.out.println("----- task succeeded -----");
                    System.out.println(getResponse);
                    break;
                } else if ("failed".equalsIgnoreCase(status)) {
                    System.out.println("----- task failed -----");
                    System.out.println("Error: " + getResponse.getStatus());
                    break;
                } else {
                    System.out.printf("Current status: %s, Retrying in 3 seconds...", status);
                    TimeUnit.SECONDS.sleep(3);
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                System.err.println("Polling interrupted");
                break;
            }
        }
        // 轮询获取任务结果
        return null;
    }

}