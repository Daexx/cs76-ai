import java.util.*;

/**
 * Created by JackGuan on 2/24/14.
 */
public class ConstraintsBoardLayout extends Constraints{
    ConstraintsBoardLayout(){
        binaryAdjs = new HashMap<>();
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
        if(!binaryAdjs.containsKey(var1)) binaryAdjs.put(var1, new LinkedList<Variable>());
        binaryAdjs.get(var1).add(var2);
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
    public LinkedList<ArcPair> getAdjArcs(Variable var, LinkedList<Variable> remain) {
        LinkedList<ArcPair> arcs = new LinkedList<>();
        LinkedList<Variable> adjs = binaryAdjs.get(var);

        if (adjs != null) {
            for (Iterator<Variable> it = adjs.iterator(); it.hasNext(); ) {
                Variable adj = it.next();
                if (remain.contains(adj))
                    arcs.add(new ArcPair(var, adj));
            }
        }
        return arcs;
    }

    @Override
    public LinkedList<ArcPair> getAdjArcsInvert(Variable var, Variable exclude, LinkedList<Variable> remain) {
        LinkedList<ArcPair> arcs = new LinkedList<>();
        LinkedList<Variable> adjs = binaryAdjs.get(var);

        if (adjs != null) {
            for (Iterator<Variable> it = adjs.iterator(); it.hasNext(); ) {
                Variable adj = it.next();
                if (remain.contains(adj) && exclude != adj)
                    arcs.add(new ArcPair(adj, var));
            }
        }
        return arcs;
    }
}
