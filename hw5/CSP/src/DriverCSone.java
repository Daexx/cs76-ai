import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by JackGuan on 2/23/14.
 */
public class DriverCSone {
    public static LinkedList<Variable> variables = new LinkedList<>(); // remember to undo when using dfs
    public static LinkedList<Domain> domains = new LinkedList<>();
    public static Constraints constraint = new ConstraintsCSone();

    public static ArrayList<Integer> students = new ArrayList<>(
            Arrays.asList(
                    101,
                    102,
                    1,
                    2,
                    3,
                    4,
                    5,
                    6,
                    7,
                    8,
                    9,
                    10,
                    11,
                    12,
                    13,
                    14
            )
    );


    public static void main(String args[]) throws IOException {

        // read dataset
        BufferedReader br = new BufferedReader(new FileReader("dataset.txt"));
        String line = br.readLine();
        LinkedList<LinkedList<Integer>> data = new LinkedList<>();
        while (line != null) {
            String[] sline = line.split("\t");
            if (sline.length > 1) {
                LinkedList<Integer> lineInts = new LinkedList<>();
                for (int i = 0; i < sline.length; i++)
                    lineInts.add(Integer.parseInt(sline[i]));
                data.add(lineInts);
            }
            line = br.readLine();
        }

        // initiate variables and assigments
        for (Iterator<LinkedList<Integer>> it = data.iterator(); it.hasNext(); ) {
            LinkedList<Integer> student = it.next();
            int id = student.removeFirst();
            domains = new LinkedList<>();
            for (Iterator<Integer> it2 = student.iterator(); it2.hasNext(); ) {
                domains.add(new Domain(it2.next()));
            }
            variables.add(new VariableCSone(id, domains, -1, constraint));
        }

        CSPsolver csp = new CSPsolver(variables, constraint);
        csp.cspSearch();
    }
}
