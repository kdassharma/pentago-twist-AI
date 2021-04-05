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
            return new AbstractMap.SimpleEntry<PentagoMove, Integer>(move, eval(boardState,boardState.getTurnPlayer(), alpha, beta));
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

    public static int eval (PentagoBoardState boardState, int currentPlayer, int alpha, int beta) {
        int utility = 0;

        for (int x = 0; x<6; x++) {
            for (int y = 0; y<6; y++) {
                if (boardState.getPieceAt(x, y) == PentagoBoardState.Piece.WHITE) {
                    utility = neighbourHeuristic(boardState, x, y);
                }
            }
        }

        if (currentPlayer == PentagoBoardState.BLACK) {
            return beta - utility;
        }

        return alpha + utility;
    }

    public static int neighbourHeuristic(PentagoBoardState boardState, int x, int y) { 
        int utility = 0;
        
        if (x+1 < 6 && boardState.getPieceAt(x+1, y) == PentagoBoardState.Piece.WHITE) {
            utility +=1;
        }
        if (x-1 >= 0 && boardState.getPieceAt(x-1, y) == PentagoBoardState.Piece.WHITE) {
            utility +=1;
        }
        if (y+1 < 6 && boardState.getPieceAt(x, y+1) == PentagoBoardState.Piece.WHITE) {
            utility +=1;
        }
        if (y-1 >= 0 && boardState.getPieceAt(x, y-1) == PentagoBoardState.Piece.WHITE) {
            utility +=1;
        }
        if (x+1 < 6 && y+1 < 6 && boardState.getPieceAt(x+1, y+1) == PentagoBoardState.Piece.WHITE) {
            utility +=1;
        }
        if (x-1 >= 0 && y-1 >= 0  && boardState.getPieceAt(x-1, y-1) == PentagoBoardState.Piece.WHITE) {
            utility +=1;
        }
        if (x+1 < 6 && y-1 >= 0 && boardState.getPieceAt(x+1, y-1) == PentagoBoardState.Piece.WHITE) {
            utility +=1;
        }
        if (x-1 >= 0 && y+1 < 6 && boardState.getPieceAt(x-1, y+1) == PentagoBoardState.Piece.WHITE) {
            utility +=1;
        }
        return utility;
    }
}