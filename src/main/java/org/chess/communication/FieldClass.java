package org.chess.communication;

import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;

import static org.chess.core.GameMechanics.*;
import static org.chess.core.History.getHistory;
import static org.chess.core.Selection.getLegalFields;
import static org.chess.core.Selection.getPieceSelected;
import static org.chess.core.move.LegalMoves.getLegalMovesForSelection;

public class FieldClass {

    private static String[] fieldClass = new String[64];

    public static String[] calcFieldClass() {
        Integer pieceSelected = getPieceSelected();
        char[] chessBoard = getCharBoard();
        boolean[] legalFields = getLegalFields();

        populateFieldsColors();
        populateFieldsMovement(pieceSelected, legalFields, chessBoard);
        populateFieldsSelection(pieceSelected);
        populateFieldsCheck(pieceSelected, chessBoard);
        populateFieldsLastMove();

        return fieldClass;
    }

    public static void populateFieldsColors() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    fieldClass[i * 8 + j] = "light";
                } else if (i % 2 != 0 && j % 2 != 0) {
                    fieldClass[i * 8 + j] = "light";
                } else if (i % 2 != 0) {
                    fieldClass[i * 8 + j] = "dark";
                } else {
                    fieldClass[i * 8 + j] = "dark";
                }
            }
        }
    }

    public static void populateFieldsMovement(Integer pieceSelected, boolean[] legalFields, char[] chessBoard) {
        if (pieceSelected != null) {
            getLegalMovesForSelection(getPlayerAI() == 0, pieceSelected);
            for (int i = 0; i < 64; i++) {
                if (legalFields[i]) {
                    if (fieldClass[i].equals("light")) {
                        if ((isUpperCase(chessBoard[i]) && isLowerCase(chessBoard[pieceSelected])) ||
                                (isLowerCase(chessBoard[i]) && isUpperCase(chessBoard[pieceSelected])))
                            fieldClass[i] = "takeLight";
                        else {
                            fieldClass[i] = "moveLight";
                        }
                    } else if (fieldClass[i].equals("dark")) {
                        if ((isUpperCase(chessBoard[i]) && isLowerCase(chessBoard[pieceSelected])) ||
                                (isLowerCase(chessBoard[i]) && isUpperCase(chessBoard[pieceSelected])))
                            fieldClass[i] = "takeDark";
                        else {
                            fieldClass[i] = "moveDark";
                        }
                    }
                }
            }
        }
    }

    public static void populateFieldsSelection(Integer pieceSelected) {
        if (pieceSelected != null){
            if (fieldClass[pieceSelected].equals("light")) {
                fieldClass[pieceSelected] = "selectedLight";
            } else {
                fieldClass[pieceSelected] = "selectedDark";
            }
        }
    }

    public static void populateFieldsCheck(Integer pieceSelected, char[] chessBoard) {
        for (int i = 0; i < 64; i++) {
            if ((isWhiteInCheck() && chessBoard[i] == 'K') ||
                    (isBlackInCheck() && chessBoard[i] == 'k')) {
                if (pieceSelected != null && pieceSelected == i) {
                    fieldClass[i] = "selectedCheck";
                } else {
                    fieldClass[i] = "check";
                }
            }
        }
    }

    public static void populateFieldsLastMove() {
        String history = getHistory();
        if (isHumanPromotingWhite() || isHumanPromotingBlack()) {
            String promotingMove = getPromotingMove();
            addLastMoveUtil(fieldClass, (promotingMove.charAt(0) - 48) * 8 + (promotingMove.charAt(1) - 48));
            addLastMoveUtil(fieldClass, (promotingMove.charAt(2) - 48) * 8 + (promotingMove.charAt(3) - 48));
        } else if (history != null && ! history.equals("#")) {
            addLastMoveUtil(fieldClass, (history.charAt(history.length() - 4) - 48) * 8 + (history.charAt(history.length() - 3) - 48));
            addLastMoveUtil(fieldClass, (history.charAt(history.length() - 2) - 48) * 8 + (history.charAt(history.length() - 1) - 48));
        }
        if (getPlayerAI() == 1 && ! isWhitePlayingBottom()) {
            String[] invertedFieldClass = new String[64];
            for (int i = 0; i < 64; i++) {
                invertedFieldClass[63 - i] = fieldClass[i];
            }
            fieldClass = invertedFieldClass;
        }
    }

    private static void addLastMoveUtil(String[] fieldClass, int sel) {
        switch (fieldClass[sel]) {
            case "light":
                fieldClass[sel] = "lastMoveLight";
                break;
            case "dark":
                fieldClass[sel] = "lastMoveDark";
                break;
            case "moveLight":
                fieldClass[sel] = "lastMoveMoveLight";
                break;
            case "moveDark":
                fieldClass[sel] = "lastMoveMoveDark";
                break;
            case "takeLight":
                fieldClass[sel] = "lastMoveTakeLight";
                break;
            case "takeDark":
                fieldClass[sel] = "lastMoveTakeDark";
                break;
        }
    }

}
