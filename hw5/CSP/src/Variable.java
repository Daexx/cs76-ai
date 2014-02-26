import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by JackGuan on 2/24/14.
 */
public interface Variable extends Comparable<Variable> {

    public int domainSize();

    public void assign(Domain domain);

    public void undoAssign();

    public int getAssignment();

    public LinkedList<Domain> getDomains();

    public int getId();

    public int getDegree();

    public void setDegree(int d);

    // get state based on the assignment
    public ArrayList<Integer> getStates();

    public Variable snapshot();

    @Override
    public int compareTo(Variable o);
}
