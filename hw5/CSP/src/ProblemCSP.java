import java.util.*;

/**
 * Created by JackGuan on 2/23/14.
 */
public class ProblemCSP {
    LinkedList<Variable> variables;
    HashSet<Variable> assigened;
    Constraints cons;

    ProblemCSP() {

    }

    ProblemCSP(LinkedList<Variable> v, Constraints c) {
        assigened = new HashSet<>();
        variables = v;
        cons = c;
    }

    protected void cspSearch() {
        if (cspDFS(0)) {
            System.out.println("solution found!!");
            for (int i = 0; i < variables.size(); i++) {
                System.out.println("[" + i + "," + variables.get(i).assignment + "]");
            }
        } else {
            System.out.println("solution not found!!");
        }
    }

    protected boolean cspDFS(int varId) {
        if (assigened.size() == variables.size())
            return true;

        Variable var = variables.get(varId);
        // keep track of this assigment
        assigened.add(var);
        // shrink the domain of based on constraints
        updateDomains(var);
        // iterate all the remain domain
        for (Domain domain : var.domains) {
            var.assignment = domain.d;
            // try to assign the next variable
            if (cspDFS(varId + 1))
                return true;  // solution found!
        }
        // if not found, reset the assignment
        var.assignment = -1;
        assigened.remove(var);
        return false;
    }

    protected int updateDomains(Variable var) {
        for(Iterator<Domain> it = var.domains.iterator(); it.hasNext(); ){
            // try to assign a domain and test if conflict exists
            var.assignment = it.next().d;
            if(cons.conflictTest(variables, var))
                it.remove();
        }
        return var.domains.size();
    }
}
