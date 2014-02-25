import sun.awt.image.ImageWatched;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by JackGuan on 2/23/14.
 */
public class Constraints {
    // constraint, map from a pair of variable to a relationship
    public HashMap<ArcPair, Integer> ConsArc = null;
    // constraint graph
    public HashMap<Variable, LinkedList<Variable>> adjacents = null;

    public void addConstraint(Variable var1, String relatinshp, Variable var2) {

    }

    public boolean isSatisfied(Variable var1, Variable var2) {
        return false;
    }

    public boolean conflictTest(LinkedList<Variable> vars, Variable var) {
        LinkedList<Variable> adjs = adjacents.get(var);
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

    public boolean conflictTest(LinkedList<Variable> vars) {
        return false;
    }

    public LinkedList<ArcPair> getAdjArcs(Variable var, LinkedList<Variable> remain) {
        LinkedList<ArcPair> arcs = new LinkedList<>();
        LinkedList<Variable> adjs = adjacents.get(var);

        if (adjs != null) {
            for (Iterator<Variable> it = adjs.iterator(); it.hasNext(); ) {
                Variable adj = it.next();
                if (remain.contains(adj))
                    arcs.add(new ArcPair(var, adj));
            }
        }
        return arcs;
    }

    public LinkedList<ArcPair> getAdjArcsInvert(Variable var, Variable exclude, LinkedList<Variable> remain) {
        LinkedList<ArcPair> arcs = new LinkedList<>();
        LinkedList<Variable> adjs = adjacents.get(var);

        if (adjs != null) {
            for (Iterator<Variable> it = adjs.iterator(); it.hasNext(); ) {
                Variable adj = it.next();
                if (remain.contains(adj) && exclude != adj)
                    arcs.add(new ArcPair(adj, var));
            }
        }
        return arcs;
    }

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
