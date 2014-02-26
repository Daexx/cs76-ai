import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by JackGuan on 2/23/14.
 */
public class Assignment {
    public HashMap<Variable, Integer> var2assignemnt;

    Assignment(){
        var2assignemnt = new HashMap<>();
    }

    public void commit(Variable var, Domain domain){
        var2assignemnt.put(var, domain.d);
    }

    public void undo(Variable var){
        var2assignemnt.remove(var);
    }

    @Override
    public String toString(){
        String s = "";
        for(Variable var : var2assignemnt.keySet()) {
            s = s + "[" + var.id + "->" + var2assignemnt.get(var) + "]\n";
        }
        return s;
    }
}
