package student_player;
import java.util.AbstractMap;

import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;

public class MyTools {
    public static double getSomething() {
        return Math.random();
    }

    public static PentagoMove getWinner(PentagoBoardState boardState) {

        for(PentagoMove move : boardState.getAllLegalMoves()) {
            PentagoBoardState tempState = (PentagoBoardState) boardState.clone();

            tempState.processMove(move);
            if (tempState.getWinner() == boardState.getTurnPlayer()) {
                return move;
            }
        }

        return null;
    }

    public static AbstractMap.SimpleEntry<PentagoMove, Integer> minimax(PentagoBoardState boardState, 
        int depth, int maximizingPlayer, PentagoMove move, int alpha, int beta) { 

        if (depth == 0 || boardState.gameOver()) {
            return new AbstractMap.SimpleEntry<PentagoMove, Integer>(move, eval(boardState,boardState.getTurnPlayer()));
        }

        int bestScore;
        PentagoMove bestMove = null;

        if (maximizingPlayer == PentagoBoardState.WHITE) {
            for (PentagoMove currentMove : boardState.getAllLegalMoves()) {
                PentagoBoardState tempState = (PentagoBoardState) boardState.clone();
                tempState.processMove(currentMove);

                bestScore = minimax(tempState, depth-1, PentagoBoardState.BLACK, 
                currentMove, alpha, beta).getValue(); 
                if (bestScore > alpha) {
                    alpha = bestScore;
                    bestMove = currentMove;
                }
            }
            return new AbstractMap.SimpleEntry<PentagoMove, Integer>(bestMove, alpha);
        }
        else {
            for (PentagoMove currentMove : boardState.getAllLegalMoves()) {
                PentagoBoardState tempState = (PentagoBoardState) boardState.clone();
                tempState.processMove(currentMove);

                bestScore = minimax(tempState, depth-1, PentagoBoardState.WHITE, 
                currentMove, alpha, beta).getValue(); 
                if (bestScore < beta) {
                    beta = bestScore;
                    bestMove = currentMove;
                }
            }
            return new AbstractMap.SimpleEntry<PentagoMove, Integer>(bestMove, beta);
        }
    }

    public static int eval (PentagoBoardState boardState, int currentPlayer) {
        int utility = 0;

        if (currentPlayer == PentagoBoardState.BLACK) {
            utility = -utility;
        }
        return utility;
    }
}