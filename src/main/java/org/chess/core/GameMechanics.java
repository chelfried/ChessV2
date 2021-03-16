package org.chess.core;

import org.chess.core.move.Move;

import static org.chess.controller.SSEController.refreshPage;
import static org.chess.core.History.*;
import static org.chess.core.Selection.*;
import static org.chess.core.ai.OpeningBook.findMoveInOpeningBook;
import static org.chess.core.ai._CommAI.moveSearchAI;
import static org.chess.core.move.LegalMoves.getLegalMoves;
import static org.chess.core.move.MoveMaker.makeMove;
import static org.chess.core.pieces._Piece.isCheck;

public class GameMechanics extends GameBoard {

    protected static final boolean whiteAlwaysBottom = true;
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
    protected static Move promotingMove;
    protected static int moveNo;

    public static void initiateChess() {
        charBoardToBitboard();
        playerTurn = 0;
        System.out.println("\n\nNEW MATCH STARTED\n\n");
        nextTurn();
    }

    public static void moveByHuman(int sel) {
        Move selectedMove = null;
        for (Move move : movesSelectedPiece) {
            if (move.getFrom() == pieceSelected && move.getDest() == sel) {
                selectedMove = new Move(pieceSelected, sel, move.getType());
                if (move.getType() >= 11 && move.getType() <= 14) {
                    humanPromotingWhite = true;
                    pieceSelected = null;
                    promotingMove = move;
                    break;
                }
                if (move.getType() >= 21 && move.getType() <= 24) {
                    humanPromotingBlack = true;
                    pieceSelected = null;
                    promotingMove = move;
                    break;
                }
            }
        }
        if (! humanPromotingWhite && ! humanPromotingBlack) {
            pieceSelected = null;
            movesSelectedPiece = null;
            legalFields = null;
            applyMove(selectedMove);
        }
    }


    public static void moveByAI() {
        Move moveByAI = null;
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
            System.out.println("Match was interrupted by player.");
        }
        aiThinking = false;
    }

    public static void applyMove(Move move) {
        bitboard = makeMove(bitboard, move);
        moveNo++;
        System.out.printf(
                "move %-13d %c%d%c%d\n", moveNo,
                (char) (move.getFrom() % 8 + 97),
                Math.abs(move.getFrom() / 8 - 8),
                (char) (move.getDest() % 8 + 97),
                Math.abs(move.getDest() / 8 - 8)
        );
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

    public static void promote(int piece) {
        humanPromotingWhite = false;
        humanPromotingBlack = false;
        applyMove(new Move(promotingMove.getFrom(), promotingMove.getDest(), piece));
    }

    public static void updateIsStalemate() {
        if (! checkmate) {
            boolean whiteCannotMove = playerTurn == 0 && getLegalMoves(true, bitboard).size() == 0;
            boolean blackCannotMove = playerTurn == 1 && getLegalMoves(false, bitboard).size() == 0;
            stalemate = whiteCannotMove || blackCannotMove;
        }
    }

    public static void updateIsCheckmate() {
        boolean whiteCheckmated = whiteInCheck && getLegalMoves(true, bitboard).size() == 0;
        boolean blackCheckmated = blackInCheck && getLegalMoves(false, bitboard).size() == 0;
        checkmate = whiteCheckmated || blackCheckmated;
    }

    public static void resetGame() {
        System.out.println("MATCH ENDED");
        resetHistory();
        resetSelection();
        gameRunning = false;
        aiThinking = false;
        charBoard = new char[64];
        bitboard = new long[14];
        bitboard[13] = 0xfL; // resets all castling rights
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

    public static Move getPromotingMove() {
        return promotingMove;
    }

    public static boolean isStalemate() {
        return stalemate;
    }

}
