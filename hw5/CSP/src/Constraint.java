import com.sun.jndi.url.iiop.iiopURLContext;

import java.util.HashMap;

/**
 * Created by JackGuan on 2/23/14.
 */
public class Constraint {
    // define the 6 basic relationship
    public static final int EQ = 0, GE = 1, LE = 2, GT = 3, LT = 4, NE = 5;
    // binary contraint, map from a pair of variable to a relationship
    public HashMap<VarPair, Integer> binary;

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
    }

    Constraint() {
        binary = new HashMap<VarPair, Integer>();
    }

    /**
     * addConstraint
     * <p/>
     * Build the constraint graph based on variable name and
     * relationship. Remember to build an in-directed graph
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
        binary.put(new VarPair(var1, var2), r);
        return;
    }

    /**
     * isSatisfied
     * <p/>
     * determine whether this two assignments satisfy the constraint
     *
     * @param ass1
     * @param ass2
     */
    public boolean isSatisfied(Assignment ass1, Assignment ass2) {
        VarPair varpair = new VarPair(ass1.var, ass2.var);
        if (binary.containsKey(varpair)) {
            int r = binary.get(varpair);
            switch (r) {
                case EQ:
                    return ass1.value == ass2.value;
                case GE:
                    return ass1.value >= ass2.value;
                case LE:
                    return ass1.value <= ass2.value;
                case GT:
                    return ass1.value > ass2.value;
                case LT:
                    return ass1.value < ass2.value;
                case NE:
                    return ass1.value != ass2.value;
                default:
                    break;
            }
        }
        System.out.print("No constraint between " + varpair);
        return true;
    }
}
