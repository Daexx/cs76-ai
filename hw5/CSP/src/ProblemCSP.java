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
        if (remain.size() == 0) return true;

        Variable var = pickMRV(remain);
        sortLCV(var, remain);
        // iterate all the remain domain
        for (Domain domain : var.domains) {
            var.assignment = domain.d;
            // try to assign the next variable in the remain
            if (MAC3Inference(var, remain)) {
                if (cspDFS(remain)) {
                    return true;  // solution found!
                }
            }
        }
        // not found, reset assignment
        // and put variable back to remain
        revertVariable(var, remain);
        return false;
    }

    protected void revertVariable(Variable var, LinkedList<Variable> remain) {
        var.assignment = -1;
        remain.add(var);
    }

    protected boolean MAC3Inference(Variable thisVar, LinkedList<Variable> remain) {
        Variable var = new Variable(thisVar);
        LinkedList<Variable> adjs = cons.adjacents.get(var);
        if(adjs == null) return true;
        adjs = findIntersection(adjs, remain);
        LinkedList<Variable> checkedAdj = new LinkedList<>();

        while (!adjs.isEmpty()) {
            Variable adj = adjs.removeFirst();
            checkedAdj.add(adj);
            if (revise(var, adj)) {
                if (var.domains.isEmpty())
                    return false;
                adjs.addAll(checkedAdj);
                checkedAdj.clear();
            }
        }
        return false;
    }

    protected boolean revise(Variable var, Variable adj) {
        for (Iterator<Domain> it = var.domains.iterator(); it.hasNext(); ) {
            // try to assign a domain and test if conflict exists
            var.assignment = it.next().d;
            if (!cons.consistentTest(var, adj)) {
                it.remove();
                return true;
            }
        }
        var.assignment = -1;
        return false;
    }

    protected LinkedList<Variable> findIntersection(LinkedList<Variable> list1, LinkedList<Variable> list2) {
        LinkedList<Variable> intersection = new LinkedList<>();
        HashSet<Variable> exited = new HashSet<>();

        for (Iterator<Variable> it = list1.iterator(); it.hasNext(); ) {
            exited.add(it.next());
        }
        for (Iterator<Variable> it = list2.iterator(); it.hasNext(); ) {
            Variable found = it.next();
            if (exited.contains(found))
                intersection.add(found);
        }
        return intersection;
    }

    protected Variable pickMRV(LinkedList<Variable> remain) {
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
            domain.h = 0.;
            for (Variable v : remain)
                domain.h += remainingDomains(var);
        }
        Collections.sort(var.domains);
    }

    protected int remainingDomains(Variable var) {
        int remainDomain = 0;
        for (Iterator<Domain> it = var.domains.iterator(); it.hasNext(); ) {
            // try to assign a domain and test if conflict exists
            var.assignment = it.next().d;
            if (!cons.conflictTest(variables, var))
                remainDomain++;
        }
        var.assignment = -1;
        return remainDomain;
    }

    protected int updateDomains(Variable var) {
        for (Iterator<Domain> it = var.domains.iterator(); it.hasNext(); ) {
            // try to assign a domain and test if conflict exists
            var.assignment = it.next().d;
            if (cons.conflictTest(variables, var))
                it.remove();
        }
        var.assignment = -1;
        return var.domains.size();
    }
}
