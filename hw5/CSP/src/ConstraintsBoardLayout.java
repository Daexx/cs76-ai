import java.io.CharArrayReader;
import java.util.*;

/**
 * Created by JackGuan on 2/24/14.
 */
public class ConstraintsBoardLayout extends Constraints{
    // define the 6 basic relationship
    public static final int EQ = 0, GE = 1, LE = 2, GT = 3, LT = 4, NE = 5;

    ConstraintsBoardLayout(){
        adjacents = new HashMap<>();
    }

    /**
     * addConstraint
     * <p/>
     * Build the constraint graph based on variable name and
     * relationship. Remember to build an in-directed graph,
     * so you need to call this twice every time
     *
     * @param var1       the first variable
     * @param var2       the second variable
     */
    @Override
    public void addConstraint(Variable var1, Variable var2) {
        // build the graph
        if(!adjacents.containsKey(var1)) adjacents.put(var1, new LinkedList<Variable>());
        adjacents.get(var1).add(var2);
        return;
    }

    @Override
    public boolean isSatisfied(Variable var1, Variable var2){
        HashSet<Integer> overlap = new HashSet<>();
        ArrayList<Integer> states1 = var1.getStates();
        ArrayList<Integer> states2 = var2.getStates();
        for(Iterator<Integer> it = states1.iterator(); it.hasNext();){
            overlap.add(it.next());
        }
        for(Iterator<Integer> it = states2.iterator(); it.hasNext();){
            if(overlap.contains(it.next()))
                return false;
        }
        return true;
    }
}
