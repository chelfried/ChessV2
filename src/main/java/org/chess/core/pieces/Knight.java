package org.chess.core.pieces;

import org.chess.core.move.Move;

import java.util.ArrayList;
import java.util.List;

public class Knight extends _Piece {

    public static List<Move> possibleN(long knight) {
        List<Move> pseudoMoves = new ArrayList<>();
        List<Integer> positions = getBitPositions(knight);
        for (Integer position : positions) {
            long movement = knightAttack[position] & antiPlayerPieces;
            moveUtil(pseudoMoves, movement, position, 0);
        }
        return pseudoMoves;
    }

}
