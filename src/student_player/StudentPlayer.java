package student_player;

import boardgame.Move;

import pentago_twist.PentagoPlayer;
import pentago_twist.PentagoBoardState;

/** A player file submitted by a student. */
public class StudentPlayer extends PentagoPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260772982");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(PentagoBoardState boardState) {

        long startTime = System.nanoTime();

        Move winningMove = MyTools.getWinner(boardState);

        if (winningMove != null) { 
            return winningMove;
        }
        
        int depth = 1;

        Move bestMove = MyTools.minimax(boardState, depth, boardState.getTurnPlayer(), null, 
            Integer.MIN_VALUE, Integer.MAX_VALUE).getKey();
        
        long stopTime = System.nanoTime();
        System.out.println(stopTime - startTime);

        return bestMove;

        // return boardState.getRandomMove();
    }
}