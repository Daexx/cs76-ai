package chai;

import chesspresso.move.IllegalMoveException;
import chesspresso.position.Position;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


/**
 * Created by JackGuan on 2/17/14.
 */
public class ABPruningTransOrder extends ABPruningTrans {
    public static boolean ASCENDING;

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
        System.out.println("ABPTO  making move " + elapsedTime / 1000. + "\t");
        return result;
    }


    @Override
    protected MoveValuePair ABMaxMinValue(Position position, int depth, int alpha, int beta, boolean maxTurn)
            throws IllegalMoveException {
        if (depth <= 0 || position.isTerminal()) {
            return handleTerminal(position, maxTurn);
        } else {
            MoveValuePair bestMove = new MoveValuePair();
            LinkedList<MoveValuePair> sortedMoves = getSortedMoves(position, maxTurn);
            for (MoveValuePair movepair : sortedMoves) {
                short move = movepair.move;
                // collect values from further moves
                // get and update transposition table if possible
                position.doMove(move);
                if (this.p2tte.containsKey(position.getHashCode())
                        && (this.p2tte.get(position.getHashCode()).depth >= depth)) {
                    //System.out.println("trans table at level: " + depth);
                    TransTableEntry tte = p2tte.get(position.getHashCode());
                    bestMove.updateMinMax(move, tte.eval, maxTurn);
                } else {
                    // recursive method
                    MoveValuePair childMove = ABMaxMinValue(position, depth - 1, alpha, beta, !maxTurn);
                    bestMove.updateMinMax(move, childMove.eval, maxTurn);
                    p2tte.put(position.getHashCode(), new TransTableEntry(childMove.eval, depth, move));
                }
                position.undoMove();
                // update the alpha beta boundary
                if (maxTurn)
                    alpha = bestMove.eval;
                else
                    beta = bestMove.eval;
                // prune the subtree if needed
                if (alpha >= beta)
                    return bestMove;
            }
            return bestMove;
        }
    }

    protected LinkedList<MoveValuePair> getSortedMoves(Position position, boolean maxTurn) throws IllegalMoveException {
        LinkedList<MoveValuePair> sortedMoves = new LinkedList<MoveValuePair>();
        short[] moves = position.getAllMoves();
        MoveValuePair theMove = null;
        ASCENDING = maxTurn ? false : true;

        for (short move : moves) {
            position.doMove(move);
            if (p2tte.containsKey(position.getHashCode()))
                theMove = new MoveValuePair(move, p2tte.get(position.getHashCode()).eval);
            else
                // for max turn, I assign worst values those unvisited positions
                theMove = new MoveValuePair(move, maxTurn ? BE_MATED : MATE);
            position.undoMove();
            sortedMoves.add(theMove);
        }

        Collections.sort(sortedMoves, new Comparator<MoveValuePair>() {
            @Override
            public int compare(MoveValuePair c1, MoveValuePair c2) {
//                System.out.println(c1.eval + " vs " + c2.eval);
                return (int) ((ASCENDING ? 1 : -1) * Math.signum(c1.eval - c2.eval)); // use your logic
            }
        });
        return sortedMoves;
    }
}
