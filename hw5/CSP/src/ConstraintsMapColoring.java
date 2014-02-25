import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by JackGuan on 2/24/14.
 */
public class ConstraintsMapColoring extends Constraints{
    // define the 6 basic relationship
    public static final int EQ = 0, GE = 1, LE = 2, GT = 3, LT = 4, NE = 5;

    ConstraintsMapColoring(){
        binary = new HashMap<>();
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
     * @param relatinshp the relationship
     * @param var2       the second variable
     */
    public void addConstraint(Variable var1, String relatinshp, Variable var2) {
        Integer r;
        if (relatinshp.equals("=") || relatinshp.equals("==")) {
            r = EQ;
        } else if (relatinshp.equals(">=")) {
            r = GE;
        } else if (relatinshp.equals("<=")) {
            r = LE;
        } else if (relatinshp.equals(">")) {
            r = GT;
        } else if (relatinshp.equals("<")) {
            r = LT;
        } else if (relatinshp.equals("!=")) {
            r = NE;
        } else {
            System.out.print("illegal relation ship!");
            r = -1;
        }
        // add the relationship
        binary.put(new VarPair(var1, var2), r);

        // build the graph
        if(!adjacents.containsKey(var1)) adjacents.put(var1, new LinkedList<Variable>());
        adjacents.get(var1).add(var2);
        return;
    }

    public boolean isSatisfied(Variable var1, Variable var2){
        VarPair varpair = new VarPair(var1, var2);
        if (binary.containsKey(varpair)) {
            int r = binary.get(varpair);
            switch (r) {
                case EQ:
                    return var1.assignment == var2.assignment;
                case GE:
                    return var1.assignment >= var2.assignment;
                case LE:
                    return var1.assignment <= var2.assignment;
                case GT:
                    return var1.assignment > var2.assignment;
                case LT:
                    return var1.assignment < var2.assignment;
                case NE:
                    return var1.assignment != var2.assignment;
                default:
                    break;
            }
        }
        // System.out.println("No constraint between " + varpair);
        return true;
    }

    public boolean conflictTest(LinkedList<Variable> vars, Variable var) {
        LinkedList<Variable> adjs = adjacents.get(var);
        if (adjs == null)
            return false; // no adjacent in constraint graph, no conflict
        for (Variable adj : adjs) {
            if (adj.assignment != -1
                    && !isSatisfied(var, adj) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean conflictTest(LinkedList<Variable> vars) {
        return false;
    }
}
