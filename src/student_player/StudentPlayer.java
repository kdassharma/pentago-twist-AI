package student_player;

import boardgame.Move;

import pentago_twist.PentagoPlayer;
import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;

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
        MyTools agent = new MyTools(startTime);

        // int depth = 2;
        
        int depth = (boardState.getTurnNumber() <= 10) ? 2 :  3;
        // if (boardState.getTurnNumber() > 20) {
        //     depth = 4;
        // }

        // Move winningMove = agent.getWinner(boardState);

        // if (winningMove != null) { 
        //     return winningMove;
        // }

        Move bestMove = agent.minimax(boardState, depth, boardState.getTurnPlayer(), (PentagoMove) boardState.getRandomMove(), 
            Integer.MIN_VALUE, Integer.MAX_VALUE).getKey();

        long stopTime = System.nanoTime();
        System.out.println((stopTime - startTime)/Math.pow(10, 9));
        // System.out.println(bestMove);

        return bestMove;
    }
}