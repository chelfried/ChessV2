package org.chess.core.pieces;

import org.chess.core.move.Move;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends _Piece {

    public static List<Move> possibleB(long bishop) {
        pieceType = 2;
        List<Move> pseudoMoves = new ArrayList<>();
        List<Integer> positions = getBitPositions(bishop);
        for (Integer position : positions) {
            long movement = diagonalMoves(position) & antiPlayerPieces;
            moveUtil(pseudoMoves, movement, position, 0);
        }
        return pseudoMoves;
    }

}
