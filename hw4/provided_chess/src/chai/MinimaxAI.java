package chai;

import java.util.Random;

import chesspresso.position.Position;

/**
 * Created by JackGuan on 2/17/14.
 */
public class MinimaxAI implements ChessAI {
    public short getMove(Position position) {
        short [] moves = position.getAllMoves();
        short move = moves[new Random().nextInt(moves.length)];

        return move;
    }
}
