package org.chess.communication;

import static org.chess.core.GameBoard.getCharBoard;
import static org.chess.core.GameMechanics.getPlayerAI;
import static org.chess.core.GameMechanics.isWhitePlayingBottom;

public class FieldPiece {

    public static String[] getPieces() {

        String[] fieldPiece = new String[64];
        char[] chessBoard = getCharBoard();

        for (int i = 0; i < 64; i++) {
            if (chessBoard[i] == 'p') {
                fieldPiece[i] = "assets/img/PB.svg";
            } else if (chessBoard[i] == 'r') {
                fieldPiece[i] = "assets/img/RB.svg";
            } else if (chessBoard[i] == 'n') {
                fieldPiece[i] = "assets/img/NB.svg";
            } else if (chessBoard[i] == 'b') {
                fieldPiece[i] = "assets/img/BB.svg";
            } else if (chessBoard[i] == 'q') {
                fieldPiece[i] = "assets/img/QB.svg";
            } else if (chessBoard[i] == 'k') {
                fieldPiece[i] = "assets/img/KB.svg";
            } else if (chessBoard[i] == 'K') {
                fieldPiece[i] = "assets/img/KW.svg";
            } else if (chessBoard[i] == 'Q') {
                fieldPiece[i] = "assets/img/QW.svg";
            } else if (chessBoard[i] == 'B') {
                fieldPiece[i] = "assets/img/BW.svg";
            } else if (chessBoard[i] == 'N') {
                fieldPiece[i] = "assets/img/NW.svg";
            } else if (chessBoard[i] == 'R') {
                fieldPiece[i] = "assets/img/RW.svg";
            } else if (chessBoard[i] == 'P') {
                fieldPiece[i] = "assets/img/PW.svg";
            }
        }

        if (getPlayerAI() == 1 && ! isWhitePlayingBottom()) {
            String[] invertedFieldPiece = new String[64];
            for (int i = 0; i < 64; i++) {
                invertedFieldPiece[63 - i] = fieldPiece[i];
            }
            fieldPiece = invertedFieldPiece;
        }

        return fieldPiece;
    }

}
