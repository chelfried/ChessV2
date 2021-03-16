package org.chess.core.pieces;

import org.chess.core.move.Move;

import java.util.ArrayList;
import java.util.List;

public class Queen extends _Piece {

    public static List<Move> possibleQ(long queen) {
        pieceType = 4;
        List<Move> pseudoMoves = new ArrayList<>();
        List<Integer> positions = getBitPositions(queen);
        for (Integer pos : positions) {
            long movement = (orthogonalMoves(pos) | diagonalMoves(pos)) & antiPlayerPieces;
            moveUtil(pseudoMoves, movement, pos, 0);
        }
        return pseudoMoves;
    }

}
