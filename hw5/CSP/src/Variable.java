import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by JackGuan on 2/24/14.
 */
public class Variable implements Comparable<Variable> {
    protected int id;
    protected int assignment;
    protected int degree;
    protected LinkedList<Domain> domains;

    public int domainSize(){
        return domains.size();
    }

    public boolean assign(Domain domain) {
        assignment = domain.d;
        return true;
    }

    public void undoAssign(){
        assignment = -1;
    }

    public int getAssignment(){
        return assignment;
    }

    public LinkedList<Domain> getDomains(){
        return domains;
    }

    public int getId(){
        return id;
    }

    public int getDegree(){
        return degree;
    }

    public void setDegree(int d){
        degree = d;
    }

    public ArrayList<Integer> getStates() {
        return new ArrayList<>(Arrays.asList(getAssignment()));
    }

    public Variable snapshot(){
        return null;
    }

    @Override
    public int compareTo(Variable o) {
        int compared = (int) Math.signum(domainSize() - o.domainSize());
        if(compared != 0)
            // return the one with minimum remaining values
            return compared;
        else
            // return the one with maximum degree
            return (int) Math.signum(o.getDegree() - getDegree());
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "[" + id + "->" + assignment + "]";
    }

    @Override
    public boolean equals(Object other) {
        return id == ((Variable) other).id;
    }
}
