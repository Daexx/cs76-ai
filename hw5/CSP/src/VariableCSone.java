import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by JackGuan on 2/26/14.
 */
public class VariableCSone extends Variable {
    public final static int OFFSET = 100;

    VariableCSone(int i) {
        id = i;
    }

    VariableCSone(int i, int ass, int w, int h) {
        id = i;
    }

    VariableCSone(VariableCSone v) {
        id = v.id;
        degree = v.degree;
        domains = (LinkedList<Domain>) v.domains.clone();
        cons = v.cons;
    }

    VariableCSone(int i, LinkedList<Domain> d, Integer ass, Constraints c) {
        id = i;
        domains = d;
        assignment = ass;
        cons = c;
    }

    public int isLeader(){
        return id / OFFSET;
    }

    @Override
    public boolean assign(Domain domain) {
        cons.rmGlobalVar(this); // only exist in 1 global set
        assignment = domain.d;
        cons.addGlobalVar(this);
        return true;
    }

    @Override
    public void undoAssign(){
        cons.rmGlobalVar(this);
        assignment = -1;
    }

    @Override
    public Variable snapshot(){
        return new VariableCSone(this);
    }

    @Override
    public int compareTo(Variable o) {
        int compared = (int) Math.signum(((VariableCSone)o).isLeader() - isLeader());
        if(compared != 0)
            return compared;
        compared = (int) Math.signum(domainSize() - o.domainSize());
        if(compared != 0)
            // return the one with minimum remaining values
            return compared;
        // return the one with maximum degree
        return (int) Math.signum(o.getDegree() - getDegree());
    }
}
