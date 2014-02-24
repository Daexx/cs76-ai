import com.sun.jndi.url.iiop.iiopURLContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by JackGuan on 2/23/14.
 */
public interface Constraints {
    public void addConstraint(Integer var1, String relatinshp, Integer var2);
    public boolean isSatisfied(Variable var1, Variable var2);
    public boolean conflictTest(LinkedList<Variable> vars, Variable var);
    public boolean conflictTest(LinkedList<Variable> vars);

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
