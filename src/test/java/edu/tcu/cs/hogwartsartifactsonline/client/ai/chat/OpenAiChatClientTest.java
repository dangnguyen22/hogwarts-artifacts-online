package edu.tcu.cs.hogwartsartifactsonline.client.ai.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwartsartifactsonline.client.ai.chat.dto.ChatRequest;
import edu.tcu.cs.hogwartsartifactsonline.client.ai.chat.dto.ChatResponse;
import edu.tcu.cs.hogwartsartifactsonline.client.ai.chat.dto.Choice;
import edu.tcu.cs.hogwartsartifactsonline.client.ai.chat.dto.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withUnauthorizedRequest;

@RestClientTest(OpenAiChatClient.class)
class OpenAiChatClientTest {
@Autowired
    private OpenAiChatClient openAiChatClient;

@Autowired
    private MockRestServiceServer mockServer;

@Autowired
private ObjectMapper objectMapper;

private String url = "https://api.openai.com/v1/chat/completions";

private ChatRequest chatRequest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGenerateSuccess() throws JsonProcessingException {
        ChatRequest chatRequest = new ChatRequest("gpt-4", List.of(
                new Message("system","Your task is to generate a short summary of a given json array in at most 100 words. The summary must includes the number of artifacts, each artifact's description, the owner information. Don't mention that the summary is from a given json array"),
                new Message("user","A json aray.")
        ));

        ChatResponse chatResponse = new ChatResponse(List.of(new Choice(0, new Message("assistant","The summary includes six artifacts, owned by three different wizards."))));
        this.mockServer.expect(requestTo(this.url))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Authorization",  startsWith("Bearer ")))
                .andExpect(content().json(this.objectMapper.writeValueAsString(chatRequest)))
                .andRespond(withSuccess(this.objectMapper.writeValueAsString(chatResponse), MediaType.APPLICATION_JSON));

        ChatResponse generatedResponse = this.openAiChatClient.generate(this.chatRequest);
        this.mockServer.verify();
        assertThat(generatedResponse.choices().get(0).message().content()).isEqualTo("The summary includes six artifacts, owned by three different wizards.");
    }

//    @Test
//    void testUnauthorizedRequest(){
//
//        this.mockServer.expect(requestTo(this.url))
//                .andExpect(method(HttpMethod.POST))
//                .andRespond(withUnauthorizedRequest());
//
//        Throwable thrown = catchThrowable(() -> {
//            ChatResponse generatedChatResponse = this.openAiChatClient.generate(this.chatRequest);
//        });
//
//        this.mockServer.verify();
//        assertThat(thrown).isInstanceOf(HttpClientErrorException.Unauthorized.class);
//    }


}