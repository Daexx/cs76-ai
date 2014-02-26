import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by JackGuan on 2/24/14.
 */
public class VariableMapColoring implements Variable{
    protected int id;
    protected int assignment;
    protected int degree;
    protected LinkedList<Domain> domains;

    VariableMapColoring(){}

    VariableMapColoring(int i){
        id = i;
    }

    VariableMapColoring(int i, int ass){
        id = i;
        assignment = ass;
    }

    VariableMapColoring(VariableMapColoring v){
        id = v.id;
        assignment = v.assignment;
        degree = v.degree;
        domains = (LinkedList<Domain>) v.domains.clone();
    }

    VariableMapColoring(int i, LinkedList<Domain> d, int ass){
        id = i;
        domains = d;
        assignment = ass;
    }

    @Override
    public int domainSize(){
        return domains.size();
    }

    @Override
    public void assign(Domain domain){
        assignment = domain.d;
    }

    @Override
    public void undoAssign(){
        assignment = -1;
    }

    @Override
    public int getAssignment(){
        return assignment;
    }

    @Override
    public LinkedList<Domain> getDomains(){
        return domains;
    }

    @Override
    public int getId(){
        return id;
    }

    @Override
    public int getDegree(){
        return degree;
    }

    @Override
    public void setDegree(int d){
        degree = d;
    }

    @Override
    public ArrayList<Integer> getStates() {
        return new ArrayList<>(Arrays.asList(getAssignment()));
    }

    @Override
    public Variable snapshot(){
        return new VariableMapColoring(this);
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
        return id == ((VariableMapColoring) other).id;
    }
}
