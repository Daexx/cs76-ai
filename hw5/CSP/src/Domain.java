/**
 * Created by JackGuan on 2/24/14.
 */
public class Domain implements Comparable<Domain> {
    int d;
    Double h;

    Domain(int d){
        this.d = d;
        this.h = 0.;
    }

    @Override
    public int compareTo(Domain o) {
        return (int) Math.signum(h -o. h);
    }
}