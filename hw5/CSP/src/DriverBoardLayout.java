import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * Created by JackGuan on 2/23/14.
 */
public class DriverBoardLayout extends JPanel {
    public static class Rectangle {
        public int w, h;
        Color c;

        Rectangle(int a, int b, Color e) {
            w = a;
            h = b;
            c = e;
        }
    }

    public static int scale = 50;
    public static LinkedList<Variable> variables = new LinkedList<>(); // remember to undo when using dfs
    public static LinkedList<VariableBoradLayout> solutions = new LinkedList<>(); // remember to undo when using dfs
    public static LinkedList<Domain> domains = new LinkedList<>();
    public static Constraints constraint = new ConstraintsBoardLayout();
    public static HashMap<Rectangle, Integer> varName2int = new HashMap<>();
    public static HashMap<Integer, Rectangle> varInt2name = new HashMap<>();
    public static Rectangle domainRange = new Rectangle(10, 10, Color.WHITE);
    public static Random randLayout = new Random(152);
//    public static ArrayList<Rectangle> rects = new ArrayList<Rectangle>(
//            Arrays.<Rectangle>asList(
//                    new Rectangle(3, 2, Color.RED),
//                    new Rectangle(5, 2, Color.YELLOW),
//                    new Rectangle(2, 3, Color.ORANGE),
//                    new Rectangle(7, 1, Color.BLUE)
//            )
//    );

    public static ArrayList<Rectangle> rects = new ArrayList<Rectangle>(
            Arrays.<Rectangle>asList(
                    new Rectangle(randLayout.nextInt(7), randLayout.nextInt(7), new Color(randLayout.nextInt(255), randLayout.nextInt(255), randLayout.nextInt(255))),
                    new Rectangle(randLayout.nextInt(7), randLayout.nextInt(7), new Color(randLayout.nextInt(255), randLayout.nextInt(255), randLayout.nextInt(255))),
                    new Rectangle(randLayout.nextInt(7), randLayout.nextInt(7), new Color(randLayout.nextInt(255), randLayout.nextInt(255), randLayout.nextInt(255))),
                    new Rectangle(randLayout.nextInt(7), randLayout.nextInt(7), new Color(randLayout.nextInt(255), randLayout.nextInt(255), randLayout.nextInt(255))),
                    new Rectangle(randLayout.nextInt(7), randLayout.nextInt(7), new Color(randLayout.nextInt(255), randLayout.nextInt(255), randLayout.nextInt(255))),
                    new Rectangle(randLayout.nextInt(7), randLayout.nextInt(7), new Color(randLayout.nextInt(255), randLayout.nextInt(255), randLayout.nextInt(255))),
                    new Rectangle(randLayout.nextInt(7), randLayout.nextInt(7), new Color(randLayout.nextInt(255), randLayout.nextInt(255), randLayout.nextInt(255))),
                    new Rectangle(4, 4, new Color(0, 0, 0)),
                    new Rectangle(randLayout.nextInt(7), randLayout.nextInt(7), new Color(randLayout.nextInt(255), randLayout.nextInt(255), randLayout.nextInt(255)))
            )
    );

    public void paint(Graphics graphics) {
        for (Iterator<Variable> it = variables.iterator(); it.hasNext(); ) {
            VariableBoradLayout sln = (VariableBoradLayout) it.next();
            graphics.setColor(rects.get(sln.getId()).c);
            int[] xy = sln.getXY();
            graphics.fillRect(xy[0] * scale, (domainRange.h - xy[1]) * scale, sln.getWidth() * scale, -sln.getHeight() * scale);
//            graphics.setColor(Color.BLACK);
//            graphics.drawRect(xy[0] * scale, (domainRange.h - xy[1]) * scale, sln.getWidth() * scale, -sln.getHeight() * scale);
        }

    }

    public static void createNameIntMapping() {
        for (int i = 0; i < rects.size(); i++) {
            varName2int.put(rects.get(i), i);
            varInt2name.put(i, rects.get(i));
        }
    }

    public static void main(String[] args) throws IOException {
        // build the name and integer mapping
        createNameIntMapping();


        // initiate variables and domains
        for (int i = 0; i < rects.size(); i++) {
            // initiate domain
            domains = new LinkedList<>();
            for (int x = 0; x <= domainRange.w - rects.get(i).w; x++) {
                for (int y = 0; y <= domainRange.h - rects.get(i).h; y++) {
                    domains.add(new Domain(x * VariableBoradLayout.OFFSET + y));
                }
            }
            // no assignment yet, which is -1
            variables.add(new VariableBoradLayout(i, domains, -1, rects.get(i).w, rects.get(i).h));
        }

        // build the constraint
        for (int i = 0; i < rects.size(); i++) {
            Integer var = varName2int.get(rects.get(i));
            for (int j = 0; j < rects.size(); j++) {
                if (i == j) continue;
                Integer adjVar = varName2int.get(rects.get(j));
                constraint.addConstraint(variables.get(var), variables.get(adjVar));
            }
            variables.get(var).setDegree(rects.get(i).w * rects.get(i).h);
        }

        CSPsolver csp = new CSPsolver(variables, constraint);


        long start = System.currentTimeMillis();
        boolean found = csp.cspSearch();
        long elapsedTime = System.currentTimeMillis() - start;
        FileOutputStream timecompete = new FileOutputStream("timecompetelayout.txt", true);
        if(found)
            timecompete.write((elapsedTime / 1000. + "\t").getBytes());
        System.out.println("time: " + elapsedTime / 1000.);
        timecompete.close();

        // drawing
        DriverBoardLayout canvas = new DriverBoardLayout();
        JFrame frame = new JFrame("Circuit layout board problem");
        frame.setSize((domainRange.w + 1) * scale, (1 + domainRange.h) * scale);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(canvas);
        frame.setVisible(true);
    }
}
