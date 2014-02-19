package chai;

import chesspresso.move.IllegalMoveException;
import chesspresso.position.Position;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by JackGuan on 2/17/14.
 */
public class ABPruningTrans implements ChessAI {
    public static boolean MAX_TURN = true, MIN_TURN = false;
    public static int MATE = Integer.MAX_VALUE, BE_MATED = Integer.MIN_VALUE;
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
    public short getMove(Position position) throws IllegalMoveException {
        long start = System.currentTimeMillis();
        short result = minimaxIDS(position, Config.IDS_DEPTH);
        long elapsedTime = System.currentTimeMillis() - start;
        try {
            FileOutputStream timecompete = new FileOutputStream("timecompete.txt", true);
            timecompete.write((elapsedTime / 1000. + "\t").getBytes());
            timecompete.close();
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException : " + ex);
        } catch (IOException ioe) {
            System.out.println("IOException : " + ioe);
        }
        System.out.print("ABP  making move " + elapsedTime / 1000. + "\t");
        return result;
    }

    private short minimaxIDS(Position position, int maxDepth) throws IllegalMoveException {
        this.terminalFound = false;
        MoveValuePair bestMove = new MoveValuePair();
        for (int d = 1; d <= maxDepth && !this.terminalFound; d++) {
            bestMove = ABMaxMinValue(position, maxDepth - 1, BE_MATED, MATE, MAX_TURN);
        }
        return bestMove.move;
    }

    private MoveValuePair ABMaxMinValue(Position position, int depth, int alpha, int beta, boolean maxTurn) throws IllegalMoveException {
        if (depth <= 0 || position.isTerminal()) {
            return handleTerminal(position, maxTurn);
        } else {
            MoveValuePair bestMove = new MoveValuePair();
            for (short move : position.getAllMoves()) {
                position.doMove(move);
                MoveValuePair childMove = ABMaxMinValue(position, depth - 1, alpha, beta, !maxTurn);
                bestMove.updateMinMax(move, childMove.eval, maxTurn);
                position.undoMove();
                if(maxTurn)
                    alpha = bestMove.eval;
                else
                    beta = bestMove.eval;
                if (doPruning(bestMove.eval, alpha, beta, maxTurn)) {
//                    System.out.println("prunning at depth: " + depth);
                    return bestMove;
                }
            }
            return bestMove;
        }
    }

    private boolean doPruning(int value, int alpha, int beta, boolean maxTurn) {
        return (maxTurn && value >= beta) || (!maxTurn && value <= alpha);
    }

    private MoveValuePair handleTerminal(Position position, boolean maxTurn) {
        MoveValuePair finalMove = new MoveValuePair();
        if (position.isTerminal() && position.isMate()) {
            this.terminalFound = position.isTerminal();
            finalMove.eval = (maxTurn ? BE_MATED : MATE);
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
