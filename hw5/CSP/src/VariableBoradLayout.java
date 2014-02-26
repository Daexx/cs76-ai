import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by JackGuan on 2/25/14.
 */
public class VariableBoradLayout implements Variable {
    protected int id;
    protected int assignment;
    protected int degree;
    protected LinkedList<Domain> domains;
    protected int width, height;
    public final static int OFFSET = 1000;

    VariableBoradLayout(int i) {
        id = i;
    }

    VariableBoradLayout(int i, int ass, int w, int h) {
        id = i;
        assignment = ass;
        width = w;
        height = h;
    }

    VariableBoradLayout(VariableBoradLayout v) {
        id = v.id;
        assignment = v.assignment;
        degree = v.degree;
        domains = (LinkedList<Domain>) v.domains.clone();
        width = v.width;
        height = v.height;
    }

    VariableBoradLayout(int i, LinkedList<Domain> d, int ass, int w, int h) {
        id = i;
        domains = d;
        assignment = ass;
        width = w;
        height = h;
    }

    public int getPixel(int x, int y) {
        return assignment + x * OFFSET + y;
    }

    public int[] getXY(){
        int[] xy = new int[2];
        int ass = assignment;
        xy[1] = ass % OFFSET;
        ass /= OFFSET;
        xy[0] = ass % OFFSET;
        return xy;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    @Override
    public int domainSize() {
        return domains.size();
    }

    @Override
    public void assign(Domain domain) {
        assignment = domain.d;
    }

    @Override
    public void undoAssign() {
        assignment = -1;
    }

    @Override
    public int getAssignment() {
        return assignment;
    }

    @Override
    public LinkedList<Domain> getDomains() {
        return domains;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getDegree() {
        return degree;
    }

    @Override
    public void setDegree(int d) {
        degree = d;
    }

    public Variable cshot(Variable o){
        return new VariableBoradLayout((VariableBoradLayout) o);
    }

    @Override
    public ArrayList<Integer> getStates() {
        ArrayList<Integer> states = new ArrayList<>();
        for(int i = 0; i < width; i++){
            states.add(getPixel(i, 0));
            states.add(getPixel(i, height - 1));
        }
        for(int i = 1; i < height - 1; i++){
            states.add(getPixel(0, i));
            states.add(getPixel(width - 1, i));
        }
        return states;
    }

    @Override
    public Variable snapshot(){
        return new VariableBoradLayout(this);
    }

    @Override
    public int compareTo(Variable o) {
        int compared = (int) Math.signum(domainSize() - o.domainSize());
        if (compared != 0)
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
        return id == ((VariableBoradLayout) other).id;
    }
}
