package chai;

import chesspresso.move.IllegalMoveException;
import chesspresso.position.Position;
import org.omg.CORBA.INTERNAL;

/**
 * Created by JackGuan on 2/17/14.
 */
public class MinimaxAI_V2 implements ChessAI {
    public int currD; // current depth
    public static boolean MAX_TURN = true, MIN_TURN = false;
    private boolean terminalFound;

    public class MoveValuePair {
        public short move;
        public int eval;
        public boolean init;

        MoveValuePair() {
            init = false;
        }

        public void updateMinMax(short m, int e, boolean findMax) {
            if (!init || (findMax && (this.eval < e)) || (!findMax && (this.eval > e))) {
                this.move = m;
                this.eval = e;
            }
        }
    }

    public short getMove(Position position) {
        return minimaxIDS(position, Config.IDS_DEPTH);
    }

    private short minimaxIDS(Position position, int maxDepth) {
        this.terminalFound = false;
        MoveValuePair bestMove = new MoveValuePair();
        for (int d = maxDepth; d <= maxDepth && !this.terminalFound; d++) {
            bestMove = maxMinValue(position, maxDepth, MAX_TURN);
        }
//        //System.out.println("the value: " + bestPair.value);
//        if(bestMove.move == 0)
//            System.out.println("return move 0!! damn it");
        return bestMove.move;
    }

    private MoveValuePair maxMinValue(Position position, int depth, boolean maxTurn) {
        if (depth <= 0 || position.isTerminal()) {
            return handleTerminal(position, maxTurn);
        } else {
            MoveValuePair bestMove = new MoveValuePair();
            for (short move : position.getAllMoves()) {
                Position newPosition = positionMove(position, move);
                MoveValuePair childMove = maxMinValue(newPosition, depth - 1, !maxTurn);
                bestMove.updateMinMax(move, childMove.eval, maxTurn);
                position.undoMove();
            }
            return bestMove;
        }
    }

    private MoveValuePair handleTerminal(Position position, boolean maxTurn) {
        MoveValuePair finalMove = new MoveValuePair();
        if (position.isTerminal() && position.isMate()) {
            this.terminalFound = position.isTerminal();
            finalMove.eval = (maxTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE);
        } else if (position.isTerminal() && position.isStaleMate())
             finalMove.eval = 0;
        else {
            finalMove.eval = (maxTurn ? 1 : -1) * position.getMaterial();
        }
        return finalMove;
    }


    private Position positionMove(Position position, short move) {
        try {
//            System.out.println("positionMove making move " + move);
            position.doMove(move);
            //System.out.println(position);
            return position;
        } catch (IllegalMoveException e) {
            System.out.println("illegal move here!");
            return null;
        }
    }
}
