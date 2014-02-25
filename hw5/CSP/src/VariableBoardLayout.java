import java.util.LinkedList;

/**
 * Created by JackGuan on 2/24/14.
 */
public class VariableBoardLayout implements Comparable<VariableBoardLayout> {
    public int id;
    public int assignment;
    public int degree;
    public LinkedList<Domain> domains;

    VariableBoardLayout(int i){
        id = i;
    }

    VariableBoardLayout(int i, int ass){
        id = i;
        assignment = ass;
    }

    VariableBoardLayout(VariableBoardLayout v){
        id = v.id;
        assignment = v.assignment;
        degree = v.degree;
        domains = (LinkedList<Domain>) v.domains.clone();
    }

    VariableBoardLayout(int i, LinkedList<Domain> d, int ass){
        id = i;
        domains = d;
        assignment = ass;
    }



    @Override
    public int compareTo(VariableBoardLayout o) {
        int compared = (int) Math.signum(domains.size() - o.domains.size());
        if(compared != 0)
            // return the one with minimum remaining values
            return compared;
        else
            // return the one with maximum degree
            return (int) Math.signum(o.degree - degree);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "[" + id + "," + assignment + "]";
    }

    @Override
    public boolean equals(Object other) {
        return id == ((VariableBoardLayout) other).id;
    }
}
