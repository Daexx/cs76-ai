import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import javax.swing.*;

import javax.swing.*;


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

    public static LinkedList<Variable> variables = new LinkedList<>(); // remember to undo when using dfs
    public static LinkedList<Domain> domains = new LinkedList<>();
    public static ConstraintsBoardLayout constraint = new ConstraintsBoardLayout();
    public static HashMap<Rectangle, Integer> varName2int = new HashMap<>();
    public static HashMap<Integer, Rectangle> varInt2name = new HashMap<>();
    public static Rectangle domainRange = new Rectangle(10, 3, Color.WHITE);
    public static ArrayList<Rectangle> rects = new ArrayList<Rectangle>(
            Arrays.<Rectangle>asList(
                    new Rectangle(3, 2, Color.RED),
                    new Rectangle(5, 2, Color.YELLOW),
                    new Rectangle(2, 3, Color.ORANGE),
                    new Rectangle(7, 1, Color.BLUE)
            )
    );

    public void paint(Graphics graphics) {
        graphics.setColor(Color.yellow);
        graphics.fillRect(10, 10, 100, 100);
//        graphics.setColor(Color.red);
//        graphics.drawRect(10, 10, 100, 100);
    }

    public static void createNameIntMapping() {
        for (int i = 0; i < rects.size(); i++) {
            varName2int.put(rects.get(i), i);
            varInt2name.put(i, rects.get(i));
        }
    }

    public static void main(String[] args) {
        // build the name and integer mapping
        createNameIntMapping();

        // initiate domain
        for (int i = 0; i < domainRange.w; i++) {
            for (int j = 0; j < domainRange.h; j++) {
                domains.add(new Domain(i * 100 + j));
            }
        }

        // initiate variables and assigments
        for (int i = 0; i < rects.size(); i++) {
            // no assignment yet, which is -1
            variables.add(new Variable(i, (LinkedList<Domain>) domains.clone(), -1));
        }

        // build the constraint
        for (int i = 0; i < rects.size(); i++) {
            Integer var = varName2int.get(rects.get(i));
            for (int j = 0; j < rects.size(); j++) {
                if (i == j) continue;
                Integer adjVar = varName2int.get(rects.get(j));
                constraint.addConstraint(variables.get(var), variables.get(adjVar));
            }
            variables.get(var).degree = rects.get(i).w * rects.get(i).h;
        }

        ProblemCSP csp = new ProblemCSP(variables, constraint);
        csp.cspSearch();

        // drawing
        DriverBoardLayout canvas = new DriverBoardLayout();
        JFrame frame = new JFrame("Circuit layout board problem");
        frame.setSize(domainRange.w * 100, domainRange.h * 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(canvas);
        frame.setVisible(true);
    }
}
