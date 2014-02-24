import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by JackGuan on 2/23/14.
 */
public class DriverMapColoring {
    public static HashSet<Integer> variables = new HashSet<>(); // remember to undo when using dfs
    public static HashSet<Integer> domain = new HashSet<>();
    public static Constraint constraint = new Constraint();
    public static HashMap<String, Integer> varName2int = new HashMap<>();
    public static HashMap<String, Integer> domainName2int = new HashMap<>();
    public static HashMap<Integer, String> varInt2name = new HashMap<>();
    public static HashMap<Integer, String> domainInt2name = new HashMap<>();
    public static ArrayList<String> domainList = new ArrayList<>(
            Arrays.asList("Red", "Yellow", "Green" )
    );
    public static ArrayList<ArrayList<String>> map = new ArrayList<ArrayList<String>>(
            Arrays.asList(
                    new ArrayList<String>(
                            Arrays.asList("WA", "NT", "SA")
                    ),
                    new ArrayList<String>(
                            Arrays.asList("NT", "WA", "SA", "Q")
                    ),
                    new ArrayList<String>(
                            Arrays.asList("SA", "WA", "NT", "Q")
                    ),
                    new ArrayList<String>(
                            Arrays.asList("Q", "NT", "SA", "NSW")
                    ),
                    new ArrayList<String>(
                            Arrays.asList("NSW", "Q", "SA", "V")
                    ),
                    new ArrayList<String>(
                            Arrays.asList("V", "SA", "NSW")
                    ),
                    new ArrayList<String>(
                            Arrays.asList("T")
                    )
            )
    );

    public static void createNameIntMapping() {
        // noted that I choose to use i + 1
        for (int i = 0; i < map.size(); i++) {
            varName2int.put(map.get(i).get(0), i);
            varInt2name.put(i, map.get(i).get(0));
        }
        for (int i = 0; i < domainList.size(); i++) {
            domainName2int.put(domainList.get(i), i);
            domainInt2name.put(i, domainList.get(i));
        }
    }

    public static void main(String args[]) {
        // build the name and integer mapping
        createNameIntMapping();

        // build the constraint
        for (int i = 0; i < map.size(); i++) {
            Integer var = varName2int.get(map.get(i).get(0));
            for (int j = 1; j < map.get(i).size(); j++) {
                Integer adjVar = varName2int.get(map.get(i).get(j));
                constraint.addConstraint(var, "!=", adjVar);
                constraint.addConstraint(adjVar, "!=", var);
            }
        }

        // initiate domain
        for (int i = 0; i < domainList.size(); i++) {
            int domainInt = domainName2int.get(domainList.get(i));
            domain.add(domainInt);
        }

        // initiate assigments
        HashMap<Integer, HashSet<Integer>> domains = new HashMap<>();
        for (int i = 0; i < map.size(); i++) {
            variables.add(i); // no domain assigned yet
            domains.put(i, (HashSet<Integer>) domain.clone());
        }

        ProblemCSP csp = new ProblemCSP(variables, domains, constraint);
        csp.cspSearch();
    }
}
