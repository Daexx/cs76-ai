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
public class ABPruningTransOrderQs extends ABPruningTransOrder {
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
        System.out.println("ABPTOQS  making move " + elapsedTime / 1000. + "\t");
        Config.tryBreakTie(position.getToPlay(), result);
        return result;
    }


    @Override
    protected MoveValuePair ABMaxMinValue(Position position, int depth, int alpha, int beta, boolean maxTurn)
            throws IllegalMoveException {
        if (depth <= 0 || position.isTerminal()) {
            return handleTerminal(position, maxTurn, alpha, beta);
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
                    return maxTurn ? bestMove.setGetVal(beta) : bestMove.setGetVal(alpha);
            }
            return bestMove;
        }
    }

    protected MoveValuePair handleTerminal(Position position, boolean maxTurn, int alpha, int beta) throws IllegalMoveException {
        MoveValuePair finalMove = new MoveValuePair();
        if (position.isTerminal() && position.isMate()) {
            this.terminalFound = position.isTerminal();
            finalMove.eval = (maxTurn ? BE_MATED : MATE);
        } else if (position.isTerminal() && position.isStaleMate())
            finalMove.eval = 0;
        else {
            finalMove.eval = maxTurn ? -quiescence(position, -alpha, -beta, !maxTurn) :
                     quiescence(position, alpha, beta, !maxTurn);
        }
        return finalMove;
    }

    protected int quiescence(Position position, int alpha, int beta, boolean maxTurn) throws IllegalMoveException {
        int evaluation = handleTerminal(position, maxTurn).eval;
        // set the stand pat value,
        // in case there is no capturing
        if (evaluation >= beta)
            return beta;
        if (evaluation > alpha)
            alpha = evaluation;

        // this part is similar the AB pruning
        LinkedList<MoveValuePair> sortedMoves = getCapturingSortedMoves(position, maxTurn);
        for (MoveValuePair movepair : sortedMoves) {
            short move = movepair.move;
            position.doMove(move);
            int value = -quiescence(position, -alpha, -beta, !maxTurn);
            position.undoMove();
            if (value >= beta)
                return beta;
            if (value > alpha)
                alpha = value;
        }
        return alpha;
    }


    protected MoveValuePair handleTerminal(Position position, boolean maxTurn) {
        MoveValuePair finalMove = new MoveValuePair();
        if (position.isTerminal() && position.isMate()) {
            this.terminalFound = position.isTerminal();
            finalMove.eval = (maxTurn ? MATE : BE_MATED);
        } else if (position.isTerminal() && position.isStaleMate())
            finalMove.eval = 0;
        else {
            finalMove.eval = (int) ((position.getMaterial() + position.getDomination()));
        }
//        System.out.print(finalMove.eval + " ");
        return finalMove;
    }

    protected LinkedList<MoveValuePair> getCapturingSortedMoves(Position position, boolean maxTurn) throws IllegalMoveException {
        LinkedList<MoveValuePair> sortedMoves = new LinkedList<MoveValuePair>();
        short[] moves = position.getAllCapturingMoves();
        MoveValuePair theMove = null;
        ASCENDING = maxTurn ? false : true;

        for (short move : moves) {
            position.doMove(move);
            if (p2tte.containsKey(position.getHashCode())) {
                theMove = new MoveValuePair(move, p2tte.get(position.getHashCode()).eval);
            } else {
                // for max turn, I assign worst values those unvisited positions
                theMove = new MoveValuePair(move, maxTurn ? BE_MATED : MATE);
//                int eval = (int) ((maxTurn ? -1 : 1) * (position.getMaterial() + position.getDomination()));
//                theMove = new MoveValuePair(move, eval);
            }
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