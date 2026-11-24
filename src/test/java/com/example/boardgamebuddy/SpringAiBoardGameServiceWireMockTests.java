package com.example.boardgamebuddy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Listing 1.4: Using WireMock to mock the behavior of OpenAI's API.
 */
@EnableWireMock(@ConfigureWireMock(baseUrlProperties = "openai.base.url")) // #1 WireMock config
@SpringBootTest(properties = "spring.ai.openai.base-url=${openai.base.url}") // #2 Override OpenAI URL ke WireMock
public class SpringAiBoardGameServiceWireMockTests {

    @Value("classpath:/test-openai-response.json") // #3 Canned response JSON
    Resource responseResource;

    @Autowired
    ChatClient.Builder chatClientBuilder;

    @BeforeEach
    public void setup() throws IOException {
        var cannedResponse = responseResource.getContentAsString(Charset.defaultCharset());
        var mapper = new ObjectMapper();
        var responseNode = mapper.readTree(cannedResponse);

        // #4 Stubbing endpoint /v1/chat/completions agar mengembalikan canned JSON "Paris"
        WireMock.stubFor(WireMock.post("/v1/chat/completions")
                .willReturn(ResponseDefinitionBuilder.okForJson(responseNode)));
    }

    @Test
    @DisplayName("Lab 1.4: Verifikasi askQuestion menghasilkan respons deterministik dari mock")
    public void testAskQuestion() {
        var boardGameService = new SpringAiBoardGameService(chatClientBuilder);
        var answer = boardGameService.askQuestion(new Question("What is the capital of France?")); // #5

        Assertions.assertThat(answer).isNotNull();
        Assertions.assertThat(answer.answer()).isEqualTo("Paris"); // #6 Assert jawaban deterministik
    }
}
