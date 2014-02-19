package chai;

import java.util.Random;

import chesspresso.position.Position;

public class RandomAI implements ChessAI {
	public short getMove(Position position) {
		short [] moves = position.getAllMoves();
        Random generator = new Random(0);
		short move = moves[generator.nextInt(moves.length)];
	
		return move;
	}
}
