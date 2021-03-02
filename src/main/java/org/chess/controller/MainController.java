package org.chess.controller;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.chess.communication.FieldClass.calcFieldClass;
import static org.chess.communication.FieldPiece.getPieces;
import static org.chess.communication.GameMessage.getMessage;
import static org.chess.controller.SSEController.refreshPage;
import static org.chess.core.GameMechanics.*;
import static org.chess.core.Selection.makeSelection;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class MainController {

    @PostMapping("/reset")
    public void resetGameBoard() {
        resetGame();
        refreshPage();
    }

    @PostMapping("/white")
    public void startAsWhite() {
        setGameRunning(true);
        setPlayerAI(0);
        initiateChess();
    }

    @PostMapping("/black")
    public void startAsBlack() {
        setGameRunning(true);
        setPlayerAI(1);
        initiateChess();
    }

    @PostMapping("/{sel}")
    public void selectPiece(@PathVariable int sel) {
        makeSelection(sel);
        refreshPage();
    }

    @GetMapping("/board")
    public static String[] getGamePieces() {
        return getPieces();
    }

    @GetMapping("/gameStarted")
    static boolean checkIfGameStarted() {
        return isGameRunning();
    }

    @GetMapping("/playingBlack")
    static boolean checkIfPlayingBlack() {
        return getPlayerAI() == 1;
    }

    @GetMapping("/gameMessage")
    static String getGameMessage() {
        return JSONObject.quote(getMessage());
    }

    @GetMapping("/fieldClass")
    static String[] getFieldClasses() {
        return calcFieldClass();
    }

    @GetMapping("/whitePromoting")
    static boolean checkForPromotionWhite() {
        return isHumanPromotingWhite();
    }

    @GetMapping("/blackPromoting")
    static boolean checkForPromotionBlack() {
        return isHumanPromotingBlack();
    }

    @PostMapping("/promote/{piece}")
    public void promotePiece(@PathVariable String piece) {
        promote(piece);
        refreshPage();
    }

    private MainController() throws IOException {
    }


}