package org.chess.core.move;

import static org.chess.core.GameMechanics.*;
import static org.chess.core.Selection.setLegalFields;
import static org.chess.core.Selection.setMovesSelectedPiece;
import static org.chess.core.move.MoveMaker.makeMove;
import static org.chess.core.pieces._Piece.*;

public class LegalMoves {

    public static String getLegalMoves(boolean white, long[] board) {

        String movesPseudo = getPseudoMoves(white, board);
        StringBuilder legalMoves = new StringBuilder();

        for (int i = 0; i < movesPseudo.length(); i += 5) {

            String move = movesPseudo.substring(i, i + 5);

            if (! isCheck(white, makeMove(board, move))) {
                legalMoves.append(move);
            }

        }

        return legalMoves.toString();
    }

    public static void getLegalMovesForSelection(boolean white, int startPos) {

        String legalMoves = getLegalMoves(white, getActiveBitboard());

        boolean[] legalFields = new boolean[64];
        StringBuilder movesSelectedPiece = new StringBuilder();

        for (int i = 0; i < legalMoves.length(); i += 5) {
            String move = legalMoves.substring(i, i + 5);
            if (move.charAt(0) - 48 == startPos / 8 && move.charAt(1) - 48 == startPos % 8) {
                movesSelectedPiece.append(move);
                legalFields[(move.charAt(2) - 48) * 8 + (move.charAt(3) - 48)] = true;
            }
        }

        setMovesSelectedPiece(movesSelectedPiece.toString());
        setLegalFields(legalFields);

    }

}
