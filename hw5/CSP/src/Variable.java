import java.util.LinkedList;

/**
 * Created by JackGuan on 2/24/14.
 */
public class Variable implements Comparable<Variable> {
    public int id;
    public int assignment;
    public int degree;
    public LinkedList<Domain> domains;

    Variable(int i){
        id = i;
    }

    Variable(int i, int ass){
        id = i;
        assignment = ass;
    }

    Variable(Variable v){
        id = v.id;
        assignment = v.assignment;
        degree = v.degree;
        domains = (LinkedList<Domain>) v.domains.clone();
    }

    Variable(int i, LinkedList<Domain> d, int ass){
        id = i;
        domains = d;
        assignment = ass;
    }

    @Override
    public int compareTo(Variable o) {
        int compared = (int) Math.signum(domains.size() - o.domains.size());
        if(compared != 0)
            // return the one with minimum remaining values
            return compared;
        else
            // return the one with maximum degree
            return (int) Math.signum(o.degree - degree);
    }
}
