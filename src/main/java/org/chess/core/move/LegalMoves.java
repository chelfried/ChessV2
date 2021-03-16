package org.chess.core.move;

import java.util.ArrayList;
import java.util.List;

import static org.chess.core.GameMechanics.*;
import static org.chess.core.Selection.setLegalFields;
import static org.chess.core.Selection.setMovesSelectedPiece;
import static org.chess.core.move.MoveMaker.makeMove;
import static org.chess.core.pieces._Piece.*;

public class LegalMoves {

    public static List<Move> getLegalMoves(boolean white, long[] board) {

        List<Move> moves = getPseudoMoves(white, board);

        moves.removeIf(pseudoMove -> isCheck(white, makeMove(board, pseudoMove)));

        return moves;
    }

    public static void getLegalMovesForSelection(boolean white, int startPos) {

        List<Move> legalMoves = getLegalMoves(white, getActiveBitboard());

        boolean[] legalFields = new boolean[64];
        List<Move> movesSelectedPiece = new ArrayList<>();

        for (Move legalMove : legalMoves) {
            if (legalMove.getFrom() == startPos) {
                movesSelectedPiece.add(new Move(legalMove.getFrom(), legalMove.getDest(), legalMove.getType()));
                legalFields[legalMove.getDest()] = true;
            }
        }

        setMovesSelectedPiece(movesSelectedPiece);
        setLegalFields(legalFields);

    }

}
