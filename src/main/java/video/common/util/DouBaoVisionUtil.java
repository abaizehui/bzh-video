package video.common.util;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionContentPart;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.model.content.generation.CreateContentGenerationTaskRequest;
import com.volcengine.ark.runtime.model.content.generation.CreateContentGenerationTaskResult;
import com.volcengine.ark.runtime.model.content.generation.GetContentGenerationTaskRequest;
import com.volcengine.ark.runtime.model.content.generation.GetContentGenerationTaskResponse;
import com.volcengine.ark.runtime.model.responses.constant.ResponsesConstants;
import com.volcengine.ark.runtime.model.responses.content.InputContentItemText;
import com.volcengine.ark.runtime.model.responses.content.InputContentItemVideo;
import com.volcengine.ark.runtime.model.responses.item.ItemEasyMessage;
import com.volcengine.ark.runtime.model.responses.item.MessageContent;
import com.volcengine.ark.runtime.model.responses.request.CreateResponsesRequest;
import com.volcengine.ark.runtime.model.responses.request.ResponsesInput;
import com.volcengine.ark.runtime.model.responses.response.ResponseObject;
import com.volcengine.ark.runtime.service.ArkService;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import video.domain.model.ImageVideoRelationModel;

/**
 * 豆包视觉识别工具 - 调用火山引擎ARK API识别图片
 */
@Component
@Slf4j
public class DouBaoVisionUtil {

    @Value("${ark.api-key:}")
    private String apiKey;
    @Value("${ark.base-url:https://ark.cn-beijing.volces.com/api/v3}")
    private String baseUrl;
    @Value("${ark.model:doubao-1.5-vision-pro-250328}")
    private String model;
    @Value("${ark.model-seed-20-lite:doubao-seed-2-0-lite-260215}")
    private String seedLiteModel;
    @Value("${ark.model-seedance-15-pro:ep-20260423235818-7vtw8}")
    private String seedance15ProModel;

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
     * 使用 doubao-seed-2-0-lite 对视频进行理解并输出文本
     *
     * @param videoUrl 视频地址
     * @param prompt 用户提示词
     * @return 模型输出文本
     */
    public String analyzeVideo(String videoUrl, String prompt) {
        log.info("开始调用 seed-2.0-lite 视频理解, videoUrl={}, prompt={}", videoUrl, prompt);
        ArkService service = ArkService.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .build();
        try {
            CreateResponsesRequest request = CreateResponsesRequest.builder()
                    .model(seedLiteModel)
                    .input(ResponsesInput.builder()
                            .addListItem(ItemEasyMessage.builder()
                                    .role(ResponsesConstants.MESSAGE_ROLE_USER)
                                    .content(MessageContent.builder()
                                            .addListItem(InputContentItemVideo.builder()
                                                    .videoUrl(videoUrl)
                                                    .build())
                                            .addListItem(InputContentItemText.builder()
                                                    .text(prompt)
                                                    .build())
                                            .build())
                                    .build())
                            .build())
                    .build();
            ResponseObject responseObject = service.createResponse(request);
            String result = extractAssistantText(responseObject);
            log.info("seed-2.0-lite 视频理解完成, copy={}", result);
            return result;
        } finally {
            service.shutdownExecutor();
        }
    }

    public String buildVideoPrompt(ImageVideoRelationModel relation) {
        String desc = relation.getVideoDesc();
        if (!StringUtils.hasText(desc)) {
            return "请理解这个视频内容，并输出简洁描述。";
        }
        return "请结合视频内容，围绕这段文案给出一个描述：[产品名称]放置在[简约桌面/大理石台面]，\n" +
                "缓慢360°旋转展示，产品特写，\n" +
                "高清质感，高级布光，反光自然，浅景深，\n" +
                "干净白色背景，无杂物，细节锐利\n";
    }

    public String buildSeedancePromptFromCopy(String copyText, String voiceCopy) {
        StringBuilder promptBuilder = new StringBuilder(StringUtils.hasText(copyText) ? copyText.trim() : "");
        if (StringUtils.hasText(voiceCopy)) {
            promptBuilder.append("\n请为该视频搭配中文旁白文案：").append(voiceCopy.trim());
        }
        promptBuilder.append(" --duration 10 --camerafixed false --watermark false");
        return promptBuilder.toString();
    }

    public String generateVideoBySeedance(String imageUrl, String prompt) {
        log.info("开始调用 Seedance 生成视频, model={}, imageUrl={}, prompt={}", seedance15ProModel, imageUrl, prompt);
        ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
        Dispatcher dispatcher = new Dispatcher();
        ArkService service = ArkService.builder()
                .dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .build();
        try {
            List<CreateContentGenerationTaskRequest.Content> contents = new ArrayList<>();
            contents.add(CreateContentGenerationTaskRequest.Content.builder()
                    .type("text")
                    .text(prompt)
                    .build());
            if (StringUtils.hasText(imageUrl)) {
                contents.add(CreateContentGenerationTaskRequest.Content.builder()
                        .type("image_url")
                        .imageUrl(CreateContentGenerationTaskRequest.ImageUrl.builder()
                                .url(imageUrl)
                                .build())
                        .build());
            }
            CreateContentGenerationTaskRequest createRequest = CreateContentGenerationTaskRequest.builder()
                    .model(seedance15ProModel)
                    .content(contents)
                    .build();
            CreateContentGenerationTaskResult createResult = service.createContentGenerationTask(createRequest);
            if (createResult == null || !StringUtils.hasText(createResult.getId())) {
                throw new IllegalStateException("Seedance 创建任务失败：taskId 为空");
            }
            log.info("Seedance 创建任务成功, taskId={}", createResult.getId());
            return pollSeedanceVideoUrl(service, createResult.getId());
        } finally {
            service.shutdownExecutor();
        }
    }

    private String pollSeedanceVideoUrl(ArkService service, String taskId) {
        GetContentGenerationTaskRequest getRequest = GetContentGenerationTaskRequest.builder()
                .taskId(taskId)
                .build();
        int maxRetry = 100;
        for (int i = 0; i < maxRetry; i++) {
            GetContentGenerationTaskResponse response = service.getContentGenerationTask(getRequest);
            String status = response.getStatus();
            log.info("Seedance 任务轮询, taskId={}, round={}, status={}", taskId, i + 1, status);
            if ("succeeded".equalsIgnoreCase(status)) {
                String url = extractGeneratedVideoUrl(response);
                if (StringUtils.hasText(url)) {
                    log.info("Seedance 任务完成, taskId={}, videoUrl={}", taskId, url);
                    return url;
                }
                throw new IllegalStateException("Seedance 任务成功但未解析到视频URL，taskId=" + taskId);
            }
            if ("failed".equalsIgnoreCase(status)) {
                throw new IllegalStateException("Seedance 任务失败，taskId=" + taskId + ", status=" + status);
            }
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("轮询 Seedance 任务被中断，taskId=" + taskId, exception);
            }
        }
        throw new IllegalStateException("Seedance 任务轮询超时，taskId=" + taskId);
    }

    private String extractGeneratedVideoUrl(GetContentGenerationTaskResponse response) {
        if (response == null) {
            return null;
        }
        Object contentObj = invokeGetter(response, "getContent");
        String url = extractVideoUrlFromAny(contentObj);
        if (StringUtils.hasText(url)) {
            return url;
        }
        log.warn("未从 Seedance 响应中提取到视频URL: {}", response);
        return null;
    }

    private String extractVideoUrlFromAny(Object obj) {
        if (obj == null) {
            return null;
        }
        Object videoUrl = invokeGetter(obj, "getVideoUrl");
        if (videoUrl instanceof String && StringUtils.hasText((String) videoUrl)) {
            return (String) videoUrl;
        }
        Object fileUrl = invokeGetter(obj, "getFileUrl");
        if (fileUrl instanceof String && StringUtils.hasText((String) fileUrl)) {
            return (String) fileUrl;
        }
        Object url = invokeGetter(obj, "getUrl");
        if (url instanceof String && StringUtils.hasText((String) url)) {
            return (String) url;
        }
        return null;
    }

    private String extractAssistantText(ResponseObject responseObject) {
        if (responseObject == null) {
            return "";
        }
        try {
            Method getText = responseObject.getClass().getMethod("getText");
            Object text = getText.invoke(responseObject);
            if (text instanceof String && org.springframework.util.StringUtils.hasText((String) text)) {
                return (String) text;
            }
        } catch (Exception ignore) {
            // ignore
        }
        try {
            Method getOutput = responseObject.getClass().getMethod("getOutput");
            Object output = getOutput.invoke(responseObject);
            if (!(output instanceof List)) {
                return String.valueOf(responseObject);
            }
            for (Object item : (List<?>) output) {
                String itemType = String.valueOf(invokeGetter(item, "getType"));
                if (!"message".equals(itemType)) {
                    continue;
                }
                Object content = invokeGetter(item, "getContent");
                if (!(content instanceof List)) {
                    continue;
                }
                for (Object c : (List<?>) content) {
                    String contentType = String.valueOf(invokeGetter(c, "getType"));
                    if (!"output_text".equals(contentType)) {
                        continue;
                    }
                    Object text = invokeGetter(c, "getText");
                    if (text instanceof String && org.springframework.util.StringUtils.hasText((String) text)) {
                        return (String) text;
                    }
                }
            }
        } catch (Exception ignore) {
            // ignore
        }
        return String.valueOf(responseObject);
    }

    private Object invokeGetter(Object obj, String methodName) {
        if (obj == null) {
            return null;
        }
        try {
            Method method = obj.getClass().getMethod(methodName);
            return method.invoke(obj);
        } catch (Exception exception) {
            return null;
        }
    }

}