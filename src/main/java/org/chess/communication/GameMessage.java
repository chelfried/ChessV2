package org.chess.communication;

import org.chess.core.GameBoard;

import static org.chess.core.GameMechanics.*;

public class GameMessage {

    public static String getMessage() {
        String message;
        if (!isGameRunning()) {
            message = "Please pick a color.";
        } else {
            if (getPlayerTurn() == 1) {
                message = "White's turn.";
            } else {
                message = "Black's turn.";
            }
        }

        if (isWhiteInCheck()) {
            message += " White in check.";
        } else if (isBlackInCheck()) {
            message += " Black in check.";
        }

        if (isHumanPromotingWhite() || isHumanPromotingBlack()) {
            message += " Choose a piece to promote to.";
        }

        if (isAiThinking()) {
            message += " AI is thinking.";
        }

        if (isCheckmate()) {
            message = "Checkmate.";
        } else if (isStalemate()){
            message = "Stalemate.";
        }

        return message;
    }

}
