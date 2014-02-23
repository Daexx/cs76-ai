import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by JackGuan on 2/23/14.
 */
public class ProblemCSP {
    protected HashMap<Integer, Integer> assigment;
    public Constraint constraints;
    protected CSPNode startNode;

    public interface CSPNode {
        public ArrayList<CSPNode> getSuccessors();
    }

    ProblemCSP(){

    }
}
