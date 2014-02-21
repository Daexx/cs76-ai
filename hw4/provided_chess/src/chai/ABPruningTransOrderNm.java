package chai;

import chesspresso.move.IllegalMoveException;
import chesspresso.position.Position;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;


/**
 * Created by JackGuan on 2/17/14.
 */
public class ABPruningTransOrderNm extends ABPruningTransOrder {
    public static boolean ASCENDING;

    @Override
    public short getMove(Position position) throws IllegalMoveException {
        long start = System.currentTimeMillis();
        short result = minimaxIDS(position, (int) Config.IDS_DEPTHS[position.getToPlay()]);
//        short result = minimaxIDS(position, Config.IDS_DEPTH);
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
        System.out.println("ABPTON  making move " + elapsedTime / 1000. + "\t");
        Config.tryBreakTie(position.getToPlay(), result);
        Config.tuneDepth(elapsedTime / 1000., position.getToPlay());
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

            if (depth > Config.NMH_R) {
                position.setToPlay((position.getToPlay() + 1) % 2);
                int value = ABMaxMinValue(position, depth - Config.NMH_R, alpha, beta, !maxTurn).eval;
                position.setToPlay((position.getToPlay() + 1) % 2);
                if ((maxTurn && value >= beta) || (!maxTurn && value <= alpha))
                    return maxTurn ? bestMove.setGetVal(beta) : bestMove.setGetVal(alpha);
            }

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
                    return maxTurn ? bestMove.setGetVal(beta) : bestMove.setGetVal(alpha);
            }
            return bestMove;
        }
    }
}