package chai;

import chesspresso.move.IllegalMoveException;
import chesspresso.position.Position;

/**
 * Created by JackGuan on 2/17/14.
 */
public class ABPruning implements ChessAI {
    public static boolean MAX_TURN = true, MIN_TURN = false;
    private boolean terminalFound;

    public class MoveValuePair {
        public short move = 0;
        public int eval;

        MoveValuePair() {
        }

        public void updateMinMax(short m, int e, boolean findMax) {
            if (move == 0 || (findMax && (this.eval < e)) || (!findMax && (this.eval > e))) {
                this.move = m;
                this.eval = e;
            }
        }
    }

    @Override
    public short getMove(Position position)  throws IllegalMoveException{
        return minimaxIDS(position, Config.IDS_DEPTH);
    }

    private short minimaxIDS(Position position, int maxDepth) throws IllegalMoveException{
        this.terminalFound = false;
        MoveValuePair bestMove = new MoveValuePair();
        for (int d = 1; d <= maxDepth && !this.terminalFound; d++) {
            bestMove = ABMaxMinValue(position, maxDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, MAX_TURN);
        }
        return bestMove.move;
    }

    private MoveValuePair ABMaxMinValue(Position position, int depth, int alpha, int beta, boolean maxTurn) throws IllegalMoveException{
        if (depth <= 0 || position.isTerminal()) {
            return handleTerminal(position, maxTurn);
        } else {
            MoveValuePair bestMove = new MoveValuePair();
            for (short move : position.getAllMoves()) {
                position.doMove(move);
                MoveValuePair childMove = ABMaxMinValue(position, depth - 1, alpha, beta, !maxTurn);
                bestMove.updateMinMax(move, childMove.eval, maxTurn);
                position.undoMove();
                if(false && doPruning(bestMove.eval, alpha, beta, maxTurn))
                    System.out.println("prunning at depth: " + depth);
                    return bestMove;
            }
            return bestMove;
        }
    }

    private boolean doPruning(int value, int alpha, int beta, boolean maxTurn ){
        return (maxTurn && value >= beta ) || (!maxTurn && value <= alpha) ;
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
//        System.out.print(finalMove.eval + " ");
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
