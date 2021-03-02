package org.chess.core;

import static org.chess.controller.SSEController.refreshPage;
import static org.chess.core.History.*;
import static org.chess.core.Selection.resetSelection;
import static org.chess.core.ai.OpeningBook.findMoveInOpeningBook;
import static org.chess.core.ai._CommAI.moveSearchAI;
import static org.chess.core.move.LegalMoves.getLegalMoves;
import static org.chess.core.move.MoveMaker.makeMove;
import static org.chess.core.pieces._Piece.isCheck;

public class GameMechanics extends GameBoard{

    protected static final boolean whiteAlwaysBottom = false;
    protected static boolean gameRunning;
    protected static int playerAI;
    protected static int playerTurn;
    protected static boolean aiThinking;
    protected static boolean whiteInCheck;
    protected static boolean blackInCheck;
    protected static boolean stalemate;
    protected static boolean checkmate;
    protected static boolean humanPromotingWhite;
    protected static boolean humanPromotingBlack;
    protected static String promotingMove;
    protected static int moveNo;

    public static void initiateChess() {
        charBoardToBitboard();
        playerTurn = 0;
        System.out.println("\n\nNEW MATCH STARTED\n\n");
        nextTurn();
    }

    public static void moveByAI() {
        String moveByAI = null;
        aiThinking = true;
        refreshPage();
        if (getHistory().length() > 4) {
            moveByAI = findMoveInOpeningBook();
        }
        if (moveByAI == null) {
            moveByAI = moveSearchAI();
        }
        if (gameRunning) {
            applyMove(moveByAI);
        } else {
            System.out.println("Match interrupted by player.");
        }
        aiThinking = false;
    }

    public static void applyMove(String move) {
        bitboard = makeMove(bitboard, move);
        moveNo++;
        System.out.printf("move %-13d %c%d%c%d\n", moveNo, move.charAt(1) + 49, Math.abs((move.charAt(0) - 48) - 8), move.charAt(3) + 49, Math.abs((move.charAt(2) - 48) - 8));
        addMoveToHistory(move);
        updateIsWhiteInCheck();
        updateIsBlackInCheck();
        updateIsCheckmate();
        updateIsStalemate();
        bitboardToCharBoard();
        nextTurn();
    }

    public static void nextTurn() {
        playerTurn = 1 - playerTurn;
        if (! checkmate && ! stalemate) {
            if (playerTurn == playerAI) {
                moveByAI();
            }
        }
    }

    public static void promote(String piece) {
        humanPromotingWhite = false;
        humanPromotingBlack = false;
        applyMove(promotingMove.concat(piece));
    }

    public static void updateIsStalemate() {
        if (! checkmate) {
            boolean whiteCannotMove = playerTurn == 0 && getLegalMoves(true, bitboard).length() == 0;
            boolean blackCannotMove = playerTurn == 1 && getLegalMoves(false, bitboard).length() == 0;
            stalemate = whiteCannotMove || blackCannotMove;
        }
    }

    public static void updateIsCheckmate() {
        boolean whiteCheckmated = whiteInCheck && getLegalMoves(true, bitboard).length() == 0;
        boolean blackCheckmated = blackInCheck && getLegalMoves(false, bitboard).length() == 0;
        checkmate = whiteCheckmated || blackCheckmated;
    }

    public static void resetGame() {
        System.out.println("MATCH ENDED");
        resetHistory();
        resetSelection();
        gameRunning = false;
        charBoard = new char[64];
        bitboard = new long[]{0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0xfL};
        humanPromotingWhite = false;
        humanPromotingBlack = false;
        whiteInCheck = false;
        blackInCheck = false;
        checkmate = false;
        stalemate = false;
        moveNo = 0;
    }

    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static long[] getActiveBitboard() {
        return bitboard;
    }

    public static void updateIsWhiteInCheck() {
        whiteInCheck = isCheck(true, bitboard);
    }

    public static void updateIsBlackInCheck() {
        blackInCheck = isCheck(false, bitboard);
    }

    public static boolean isWhiteInCheck() {
        return whiteInCheck;
    }

    public static boolean isBlackInCheck() {
        return blackInCheck;
    }

    public static boolean isCheckmate() {
        return checkmate;
    }

    public static boolean isGameRunning() {
        return gameRunning;
    }

    public static void setGameRunning(boolean gameRunning) {
        GameMechanics.gameRunning = gameRunning;
    }

    public static int getPlayerAI() {
        return playerAI;
    }

    public static void setPlayerAI(int playerAI) {
        GameMechanics.playerAI = playerAI;
    }

    public static int getPlayerTurn() {
        return playerTurn;
    }

    public static boolean isAiThinking() {
        return aiThinking;
    }

    public static void setAiThinking(boolean aiThinking) {
        GameMechanics.aiThinking = aiThinking;
    }

    public static boolean isWhitePlayingBottom() {
        return whiteAlwaysBottom;
    }

    public static boolean isHumanPromotingWhite() {
        return humanPromotingWhite;
    }

    public static boolean isHumanPromotingBlack() {
        return humanPromotingBlack;
    }

    public static String getPromotingMove() {
        return promotingMove;
    }

    public static boolean isStalemate() {
        return stalemate;
    }

}
