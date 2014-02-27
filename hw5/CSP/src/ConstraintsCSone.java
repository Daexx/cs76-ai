import java.util.*;

/**
 * Created by JackGuan on 2/26/14.
 */
public class ConstraintsCSone extends Constraints {
    ConstraintsCSone() {
        globalAdjs = new HashMap<>();
        binaryAdjs = new HashMap<>();
    }

    @Override
    public void addConstraint(Variable var1, Variable var2) {
        // build the graph
        return;
    }

    @Override
    public boolean conflictTest(Variable var) {
        // for global constraints
        if (!isSatisfied(var))
            return true;

        // for binary constraints
        LinkedList<Variable> adjs = binaryAdjs.get(var);
        if (adjs == null)
            return false; // no adjacent in constraint graph, no conflict
        for (Variable adj : adjs) {
            if (!isSatisfied(var, adj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param var1
     * @param var2
     * @return
     */
    @Override
    public boolean isSatisfied(Variable var1, Variable var2) {
        return true;
    }

    public boolean isSatisfied(Variable var) {
        // one leader in one section a section is created by leader,
        // so when assigning leaders,there should be only one person in the section
        HashSet<Variable> thisSection = globalAdjs.get(var.getAssignment());
        if (((VariableCSone) var).isLeader() == 1) {
            if (thisSection.size() > 1)
                return false;
            else
                return true;
        } else if (((VariableCSone) var).isLeader() != 1
                && thisSection.size() == 1)
            return false;

        // keep balance among sections
        int numOfLeader = globalAdjs.size();
        Double numOfStudent = 0.;
        for (Integer section : globalAdjs.keySet())
            numOfStudent += globalAdjs.get(section).size() - 1;
        if (thisSection.size() - 1 < numOfStudent / numOfLeader - 1
                || thisSection.size() - 1 > numOfStudent / numOfLeader + 1)
            return false;

        return true;
    }

    @Override
    public LinkedList<Constraints.ArcPair> getAdjArcs(Variable var, LinkedList<Variable> remain) {
        LinkedList<ArcPair> arcs = new LinkedList<>();
        if (((VariableCSone) var).isLeader() == 0) {
            for (Iterator<Variable> it = remain.iterator(); it.hasNext(); ) {
                Variable adj = it.next();
                arcs.add(new ArcPair(var, adj));
            }
        }
        return arcs;
    }

    @Override
    public LinkedList<Constraints.ArcPair> getAdjArcsInvert(Variable var, Variable exclude, LinkedList<Variable> remain) {
        LinkedList<ArcPair> arcs = new LinkedList<>();
        if (((VariableCSone) var).isLeader() == 0) {
            for (Iterator<Variable> it = remain.iterator(); it.hasNext(); ) {
                Variable adj = it.next();
                if (remain.contains(adj) && exclude != adj)
                    arcs.add(new ArcPair(adj, var));
            }
        }
        return arcs;
    }
}
