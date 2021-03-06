import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by JackGuan on 2/25/14.
 */
public class VariableBoradLayout extends Variable {
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
    public ArrayList<Integer> getStates() {
        ArrayList<Integer> states = new ArrayList<>();
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                states.add(getPixel(i, j));
            }
        }
        return states;
    }

    @Override
    public Variable snapshot(){
        return new VariableBoradLayout(this);
    }
}
