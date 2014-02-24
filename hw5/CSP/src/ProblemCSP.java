import org.omg.PortableInterceptor.INACTIVE;

import javax.net.ssl.SSLContext;
import java.util.*;

/**
 * Created by JackGuan on 2/23/14.
 */
public class ProblemCSP {
    ArrayList<Integer> asgnmnts; // remember to undo when using dfs
    HashSet<Integer> variables;
    HashMap<Integer, HashSet<Integer>> domains;
    Constraint cons;
    int varNum;

    ProblemCSP() {

    }

    ProblemCSP(HashSet<Integer> v, HashMap<Integer, HashSet<Integer>> d, Constraint c) {
        varNum = v.size();
        variables = v;
        domains = d;
        cons = c;

        // noted that capacity is not size
        asgnmnts = new ArrayList<>(varNum);
        for (int i = 0; i < varNum; i++) {
            asgnmnts.add(-1);
        }
    }

    protected void cspSearch() {
        if (cspDFS(0)) {
            System.out.println("solution found!!");
            for (int i = 0; i < asgnmnts.size(); i++) {
                System.out.println("[" + i + "," + asgnmnts.get(i) + "]");
            }
        } else {
            System.out.println("solution not found!!");
        }
    }

    protected boolean cspDFS(int var) {
        if (var >= asgnmnts.size()) return true;
        // shrink the domain of this variable first
        domainShrink(var);
        // iterate all the remain domain
        for (Integer d : domains.get(var)) {
            asgnmnts.set(var, d);
            // try to assign the next variable
            if (cspDFS(var + 1))
                return true;  // solution found!
        }
        // if not found, reset the assignment
        asgnmnts.set(var, -1);
        return false;
    }

    protected void domainShrink(Integer var) {
        HashSet<Integer> domain = domains.get(var);
        for(Iterator<Integer> it = domain.iterator(); it.hasNext(); ){
            Integer d = it.next();
            if(conflictTest(var, d))
                it.remove();
        }
    }

    public boolean conflictTest(int var, int val) {
        ArrayList<Integer> adjs = cons.adjacents.get(var);
        if (adjs == null)
            return false; // no adjacent in constraint graph, no conflict
        for (Integer adj : adjs) {
            if (asgnmnts.get(adj) != -1
                    && !cons.isSatisfied(var, val, adj, asgnmnts.get(adj))) {
                return true;
            }
        }
        return false;
    }
}
