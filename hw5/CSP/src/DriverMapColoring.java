import java.util.*;

/**
 * Created by JackGuan on 2/23/14.
 */
public class DriverMapColoring {
    public static LinkedList<Variable> variables = new LinkedList<>(); // remember to undo when using dfs
    public static LinkedList<Domain> domains = new LinkedList<>();
    public static ConstraintsMapColoring constraint = new ConstraintsMapColoring();
    public static HashMap<String, Integer> varName2int = new HashMap<>();
    public static HashMap<String, Integer> domainName2int = new HashMap<>();
    public static HashMap<Integer, String> varInt2name = new HashMap<>();
    public static HashMap<Integer, String> domainInt2name = new HashMap<>();
    public static ArrayList<String> domainList = new ArrayList<>(
            Arrays.asList("Red", "Yellow", "Green" , "Blue")
    );
    public static ArrayList<ArrayList<String>> map = new ArrayList<>(
            Arrays.asList(
                    new ArrayList<>(
                            Arrays.asList("WA", "NT", "SA")
                    ),
                    new ArrayList<>(
                            Arrays.asList("NT", "WA", "SA", "Q")
                    ),
                    new ArrayList<>(
                            Arrays.asList("SA", "WA", "NT", "Q", "NSW")
                    ),
                    new ArrayList<>(
                            Arrays.asList("Q", "NT", "SA", "NSW")
                    ),
                    new ArrayList<>(
                            Arrays.asList("NSW", "Q", "SA", "V")
                    ),
                    new ArrayList<>(
                            Arrays.asList("V", "SA", "NSW")
                    ),
                    new ArrayList<>(
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

        // initiate domain
        for (int i = 0; i < domainList.size(); i++) {
            int domainInt = domainName2int.get(domainList.get(i));
            domains.add(new Domain(domainInt));
        }

        // initiate variables and assigments
        for (int i = 0; i < map.size(); i++) {
            // no assignment yet, which is -1
            variables.add(new Variable(i, (LinkedList<Domain>) domains.clone(), -1));
        }

        // build the constraint
        for (int i = 0; i < map.size(); i++) {
            Integer var = varName2int.get(map.get(i).get(0));
            for (int j = 1; j < map.get(i).size(); j++) {
                Integer adjVar = varName2int.get(map.get(i).get(j));
                constraint.addConstraint(variables.get(var), "!=", variables.get(adjVar));
            }
            variables.get(var).degree = map.get(i).size() - 1;
        }

        ProblemCSP csp = new ProblemCSP(variables, constraint);
        csp.cspSearch();
    }
}
