package student_player;
import java.util.AbstractMap;

import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;
import boardgame.Board;

public class MyTools {

    public long startTime;

    public MyTools(long startTime) {
        this.startTime = startTime;
    }

    public PentagoMove openingStrategy(PentagoBoardState boardState){
            // Do turns numbers matter? (boardState.firstPlayer() == boardState.getTurnPlayer())
            // Would the strategy matter based off first or second move
            if (boardState.isLegal(new PentagoMove(1,1,0,0,boardState.getTurnPlayer()))) {
                return new PentagoMove(1,1,0,0,boardState.getTurnPlayer());
            }
            if (boardState.isLegal(new PentagoMove(1,4,0,0,boardState.getTurnPlayer()))) {
                return new PentagoMove(1,4,0,0,boardState.getTurnPlayer());
            }
            if (boardState.isLegal(new PentagoMove(4,1,0,0,boardState.getTurnPlayer()))) {
                return new PentagoMove(4,1,0,0,boardState.getTurnPlayer());
            }
            if (boardState.isLegal(new PentagoMove(4,4,0,0,boardState.getTurnPlayer()))) {
                return new PentagoMove(4,4,0,0,boardState.getTurnPlayer());
            }
            return (PentagoMove) boardState.getRandomMove();
    }
    
    /** 
     * Checks all legal moves to find the first winning move
     * @param boardState State of the board
     * @return PentagoMove winning move or null if none found
     */
    public PentagoMove getWinner(PentagoBoardState boardState) {

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
    public AbstractMap.SimpleEntry<PentagoMove, Integer> minimax(PentagoBoardState boardState, 
        int depth, int maximizingPlayer, PentagoMove move, int alpha, int beta) { 

        if (depth == 0 || boardState.getWinner() != Board.NOBODY || (System.nanoTime() - this.startTime > 2.5*Math.pow(10, 9))) {
            return new AbstractMap.SimpleEntry<PentagoMove, Integer>(move, eval(boardState,maximizingPlayer, alpha, beta));
        }

        // if (depth == 0 || boardState.getWinner() != Board.NOBODY) {
        //     return new AbstractMap.SimpleEntry<PentagoMove, Integer>(move, eval(boardState,maximizingPlayer, alpha, beta));
        // }

        int bestScore;
        PentagoMove bestMove = (PentagoMove) boardState.getRandomMove(); // Check wether this should just be move
        // PentagoMove bestMove = null;

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
                if (beta <= alpha) {
                    break;
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
                if (beta <= alpha) {
                    break;
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
    public int eval (PentagoBoardState boardState, int currentPlayer, int alpha, int beta) {
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
     * Simple heuristic which counts all adjacent (horizontal, vertical, diagonal) pieces 
     * @param boardState
     * @param x
     * @param y
     * @return int Utility
     */
    public int neighbourHeuristic(PentagoBoardState boardState, int x, int y) { 
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

    // public ArrayList<PentagoMove> cutMoves(PentagoBoardState boardState){
    //     ArrayList<PentagoMove> moves = boardState.getAllLegalMoves();

    //     for (PentagoMove move : moves) {
    //         PentagoCoord coord = move.getMoveCoord();
    //         if (!checkNeighbours(boardState, coord.getX(), coord.getY())) {
    //             moves.remove(move);
    //         }
    //     }
        
    //     return moves;
    // }

    // public boolean checkNeighbours(PentagoBoardState boardState, int x, int y) {

    //     if (x+1 < 6 && boardState.getPieceAt(x+1, y) == PentagoBoardState.Piece.WHITE) {
    //         return true;
    //     }
    //     if (x-1 >= 0 && boardState.getPieceAt(x-1, y) == PentagoBoardState.Piece.WHITE) {
    //         return true;
    //     }
    //     if (y+1 < 6 && boardState.getPieceAt(x, y+1) == PentagoBoardState.Piece.WHITE) {
    //         return true;
    //     }
    //     if (y-1 >= 0 && boardState.getPieceAt(x, y-1) == PentagoBoardState.Piece.WHITE) {
    //         return true;
    //     }
    //     if (x+1 < 6 && y+1 < 6 && boardState.getPieceAt(x+1, y+1) == PentagoBoardState.Piece.WHITE) {
    //         return true;
    //     }
    //     if (x-1 >= 0 && y-1 >= 0  && boardState.getPieceAt(x-1, y-1) == PentagoBoardState.Piece.WHITE) {
    //         return true;
    //     }
    //     if (x+1 < 6 && y-1 >= 0 && boardState.getPieceAt(x+1, y-1) == PentagoBoardState.Piece.WHITE) {
    //         return true;
    //     }
    //     if (x-1 >= 0 && y+1 < 6 && boardState.getPieceAt(x-1, y+1) == PentagoBoardState.Piece.WHITE) {
    //         return true;
    //     }
        
    //     return false;
    // }
}