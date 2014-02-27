import java.util.*;

/**
 * Created by JackGuan on 2/23/14.
 */
public class CSPsolver {
    protected LinkedList<Variable> variables;
    protected Constraints cons;
    protected int nodeCnt = 0;

    CSPsolver() {

    }

    CSPsolver(LinkedList<Variable> v, Constraints c) {
        variables = v;
        cons = c;
    }

    protected void cspSearch() {
        LinkedList<Variable> remain =
                new LinkedList<>((Collection<? extends Variable>) variables.clone());
        if (cspDFS(remain)) {
            System.out.println("solution found with " + nodeCnt + " nodes explored");
            for (int i = 0; i < variables.size(); i++) {
                System.out.println(variables.get(i));
            }
        } else {
            System.out.println("solution not found!!");
        }
    }

    protected boolean cspDFS(LinkedList<Variable> remain) {
        if (remain.size() == 0) return true;
        nodeCnt++;
        // pick a Minimum Remaining Variable
        Variable var = pickMRV(remain);

//        System.out.println(var.getId() + ": nodes explored: " + ++nodeCnt);
        // update domain and sort the values based on Least Constraining
        sortLCV(var, remain);
        // iterate all the remain domain
        for (Domain domain : var.getDomains()) {
            var.assign(domain);
            // try to assign the next variable in the remain
            if (MAC3Inference(var, remain)) {
                if (cspDFS(remain)) {
                    return true;  // solution found!
                }
            }
        }
        // not found, reset assignment and put variable back to remain
        undoAssignment(var, remain);
        return false;
    }

    protected void undoAssignment(Variable var, LinkedList<Variable> remain) {
        var.undoAssign();
        var.domainsRecover();
        remain.add(var);
    }

    protected boolean MAC3Inference(Variable thisVar, LinkedList<Variable> remain) {
        Variable var = thisVar.snapshot();
        int backup = thisVar.getAssignment();
        LinkedList<Constraints.ArcPair> arcs = cons.getAdjArcs(var, remain);

        while (arcs != null && !arcs.isEmpty()) {
            Constraints.ArcPair arc = arcs.removeFirst();
            if (revise(arc.first, arc.second)) {
                if (var.getDomains().isEmpty())
                    return false;
                arcs.addAll(cons.getAdjArcsInvert(arc.first, arc.second, remain));
            }
        }
        thisVar.assign(new Domain(backup));
        return true;
    }

    protected boolean revise(Variable var, Variable adj) {
        for (Iterator<Domain> it = var.getDomains().iterator(); it.hasNext(); ) {
            // try to assign a domain and test if conflict exists
            var.assign(it.next());
            if (!cons.consistentTest(var, adj)) {
                it.remove();
                var.undoAssign();
                return true;
            }
        }
        var.undoAssign();
        return false;
    }


    protected Variable pickMRV(LinkedList<Variable> remain) {
        Collections.sort(remain);
        return remain.removeFirst();
    }

    protected void sortLCV(Variable var, LinkedList<Variable> remain) {
        // update the remaining domains based on constraints
        updateDomains(var);
        // compute value heuristic by trying
        for (Domain domain : var.getDomains()) {
            var.assign(domain);
            // try to assign the next variable in the remain
            domain.h = 0.;
            for (Variable v : remain)
                domain.h += remainingDomains(v);
        }
        var.undoAssign();
        Collections.sort(var.getDomains());
    }

    protected int remainingDomains(Variable var) {
        int remainDomain = 0;
        for (Iterator<Domain> it = var.getDomains().iterator(); it.hasNext(); ) {
            // try to assign a domain and test if conflict exists
            var.assign(it.next());
            if (!cons.conflictTest(var))
                remainDomain++;
        }
        var.undoAssign();
        return remainDomain;
    }

    protected int updateDomains(Variable var) {
        var.setDomainsBackup();
        for (Iterator<Domain> it = var.getDomains().iterator(); it.hasNext(); ) {
            // try to assign a domain and test if conflict exists
            var.assign(it.next());
            if (cons.conflictTest(var))
                it.remove();
        }
        var.undoAssign();
        return var.domainSize();
    }
}
