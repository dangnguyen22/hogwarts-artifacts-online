package edu.tcu.cs.hogwartsartifactsonline.client.ai.chat;

import edu.tcu.cs.hogwartsartifactsonline.client.ai.chat.dto.ChatRequest;
import edu.tcu.cs.hogwartsartifactsonline.client.ai.chat.dto.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OpenAiChatClient implements ChatClient{

    private final RestClient restClient;

    public OpenAiChatClient(@Value("${ai.openai.api-key}") String apiKey,
                            @Value("${ai.openai.endpoint}") String endpoint,
            RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(endpoint)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
        System.out.println("API Key: " + apiKey);
    }

    @Override
    public ChatResponse generate(ChatRequest request) {
        return this.restClient
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(ChatResponse.class)
                ;
    }
}
