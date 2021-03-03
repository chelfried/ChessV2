package org.chess.core;

import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;
import static org.chess.core.move.LegalMoves.getLegalMovesForSelection;

public class Selection extends GameMechanics{

    protected static Integer pieceSelected;
    protected static String movesSelectedPiece;
    protected static boolean[] legalFields;

    public static void makeSelection(int sel) {
        if (! humanPromotingWhite && ! humanPromotingBlack && ! checkmate && ! stalemate) {
            if (playerAI == 1 && ! whiteAlwaysBottom) {
                sel = Math.abs(sel - 63);
            }
            if (pieceSelected == null && playerAI != playerTurn) {
                if ((playerAI == 0 && isUpperCase(charBoard[sel])) || (playerAI == 1 && isLowerCase(charBoard[sel]))) {
                    pieceSelected = sel;
                    getLegalMovesForSelection(playerAI == 0, sel);
                }
            } else if (pieceSelected != null) {
                if (pieceSelected == sel) {
                    pieceSelected = null;
                    movesSelectedPiece = null;
                    legalFields = null;
                } else if ((isUpperCase(charBoard[pieceSelected]) && isUpperCase(charBoard[sel])) ||
                        (isLowerCase(charBoard[pieceSelected]) && isLowerCase(charBoard[sel]))) {
                    getLegalMovesForSelection(playerAI == 0, sel);
                    pieceSelected = sel;
                } else if (legalFields[sel]) {
                    moveByHuman(sel);
                }
            }
        }
    }

    public static void setMovesSelectedPiece(String movesSelectedPiece) {
        Selection.movesSelectedPiece = movesSelectedPiece;
    }
    public static Integer getPieceSelected() {
        return pieceSelected;
    }

    public static void resetSelection(){
        pieceSelected = null;
    }

    public static void setLegalFields(boolean[] legalFields) {
        Selection.legalFields = legalFields;
    }

    public static boolean[] getLegalFields() {
        return legalFields;
    }

}
