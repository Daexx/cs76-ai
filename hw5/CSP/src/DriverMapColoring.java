import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by JackGuan on 2/23/14.
 */
public class DriverMapColoring {
    public static LinkedList<Variable> variables = new LinkedList<>(); // remember to undo when using dfs
    public static LinkedList<Domain> domains = new LinkedList<>();
    public static Constraints constraint = new ConstraintsMapColoring();
    public static HashMap<String, Integer> varName2int = new HashMap<>();
    public static HashMap<String, Integer> domainName2int = new HashMap<>();
    public static HashMap<Integer, String> varInt2name = new HashMap<>();
    public static HashMap<Integer, String> domainInt2name = new HashMap<>();
    public static ArrayList<String> domainList = new ArrayList<>(
            Arrays.asList("Red", "Yellow", "Green", "blue")
    );
    public static ArrayList<ArrayList<String>> map2 = new ArrayList<>();
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

    public static void createNameIntMapping(ArrayList<ArrayList<String>> mapused ) {
        for (int i = 0; i < mapused.size(); i++) {
            varName2int.put(mapused.get(i).get(0), i);
            varInt2name.put(i, mapused.get(i).get(0));
        }
        for (int i = 0; i < domainList.size(); i++) {
            domainName2int.put(domainList.get(i), i);
            domainInt2name.put(i, domainList.get(i));
        }
    }

    public static void main(String args[]) throws IOException {


        // read dataset
        BufferedReader br = new BufferedReader(new FileReader("map.txt"));
        String line = br.readLine();
        int iline = 0;
        while (line != null) {
            String[] sline = line.split(",");
            if (sline.length >= 1) {
                ArrayList<String> lineInts = new ArrayList<>();
                for (int i = 0; i < sline.length; i++)
                    lineInts.add(sline[i]);
                map2.add(lineInts);
            }
            line = br.readLine();
        }

        ArrayList<ArrayList<String>> mapused = map;

        // build the name and integer mapping
        createNameIntMapping(mapused);

        // initiate domain
        for (int i = 0; i < domainList.size(); i++) {
            int domainInt = domainName2int.get(domainList.get(i));
            domains.add(new Domain(domainInt));
        }

        // initiate variables and assigments
        for (int i = 0; i < mapused.size(); i++) {
            // no assignment yet, which is -1
            variables.add(new VariableMapColoring(i, (LinkedList<Domain>) domains.clone(), -1));
        }

        // build the constraint
        for (int i = 0; i < mapused.size(); i++) {
            Integer var = varName2int.get(mapused.get(i).get(0));
            for (int j = 1; j < mapused.get(i).size(); j++) {
                Integer adjVar = varName2int.get(mapused.get(i).get(j));
                constraint.addConstraint(variables.get(var), variables.get(adjVar));
            }
            variables.get(var).setDegree(mapused.get(i).size() - 1);
        }

        CSPsolver csp = new CSPsolver(variables, constraint);

        long start = System.currentTimeMillis();
        csp.cspSearch();
        long elapsedTime = System.currentTimeMillis() - start;
        FileOutputStream timecompete = new FileOutputStream("timecompete.txt", true);
        timecompete.write((elapsedTime / 1000. + "\t").getBytes());
        System.out.println("time: " + elapsedTime / 1000.);
        timecompete.close();
    }
}
