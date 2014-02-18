package chai;

import java.util.Random;

import chesspresso.move.IllegalMoveException;
import chesspresso.position.Position;

/**
 * Created by JackGuan on 2/17/14.
 */
public class MinimaxAI implements ChessAI {
    public int currD; // current depth
    public static boolean GET_MAX = true, GET_MIN = false;
    private boolean terminalFound;

    public class MoveValuePair{
        public short move = -1;
        public int value;
        private boolean isMax;

        MoveValuePair(boolean getM){
            isMax = getM;
            value = getM ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }

        public void update(int newValue, short newMove){
            // I don't use xor operation '^' because it does not
            // support short circuit semantics
            if ( ((value - newValue < 0 ) && isMax) ||
                    (value - newValue > 0 ) && !isMax){
                value = newValue;
                move = newMove;
            }
        }
    }

    public short getMove(Position position) {
        short[] moves = position.getAllMoves();
        short move = moves[new Random().nextInt(moves.length)];

        return move;
    }

    public short minimaxIDS(Position position, int maxDepth) {
        this.terminalFound = false;
        short move;
        for(int d = 1; d < maxDepth || this.terminalFound ; d++){
             move = minmaxDecision(position, maxDepth);
        }
        return move;
    }

    public short minmaxDecision(Position position, int depth) {
        MoveValuePair bestPair = new MoveValuePair(GET_MAX);
        for (short move : position.getAllMoves()) {
            int thisMax = maxMinValue(positionMove(position, move), depth - 1, GET_MAX);
            bestPair.update(thisMax, move);
            position.undoMove();
        }
        return bestPair.move;
    }

    public int maxMinValue(Position position, int depth, boolean isGetMax){
        if(depth == 0 ){
            if(position.isTerminal()) terminalFound = true;
            return position.getMaterial();
        }

        MoveValuePair bestPair = new MoveValuePair(isGetMax);
        for (short move : position.getAllMoves()) {
            int thisMin = maxMinValue(positionMove(position, move), depth - 1, !isGetMax);
            bestPair.update(thisMin, move);
            position.undoMove();
        }
        return bestPair.value;
    }

    private Position positionMove(Position position, short move){
        try {
            System.out.println("making move " + move);
            position.doMove(move);
            System.out.println(position);
            return position;
        } catch (IllegalMoveException e) {
            System.out.println("illegal move!");
            return null;
        }
    }
}
