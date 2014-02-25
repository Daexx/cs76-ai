import sun.util.resources.cldr.hi.TimeZoneNames_hi;

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
        // pick a Minimum Remaining Variable
        Variable var = pickMRV(remain);
        // update domain and sort the values based on Least Constraining
        sortLCV(var, remain);
        // iterate all the remain domain
        for (Domain domain : var.domains) {
            var.assignment = domain.d;
            // try to assign the next variable in the remain
            LinkedList<Variable> snapshot = (LinkedList<Variable>) remain.clone();
            if (MAC3Inference(var, snapshot)) {
                if (cspDFS(snapshot)) {
                    return true;  // solution found!
                }
            }
        }
        // not found, reset assignment and put variable back to remain
        undoAssignment(var, remain);
        return false;
    }

    protected void undoAssignment(Variable var, LinkedList<Variable> remain) {
        var.assignment = -1;
        remain.add(var);
    }

    protected boolean MAC3Inference(Variable thisVar, LinkedList<Variable> remain) {
        Variable var = new Variable(thisVar);
        LinkedList<Constraints.ArcPair> arcs = cons.getAdjArcs(var, remain);

        while (!arcs.isEmpty()) {
            Constraints.ArcPair arc = arcs.removeFirst();
            if (revise(arc.first, arc.second)) {
                if (var.domains.isEmpty())
                    return false;
                arcs.addAll(cons.getAdjArcsInvert(arc.first, arc.second, remain));
            }
        }
        return true;
    }

    protected boolean revise(Variable var, Variable adj) {
        for (Iterator<Domain> it = var.domains.iterator(); it.hasNext(); ) {
            // try to assign a domain and test if conflict exists
            var.assignment = it.next().d;
            if (!cons.consistentTest(var, adj)) {
                it.remove();
                var.assignment = -1;
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
                domain.h += remainingDomains(v);
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
        return var.domains.size();
    }
}
