package com.example.boardgamebuddy;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Listing 1.2: Implementasi BoardGameService menggunakan Spring AI ChatClient.
 */
@Service
@Primary
public class SpringAiBoardGameService implements BoardGameService {

    private final ChatClient chatClient;

    public SpringAiBoardGameService(ChatClient.Builder chatClientBuilder) { // #1 Constructor Injection
        this.chatClient = chatClientBuilder.build(); // #2 Inisialisasi ChatClient
    }

    @Override
    public Answer askQuestion(Question question) {
        var answerText = chatClient.prompt()
                .user(question.question()) // #3 Menentukan user prompt
                .call()
                .content();
        return new Answer(answerText);
    }
}
