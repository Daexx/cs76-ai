import java.util.*;

/**
 * Created by JackGuan on 2/23/14.
 */
public class ProblemCSP {
    LinkedList<Variable> variables;
    Constraints cons;

    ProblemCSP() {

    }

    ProblemCSP(LinkedList<Variable> v, Constraints c) {
        variables = v;
        cons = c;
    }

    protected void cspSearch() {
        LinkedList<Variable> remain =
                new LinkedList<>((Collection<? extends Variable>) variables.clone());
        if (cspDFS(remain)) {
            System.out.println("solution found!!");
            for (int i = 0; i < variables.size(); i++) {
                System.out.println("[" + i + "," + variables.get(i).assignment + "]");
            }
        } else {
            System.out.println("solution not found!!");
        }
    }

    protected boolean cspDFS(LinkedList<Variable> remain) {
        if (remain.size() == 0)
            return true;

        Variable var = pickMRV(remain);
        sortLCV(var, remain);
        // iterate all the remain domain
        for (Domain domain : var.domains) {
            var.assignment = domain.d;
            // try to assign the next variable in the remain
            if (cspDFS(remain))
                return true;  // solution found!
        }
        // not found, reset assignment
        // and put variable back to remain
        var.assignment = -1;
        remain.add(var);
        return false;
    }

    protected Variable pickMRV(LinkedList<Variable> remain){
        Collections.sort(remain);
        Iterator<Variable> it = remain.iterator();
        Variable picked = it.next();
        it.remove();
        return picked;
    }

    protected void sortLCV(Variable var, LinkedList<Variable> remain) {
        // update the remaining domains based on constraints
        updateDomains(var);
        // compute value heuristic by trying
        for (Domain domain : var.domains) {
            var.assignment = domain.d;
            // try to assign the next variable in the remain
            for(Variable v : remain)
                domain.h += remainingDomains(var);
        }
        Collections.sort(var.domains);
    }

    protected int remainingDomains(Variable var){
        int remainDomain = 0;
        for(Iterator<Domain> it = var.domains.iterator(); it.hasNext(); ){
            // try to assign a domain and test if conflict exists
            var.assignment = it.next().d;
            if(!cons.conflictTest(variables, var))
                remainDomain++;
        }
        var.assignment = -1;
        return remainDomain;
    }

    protected int updateDomains(Variable var) {
        for(Iterator<Domain> it = var.domains.iterator(); it.hasNext(); ){
            // try to assign a domain and test if conflict exists
            var.assignment = it.next().d;
            if(cons.conflictTest(variables, var))
                it.remove();
        }
        var.assignment = -1;
        return var.domains.size();
    }
}
