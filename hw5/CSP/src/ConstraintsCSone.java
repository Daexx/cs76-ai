import java.util.*;

/**
 * Created by JackGuan on 2/26/14.
 */
public class ConstraintsCSone extends Constraints {
    ConstraintsCSone(){
        globalAdjs = new HashMap<>();
    }

    /**
     * addConstraint
     * <p/>
     * Build the constraint graph based on variable name and
     * relationship. Remember to build an in-directed graph,
     * so you need to call this twice every time
     *
     * @param var       the first variable
     */
    @Override
    public void addConstraint(Variable var) {
        // build the graph
        return;
    }

    /**
     *
     * @param var1
     * @param var2
     * @return
     */
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

    @Override
    public LinkedList<Constraints.ArcPair> getAdjArcs(Variable var, LinkedList<Variable> remain) {
        LinkedList<Constraints.ArcPair> arcs = new LinkedList<>();
        HashSet<Variable> adjs = globalAdjs.get(var.getAssignment());

        if (!adjs.isEmpty()) {
            for (Iterator<Variable> it = adjs.iterator(); it.hasNext(); ) {
                Variable adj = it.next();
                if (remain.contains(adj))
                    arcs.add(new Constraints.ArcPair(var, adj));
            }
        }
        return arcs;
    }

    @Override
    public LinkedList<Constraints.ArcPair> getAdjArcsInvert(Variable var, Variable exclude, LinkedList<Variable> remain) {
        LinkedList<Constraints.ArcPair> arcs = new LinkedList<>();
        HashSet<Variable> adjs = globalAdjs.get(var.getAssignment());

        if (!adjs.isEmpty()) {
            for (Iterator<Variable> it = adjs.iterator(); it.hasNext(); ) {
                Variable adj = it.next();
                if (remain.contains(adj) && exclude != adj)
                    arcs.add(new Constraints.ArcPair(adj, var));
            }
        }
        return arcs;
    }
}
