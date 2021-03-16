package org.chess.core.pieces;

import org.chess.core.move.Move;

import java.util.ArrayList;
import java.util.List;

public class Rook extends _Piece {

    public static List<Move> possibleR(long rook) {
        pieceType = 3;
        List<Move> pseudoMoves = new ArrayList<>();
        List<Integer> positions = getBitPositions(rook);
        for (Integer pos : positions) {
            long movement = orthogonalMoves(pos) & antiPlayerPieces;
            moveUtil(pseudoMoves, movement, pos, 0);
        }
        return pseudoMoves;
    }

}
