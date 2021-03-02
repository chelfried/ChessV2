package org.chess.core;

public class PlayerActions extends Selection{

    public static void moveByHuman(int sel) {
        StringBuilder selectedMove = new StringBuilder();
        selectedMove.append(pieceSelected / 8).append(pieceSelected % 8).append(sel / 8).append(sel % 8);
        for (int i = 0; i < movesSelectedPiece.length(); i += 5) {
            String move = movesSelectedPiece.substring(i, i + 5);
            if (selectedMove.charAt(0) == move.charAt(0) &&
                    selectedMove.charAt(1) == move.charAt(1) &&
                    selectedMove.charAt(2) == move.charAt(2) &&
                    selectedMove.charAt(3) == move.charAt(3)) {
                if (move.charAt(4) == 'Q' || move.charAt(4) == 'R' || move.charAt(4) == 'B' || move.charAt(4) == 'N') {
                    humanPromotingWhite = true;
                    pieceSelected = null;
                    promotingMove = move.substring(0, 4);
                    break;
                } else if (move.charAt(4) == 'q' || move.charAt(4) == 'r' || move.charAt(4) == 'b' || move.charAt(4) == 'n') {
                    humanPromotingBlack = true;
                    pieceSelected = null;
                    promotingMove = move.substring(0, 4);
                    break;
                }
                pieceSelected = null;
                movesSelectedPiece = null;
                legalFields = null;
                applyMove(move);
                break;
            }
        }
    }

}
