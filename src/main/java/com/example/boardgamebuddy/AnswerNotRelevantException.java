package com.example.boardgamebuddy;

/**
 * Chapter 2 Bagian 2.3: Exception yang dilempar ketika jawaban LLM dinilai tidak relevan.
 */
public class AnswerNotRelevantException extends RuntimeException {

    public AnswerNotRelevantException(String question, String answer) {
        super("The answer '" + answer + "' is not relevant to the question '" + question + "'.");
    }
}
