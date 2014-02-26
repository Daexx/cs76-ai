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
        width = w;
        height = h;
    }

    VariableBoradLayout(VariableBoradLayout v) {
        id = v.id;
        degree = v.degree;
        domains = (LinkedList<Domain>) v.domains.clone();
        width = v.width;
        height = v.height;
    }

    VariableBoradLayout(int i, LinkedList<Domain> d, int ass, int w, int h) {
        id = i;
        domains = d;
        width = w;
        height = h;
    }

    public int getPixel(int x, int y) {
        return 0 + x * OFFSET + y;
    }

    public int[] getXY(){
        int[] xy = new int[2];
        int ass = 0;
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

//    @Override
//    public ArrayList<Integer> getStates() {
//        ArrayList<Integer> states = new ArrayList<>();
//        for(int i = 0; i < width; i++){
//            states.add(getPixel(i, 0));
//            states.add(getPixel(i, height - 1));
//        }
//        for(int i = 1; i < height - 1; i++){
//            states.add(getPixel(0, i));
//            states.add(getPixel(width - 1, i));
//        }
//        return states;
//    }

    @Override
    public Variable snapshot(){
        return new VariableBoradLayout(this);
    }
}
