package chai;

import java.util.Random;

import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.position.Position;

/**
 * Created by JackGuan on 2/17/14.
 */
public class MinimaxAI implements ChessAI {
    public static boolean GET_MAX = true, GET_MIN = false;
    public static int MATE = Integer.MAX_VALUE, BE_MATED = Integer.MIN_VALUE;
    private boolean terminalFound;

    public class MoveValuePair {
        public short move = -1;
        public int value;
        private boolean isMax;
        public boolean isupdate = false;

        MoveValuePair() {
            value = 0;
        }

        MoveValuePair(boolean getM) {
            isMax = getM;
            value = getM ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        }

        public boolean update(int newValue, short newMove) {
            // I don't use xor operation '^' because it does not
            // support short circuit semantics
            if (((value - newValue <= 0) && isMax)
                    ||  ((value - newValue >= 0) && !isMax)
                    || move == -1) {
                value = newValue;
                move = newMove;
                isupdate = true;
                return true;
            } else
                return false;
        }
    }

    public short getMove(Position position) {
        return minimaxIDS(position, Config.IDS_DEPTH);
    }

    private short minimaxIDS(Position position, int maxDepth) {
        this.terminalFound = false;
        MoveValuePair bestPair = new MoveValuePair();
        for (int d = maxDepth; d <= maxDepth && !this.terminalFound; d++) {
            bestPair = maxMinValue(position, maxDepth, GET_MAX);
            System.out.print("[" + bestPair.value + ", " + bestPair.isupdate + "]\n");
        }
        //System.out.println("the value: " + bestPair.value);
        return bestPair.move;
    }

//    private MoveValuePair minmaxDecision(Position position, int depth) {
//        MoveValuePair bestPair = new MoveValuePair(GET_MAX);
//        short[] moves = position.getAllMoves();
//        int i = 0;
//        for (short move : moves) {
//            int thisMax = maxMinValue(positionMove(position, move), depth - 1, GET_MIN);
//            boolean isUpdate = false;
//            if(thisMax > bestPair.value)
//            {
//                bestPair.value = thisMax;
//                bestPair.move = move;
//                isUpdate =true;
//            }
//            position.undoMove();
//            System.out.print("[" + i++ + "," + thisMax + ", " + isUpdate + "]\n");
//        }
//
//        System.out.println();
//        return bestPair;
//    }

    private MoveValuePair maxMinValue(Position position, int depth, boolean isGetMax) {
        MoveValuePair bestPair = new MoveValuePair(isGetMax);
        MoveValuePair min_or_max = new MoveValuePair(isGetMax);
        boolean getupdate = false;
        if (depth <= 0 || position.isTerminal()) {
            this.terminalFound = position.isTerminal();
            bestPair.update((isGetMax ? 1 : -1) * position.getMaterial(), (short) -2);
        } else {
            int i = 0;
            for (short move : position.getAllMoves()) {
                min_or_max = maxMinValue(positionMove(position, move), depth - 1, !isGetMax);
                getupdate |= bestPair.update(min_or_max.value, move);
//                System.out.println("getupdate " + getupdate);
                position.undoMove();
                i++;
            }
//            if(!bestPair.isupdate)
//                System.out.println(min_or_max.value + "   " + i);
        }

        return bestPair;
    }

    private Position positionMove(Position position, short move) {
        try {
//            System.out.println("positionMove making move " + move);
            position.doMove(move);
            //System.out.println(position);
            return position;
        } catch (IllegalMoveException e) {
            System.out.println("illegal move!");
            return null;
        }
    }
}
