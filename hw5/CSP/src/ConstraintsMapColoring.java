import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by JackGuan on 2/24/14.
 */
public class ConstraintsMapColoring extends Constraints{
    // define the 6 basic relationship
    public static final int EQ = 0, GE = 1, LE = 2, GT = 3, LT = 4, NE = 5;

    ConstraintsMapColoring(){
        binaryAdjs = new HashMap<>();
    }

    @Override
    public boolean isSatisfied(Variable var1, Variable var2){
        return var1.getStates().get(0) != var2.getStates().get(0);
    }

    @Override
    public boolean conflictTest(LinkedList<Variable> vars) {
        return false;
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
