import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by JackGuan on 2/24/14.
 */
public class VariableMapColoring extends Variable{


    VariableMapColoring(){}

    VariableMapColoring(int i){
        id = i;
    }

    VariableMapColoring(int i, int ass){
        id = i;
    }

    VariableMapColoring(VariableMapColoring v){
        id = v.id;
        degree = v.degree;
        domains = (LinkedList<Domain>) v.domains.clone();
    }

    VariableMapColoring(int i, LinkedList<Domain> d, int ass){
        id = i;
        domains = d;
    }

    @Override
    public Variable snapshot(){
        return new VariableMapColoring(this);
    }

}
