import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by JackGuan on 2/23/14.
 */
public abstract class Constraints {
    // constraint graph based on binary constraints
    public HashMap<Variable, LinkedList<Variable>> binaryAdjs = null;
    // the global constraints
    public HashMap<Integer, HashSet<Variable>> globalAdjs = null;

    public boolean conflictTest(Variable var) {
        LinkedList<Variable> adjs = binaryAdjs.get(var);
        if (adjs == null)
            return false; // no adjacent in constraint graph, no conflict
        for (Variable adj : adjs) {
            if (!isSatisfied(var, adj) ) {
                return true;
            }
        }
        return false;
    }

    public boolean consistentTest(Variable var, Variable adj) {
        for(Domain domain : adj.getDomains()){
            adj.assign(domain);
            if(isSatisfied(var, adj)){
                adj.undoAssign();
                return true;
            }
        }
        adj.undoAssign();
        return false;
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
    public void addConstraint(Variable var1, Variable var2) {
        // build the graph
        if(!binaryAdjs.containsKey(var1)) binaryAdjs.put(var1, new LinkedList<Variable>());
        binaryAdjs.get(var1).add(var2);
    }

    public void addConstraint(Variable var1) {
        // TODO: should be overrided if needed
    }

    public abstract boolean isSatisfied(Variable var1, Variable var2);

    public boolean conflictTest(LinkedList<Variable> vars) {
        return false;
    }

    public abstract LinkedList<ArcPair> getAdjArcs(Variable var, LinkedList<Variable> remain);

    public abstract LinkedList<ArcPair> getAdjArcsInvert(Variable var, Variable exclude, LinkedList<Variable> remain);

    public class ArcPair {
        Variable first;
        Variable second;

        ArcPair(Variable v1, Variable v2) {
            first = v1;
            second = v2;
        }

        @Override
        public int hashCode() {
            return first.getId() * Config.VAR_NUM_SUPRT + second.getId();
        }

        @Override
        public String toString() {
            return "[" + first + "," + second + "]";
        }

        @Override
        public boolean equals(Object other) {
            return first == ((ArcPair) other).first
                    && second == ((ArcPair) other).second;
        }
    }
}
