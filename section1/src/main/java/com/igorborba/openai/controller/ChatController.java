package com.igorborba.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClient){
        this.chatClient = chatClient.build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message,
                       @RequestParam("llm_model") String llmModel){
        String context = "You are a senior developer. Answer like this. " + message;

        ChatClient.CallResponseSpec response = chatClient.prompt(context)
                .system(context)
                .options(ChatOptions.builder()
                        .model(validateLLMModel(llmModel))
                        .maxTokens(200)
                        .temperature(0.2)
                        .build())
                .call();
        return response.content();
    }

    private String validateLLMModel(String model){
        return switch (model) {
            case "3.5" -> "gpt-3.5-turbo";
            case "4o" -> "gpt-4o-mini";
            default -> throw new IllegalArgumentException("Invalid LLM Model selected");
        };
    }
}
