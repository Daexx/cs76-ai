package chai;

import chesspresso.move.IllegalMoveException;
import chesspresso.position.Position;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;


/**
 * Created by JackGuan on 2/17/14.
 */
public class ABPruningTrans extends ABPruning{
    protected HashMap<Long, TransTableEntry> p2tte = new HashMap<Long, TransTableEntry>();

    public class TransTableEntry {
        public int eval;
        public int depth;
        public short move;

        TransTableEntry(int e, int d, short m) {
            eval = e;
            depth = d;
            move = m;
        }
    }

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
        System.out.println("ABPT  making move " + elapsedTime / 1000. + "\t");
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
            for (short move : position.getAllMoves()) {
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
                alpha = maxTurn ? bestMove.eval : alpha;
                beta = !maxTurn ? bestMove.eval : beta;
                // prune the subtree if needed
                if(alpha >= beta)
                    return maxTurn ? bestMove.setGetVal(beta) : bestMove.setGetVal(alpha);
            }
            return bestMove;
        }
    }
}
