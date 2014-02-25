import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
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
    public static LinkedList<Variable> variables = new LinkedList<>(); // remember to undo when using dfs
    public static LinkedList<Domain> domains = new LinkedList<>();
    public static ConstraintsMapColoring constraint = new ConstraintsMapColoring();
    public static HashMap<String, Integer> varName2int = new HashMap<>();
    public static HashMap<String, Integer> domainName2int = new HashMap<>();
    public static HashMap<Integer, String> varInt2name = new HashMap<>();
    public static HashMap<Integer, String> domainInt2name = new HashMap<>();
    public static ArrayList<String> domainList = new ArrayList<>(
            Arrays.asList("Red", "Yellow", "Green", "Blue")
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

    public void paint(Graphics graphics) {
        graphics.setColor(Color.yellow);
        graphics.fillRect(10, 10, 100, 100);
//        graphics.setColor(Color.red);
//        graphics.drawRect(10, 10, 100, 100);
    }

    public static void main(String[] args) {
        DriverBoardLayout canvas = new DriverBoardLayout();
        JFrame frame = new JFrame("Circuit layout board problem");
        frame.setSize(400, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(canvas);
        frame.setVisible(true);
    }
}
