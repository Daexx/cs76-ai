import com.sun.jndi.url.iiop.iiopURLContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by JackGuan on 2/23/14.
 */
public class Constraint {
    // define the 6 basic relationship
    public static final int EQ = 0, GE = 1, LE = 2, GT = 3, LT = 4, NE = 5;
    // binary contraint, map from a pair of variable to a relationship
    public HashMap<VarPair, Integer> binary;
    // contraint graph
    public HashMap<Integer, ArrayList<Integer>> adjacents;

    Constraint(){
        binary = new HashMap<>();
        adjacents = new HashMap<>();
    }

    /**
     * addConstraint
     * <p/>
     * Build the constraint graph based on variable name and
     * relationship. Remember to build an in-directed graph,
     * so you need to call this twice every time
     *
     * @param var1       the first variable
     * @param relatinshp the relationship
     * @param var2       the second variable
     */
    public void addConstraint(Integer var1, String relatinshp, Integer var2) {
        Integer r;
        if (relatinshp.equals("=") || relatinshp.equals("==")) {
            r = EQ;
        } else if (relatinshp.equals(">=")) {
            r = GE;
        } else if (relatinshp.equals("<=")) {
            r = LE;
        } else if (relatinshp.equals(">")) {
            r = GT;
        } else if (relatinshp.equals("<")) {
            r = LT;
        } else if (relatinshp.equals("!=")) {
            r = NE;
        } else {
            System.out.print("illegal relation ship!");
            r = -1;
        }
        // add the relationship
        binary.put(new VarPair(var1, var2), r);

        // build the graph
        if(!adjacents.containsKey(var1)) adjacents.put(var1, new ArrayList<Integer>());
        adjacents.get(var1).add(var2);
        return;
    }

    public boolean isSatisfied(int var1, int val1, int var2, int val2){
        VarPair varpair = new VarPair(var1, var2);
        if (binary.containsKey(varpair)) {
            int r = binary.get(varpair);
            switch (r) {
                case EQ:
                    return val1 == val2;
                case GE:
                    return val1 >= val2;
                case LE:
                    return val1 <= val2;
                case GT:
                    return val1 > val2;
                case LT:
                    return val1 < val2;
                case NE:
                    return val1 != val2;
                default:
                    break;
            }
        }
        // System.out.println("No constraint between " + varpair);
        return true;
    }

    public class VarPair {
        Integer first;
        Integer second;

        VarPair(Integer v1, Integer v2) {
            first = v1;
            second = v2;
        }

        @Override
        public int hashCode() {
            return first * Config.VAR_NUM_SUPRT + second;
        }

        @Override
        public String toString() {
            return "[" + first + "," + second + "]";
        }

        @Override
        public boolean equals(Object other) {
            return first == ((VarPair) other).first
                    && second == ((VarPair) other).second;
        }
    }
}
