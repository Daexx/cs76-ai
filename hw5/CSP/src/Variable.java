import com.sun.scenario.effect.impl.state.LinearConvolveKernel;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import oracle.jrockit.jfr.jdkevents.throwabletransform.ConstructorTracerWriter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by JackGuan on 2/24/14.
 */
public class Variable implements Comparable<Variable> {
    public int id;
    public int assignment;
    public int degree;
    public LinkedList<Domain> domains;

    Variable(int i){
        id = i;
    }

    Variable(int i, int ass){
        id = i;
        assignment = ass;
    }

    Variable(int i, LinkedList<Domain> d, int ass){
        id = i;
        domains = d;
        assignment = ass;
    }

    public void updateDegree(){

    }



    @Override
    public int compareTo(Variable o) {
        int compared = (int) Math.signum(domains.size() - o.domains.size());
        if(compared != 0)
            return compared;
        else
            return (int) Math.signum(degree - o.degree);
    }
}
