import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by JackGuan on 2/26/14.
 */
public class VariableCSone extends Variable {
    protected int width, height;
    public final static int OFFSET = 1000;

    VariableCSone(int i) {
        id = i;
    }

    VariableCSone(int i, int ass, int w, int h) {
        id = i;
        width = w;
        height = h;
    }

    VariableCSone(VariableCSone v) {
        id = v.id;
        degree = v.degree;
        domains = (LinkedList<Domain>) v.domains.clone();
        width = v.width;
        height = v.height;
    }

    VariableCSone(int i, LinkedList<Domain> d, int ass, int w, int h) {
        id = i;
        domains = d;
        width = w;
        height = h;
    }

    public int getPixel(int x, int y) {
        return  x * OFFSET + y;
    }

    public int[] getXY(){
        int[] xy = new int[2];

        return xy;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public Variable snapshot(Variable o){
        return new VariableBoradLayout((VariableBoradLayout) o);
    }

}
