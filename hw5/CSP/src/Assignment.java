/**
 * Created by JackGuan on 2/23/14.
 */
public class Assignment {
    public Integer var;
    public Integer value;
    Assignment(){}
    Assignment(int v, int val){
        var = v;
        value = val;
    }
    Assignment(Assignment ass){
        var = ass.var;
        value = ass.value;
    }
}
