import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by JackGuan on 2/24/14.
 */
public class ConstraintsMapColoring extends Constraints{
    // define the 6 basic relationship
    public static final int EQ = 0, GE = 1, LE = 2, GT = 3, LT = 4, NE = 5;

    ConstraintsMapColoring(){
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
        return var1.getStates().get(0) != var2.getStates().get(0);
    }

    @Override
    public boolean conflictTest(LinkedList<Variable> vars) {
        return false;
    }
}
