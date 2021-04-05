package student_player;
import java.util.AbstractMap;

import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;
import boardgame.Board;

public class MyTools {
    
    /** 
     * Checks all legal moves to find the first winning move
     * @param boardState State of the board
     * @return PentagoMove winning move or null if none found
     */
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

    
    /** 
     * Minimax algorithm with white as the maximizingPlayer
     * @param boardState State of the board
     * @param depth How many levels of nodes to traverse
     * @param maximizingPlayer 
     * @param move
     * @param alpha 
     * @param beta
     * @return SimpleEntry<PentagoMove, Integer> Represents a <move, score for that move>
     */
    public static AbstractMap.SimpleEntry<PentagoMove, Integer> minimax(PentagoBoardState boardState, 
        int depth, int maximizingPlayer, PentagoMove move, int alpha, int beta) { 

        if (depth == 0 || boardState.gameOver()) {
            return new AbstractMap.SimpleEntry<PentagoMove, Integer>(move, eval(boardState,maximizingPlayer, alpha, beta));
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

    
    /** 
     * Evaluation function for a given state
     * @param boardState
     * @param currentPlayer
     * @param alpha
     * @param beta
     * @return int Utility of a given position
     */
    public static int eval (PentagoBoardState boardState, int currentPlayer, int alpha, int beta) {
        int utility = 0;

        if (boardState.getWinner() == Board.NOBODY) {
            for (int x = 0; x<6; x++) {
                for (int y = 0; y<6; y++) {
                    if (boardState.getPieceAt(x, y) == PentagoBoardState.Piece.WHITE) {
                        utility += neighbourHeuristic(boardState, x, y);
                    }
                }
            }
    
            if (currentPlayer == PentagoBoardState.WHITE) {
                utility = alpha + utility;
            }
            else { 
                utility = beta - utility;
            }
        }
        else { 
            if (boardState.getWinner() == PentagoBoardState.WHITE) {
                utility = Integer.MAX_VALUE;
            }
            else {
                utility = Integer.MIN_VALUE;
            }
        }


        return utility;
    }

    
    /** 
     * Simple heuristic which counts all adjacent (horizontal, vertical, diagonal) pieces of the same
     * colour
     * @param boardState
     * @param x
     * @param y
     * @return int Utility
     */
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