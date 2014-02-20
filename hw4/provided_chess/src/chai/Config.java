package chai;

import sun.launcher.resources.launcher;

/**
 * Created by JackGuan on 2/18/14.
 */
public class Config {
    static int IDS_DEPTH = 5;
    static short[] last_moves = {0, 0, 0, 0};
    static int pointer = 0;
    static short repeat_cnt = 0;

    public static void tryBreakTie(int turn, short move) {
        last_moves[pointer] = move;
        pointer = (pointer + 1) % 4;

        if (last_moves[0] == last_moves[2]
                && last_moves[1] == last_moves[3]) {
            IDS_DEPTH++;
            repeat_cnt++;
            System.out.println("breaking tie: " + IDS_DEPTH);
        } else if (repeat_cnt != 0) {
            IDS_DEPTH -= repeat_cnt;
            repeat_cnt = 0;
            System.out.println("tie solved: " + IDS_DEPTH);
        }

    }
}
