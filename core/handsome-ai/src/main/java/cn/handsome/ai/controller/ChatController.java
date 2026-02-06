package cn.handsome.ai.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 *
 * @author luoyong
 * @date 2025/9/2
 */
@Slf4j
@RestController
@RequestMapping("/ai/chat")
@RequiredArgsConstructor
public class ChatController {
    private final OllamaChatModel ollamaChatModel;
    private final OllamaApi ollamaApi;
    private final VectorStore vectorStore;

    @GetMapping()
    public String generate(@RequestParam(value = "message") String message) {
        return ollamaChatModel.call(message);
    }

    @GetMapping("sse")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return ollamaChatModel.stream(prompt);
    }

    @GetMapping("ollama")
    public OllamaApi.ChatResponse ollamaApi(@RequestParam(value = "message") String message) {
        //从知识库检索相关信息，再将检索得到的信息同用户的输入一起构建一个prompt，最后调用ollama api
        SearchRequest searchRequest = SearchRequest.builder().query(message).topK(5).build();
        List<Document> documents = vectorStore.similaritySearch(searchRequest);
        String targetMessage;
        if (CollectionUtils.isEmpty(documents)) {
            targetMessage = String.format("用户提问：%s\n", message);
        } else {
            targetMessage = String.format("已知信息：%s\n 用户提问：%s\n",
                    documents.get(0).getText(), message);
        }
        log.info("targetMessage: {}", targetMessage);
        String model = ollamaChatModel.getDefaultOptions().getModel();
        OllamaApi.ChatRequest request = OllamaApi.ChatRequest.builder(model)
                // not streaming
                .stream(false)
                .messages(List.of(
                        OllamaApi.Message.builder(OllamaApi.Message.Role.USER)
                                .content(targetMessage)
                                .build()))
                .options(OllamaOptions.builder().temperature(0.9d).build())
                .build();

        return ollamaApi.chat(request);
    }
}