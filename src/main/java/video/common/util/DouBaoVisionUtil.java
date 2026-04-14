package video.common.util;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionContentPart;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    @Value("${ark.prompt:请识别图片中的商品，只返回标准产品名称，不要任何多余内容}")
    private String prompt;

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
}
