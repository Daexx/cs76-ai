import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by JackGuan on 2/23/14.
 */
public class MapColoringCSP extends ProblemCSP {

    public class MCNode implements CSPNode{
        public HashMap<Integer, Integer> assignment;

        protected HashMap<Integer, LinkedList<Integer>> unary;
        protected HashMap<Integer, LinkedList<Integer>> binary;

        public void addUnary(Integer a, Integer b) {

        }

        public void addBinary(Integer a, Integer b) {
            return;
        }

        @Override
        public ArrayList<CSPNode> getSuccessors() {
            return null;
        }
    }
}
