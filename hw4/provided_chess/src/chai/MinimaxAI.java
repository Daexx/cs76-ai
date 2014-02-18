package chai;

import java.util.Random;

import chesspresso.position.Position;

/**
 * Created by JackGuan on 2/17/14.
 */
public class MinimaxAI implements ChessAI {
    public int currD; // current depth

    public short getMove(Position position) {
        short [] moves = position.getAllMoves();
        short move = moves[new Random().nextInt(moves.length)];

        return move;
    }

    public short minimaxIDS(int maxDepth){

    }

    public short minimaxDFS(int depth){

    }
}
