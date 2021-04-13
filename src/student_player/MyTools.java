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

    public PentagoMove openingStrategy(PentagoBoardState boardState, int player){
        PentagoMove move = null;

        if (boardState.isLegal(new PentagoMove(1,1,0,0,boardState.getTurnPlayer()))) {
            move = new PentagoMove(1,1,0,0,boardState.getTurnPlayer());
        }
        else if (boardState.isLegal(new PentagoMove(1,4,0,0,boardState.getTurnPlayer()))) {
            move = new PentagoMove(1,4,0,0,boardState.getTurnPlayer());
        }
        else if (boardState.isLegal(new PentagoMove(4,1,0,0,boardState.getTurnPlayer()))) {
            move = new PentagoMove(4,1,0,0,boardState.getTurnPlayer());
        }
        else if (boardState.isLegal(new PentagoMove(4,4,0,0,boardState.getTurnPlayer()))) {
            move = new PentagoMove(4,4,0,0,boardState.getTurnPlayer());
        }
        if (move != null) {
            PentagoBoardState tempState = (PentagoBoardState) boardState.clone();
            tempState.processMove(move);

            for (PentagoMove oppMove:tempState.getAllLegalMoves()) {
                PentagoBoardState oppState = (PentagoBoardState) tempState.clone();
                oppState.processMove(oppMove);
                if (oppState.getWinner() == 1 - player) {
                    return null;
                }
            }
        }

        return move;
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

        if (boardState.getTurnNumber() <= 4) {
            PentagoMove openingMove = openingStrategy(boardState, boardState.getTurnPlayer());
            if (openingMove != null) {
                return new AbstractMap.SimpleEntry<PentagoMove, Integer>(openingMove, 0);
            }
        }

        if (depth == 0 || boardState.getWinner() != Board.NOBODY || (System.nanoTime() - this.startTime > 1.95*Math.pow(10, 9))) {
            return new AbstractMap.SimpleEntry<PentagoMove, Integer>(move, eval(boardState,maximizingPlayer, alpha, beta));
        }
        
        int bestScore;
        PentagoMove bestMove = move; 

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
                        // utility += checkDiagonals(boardState, x, y);
                        // utility += checkVerticals(boardState, x, y);
                        // utility += checkHorizontals(boardState, x, y);
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
            utility +=2;
        }
        if (x-1 >= 0 && boardState.getPieceAt(x-1, y) == PentagoBoardState.Piece.WHITE) {
            utility +=2;
        }
        if (y+1 < 6 && boardState.getPieceAt(x, y+1) == PentagoBoardState.Piece.WHITE) {
            utility +=2;
        }
        if (y-1 >= 0 && boardState.getPieceAt(x, y-1) == PentagoBoardState.Piece.WHITE) {
            utility +=2;
        }
        if (x+1 < 6 && y+1 < 6 && boardState.getPieceAt(x+1, y+1) == PentagoBoardState.Piece.WHITE) {
            utility +=2;
        }
        if (x-1 >= 0 && y-1 >= 0  && boardState.getPieceAt(x-1, y-1) == PentagoBoardState.Piece.WHITE) {
            utility +=2;
        }
        if (x+1 < 6 && y-1 >= 0 && boardState.getPieceAt(x+1, y-1) == PentagoBoardState.Piece.WHITE) {
            utility +=2;
        }
        if (x-1 >= 0 && y+1 < 6 && boardState.getPieceAt(x-1, y+1) == PentagoBoardState.Piece.WHITE) {
            utility +=2;
        }
        return utility;
    }

    
    public int checkVerticals(PentagoBoardState boardState, int x, int y) {
        int utility = 0;

        // Vetical down
        for (int i = y+1; i<6; i++) {
            if (boardState.getPieceAt(x, i) != PentagoBoardState.Piece.WHITE) { 
                break;
            }
            utility += 1; 
        }

        // Vertical up
        for (int i = y-1; i>=0; i--) {
            if (boardState.getPieceAt(x, i) != PentagoBoardState.Piece.WHITE) {
                break;
            }
            utility += 1; 
        }
        
        return utility; 
    }

    public int checkHorizontals(PentagoBoardState boardState, int x, int y) {
        int utility = 0;

        // Horizontal right
        for (int i = x+1; i<6; i++) {
            if (boardState.getPieceAt(i, y) != PentagoBoardState.Piece.WHITE) {
                break;
            }
            utility += 1; 
        }

        // Horizontal left
        for (int i = x-1; i>=0; i--) {
            if (boardState.getPieceAt(i, y) != PentagoBoardState.Piece.WHITE) {
                break;
            }
            utility += 1; 
        }
        
        return utility; 
    }

    public int checkDiagonals(PentagoBoardState boardState, int x, int y) {
        int utility = 0;

        int i = x+1;
        int j = y+1;

        // Down right 
        while (i < 6 && j < 6) {
            if (boardState.getPieceAt(i, j) != PentagoBoardState.Piece.WHITE) {
                break;
            }
            utility += 1;
            i++;
            j++;
        }

        i = x-1;
        j = y-1;

        // Up left
        while (i >= 0 && j >= 0) {
            if (boardState.getPieceAt(i, j) != PentagoBoardState.Piece.WHITE) {
                break;
            }
            utility += 1;
            i--;
            j--;
        }

        i = x+1;
        j = y-1;

        // up right
        while (i < 6 && j >= 0) {
            if (boardState.getPieceAt(i, j) != PentagoBoardState.Piece.WHITE) {
                break;
            }
            utility += 1;
            i++;
            j--;
        }

        i = x-1;
        j = y+1;

        // Down left
        while (i >= 0 && j < 6) {
            if (boardState.getPieceAt(i, j) != PentagoBoardState.Piece.WHITE) {
                break;
            }
            utility += 1;
            i--;
            j++;
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
}