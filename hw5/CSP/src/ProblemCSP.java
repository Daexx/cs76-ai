import org.omg.PortableInterceptor.INACTIVE;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by JackGuan on 2/23/14.
 */
public class ProblemCSP {
    ArrayList<Integer> asgnmnts; // remember to undo when using dfs
    HashSet<Integer> domain;
    Constraint cons;
    int varNum;

    ProblemCSP() {

    }

    ProblemCSP(ArrayList<Integer> a, HashSet<Integer> d, Constraint c) {
        varNum = a.size();
        asgnmnts = a;
        domain = d;
        cons = c;
        if (cspDFS(0, domain)) {
            System.out.println("solution found!!");
            for (int i = 0; i < asgnmnts.size(); i++) {
                System.out.println("[" + i + "," + asgnmnts.get(i) + "]");
            }
        } else {
            System.out.println("solution not found!!");
        }
    }

    protected void back

    protected boolean cspDFS(int var, HashSet<Integer> domain) {
        if (var >= asgnmnts.size()) return true;
        // iterate all the remain domains
        for (Integer d : domain) {
            asgnmnts.set(var, d);
            if (!conflictTest(var, d)) {
                // decouple the domains for different layers of recursion
//                HashSet<Integer> domainShrink = (HashSet<Integer>) domain.clone();
//                domainShrink.remove(d);
                // try to assign the next variable
                if (cspDFS(var+1, domain))
                    return true;  // solution found!
            }
        }
        // if not found, reset the assignment
        asgnmnts.set(var, -1);
        return false;
    }

    public boolean conflictTest(int var, int val) {
        ArrayList<Integer> adjs = cons.adjacents.get(var);
        if(adjs == null)
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
