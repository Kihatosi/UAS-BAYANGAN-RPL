package com.example.boardgamebuddy;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Listing 1.3: REST Controller untuk melayani request POST /ask.
 */
@RestController
public class AskController {

    private final BoardGameService boardGameService;

    public AskController(BoardGameService boardGameService) { // #1 Constructor Injection
        this.boardGameService = boardGameService;
    }

    @PostMapping(path = "/ask", produces = "application/json")
    public Answer ask(@RequestBody Question question) {
        return boardGameService.askQuestion(question); // #2 Delegasi ke service
    }
}
