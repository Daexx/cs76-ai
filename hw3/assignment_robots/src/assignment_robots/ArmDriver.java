package assignment_robots;

import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.shape.Polygon;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import assignment_robots.SearchProblem.SearchNode;
import assignment_robots.RoadMapProblem.RoadMapNode;

public class ArmDriver extends Application {
	// default window size
	protected int winw = 600;
	protected int winh = 400;

	public void addPolygon(Group g, Double[] points) {
		Polygon p = new Polygon();
		p.getPoints().addAll(points);

		g.getChildren().add(p);
	}

	// plot a ArmRobot;
	public void plotArmRobot(Group g, Double[] config) {
		ArmRobot arm = new ArmRobot(config);
		//System.out.println("plot arm" + arm);
		double[][] current;
		Double[] to_add;
		Polygon p;
		for (int i = 1; i <= arm.getLinks(); i++) {
			current = arm.getLinkBox(i);

			to_add = new Double[2 * current.length];
			for (int j = 0; j < current.length; j++) {
				// System.out.println("plotArmRobot: " + current[j][0] + ", " +
				// current[j][1]);
				to_add[2 * j] = current[j][0];
				// to_add[2*j+1] = current[j][1];
				to_add[2 * j + 1] = winh - current[j][1];
			}
			p = new Polygon();
			p.getPoints().addAll(to_add);
			p.setStroke(Color.BLUE);
			p.setFill(Color.LIGHTBLUE);
			g.getChildren().add(p);
		}

	}
	
	// plot a ArmRobot;
	public void plotArmRobotPath(Group g, Double[] config, int length, int idx) {
		ArmRobot arm = new ArmRobot(config);
		//System.out.println("plot arm" + arm);
		double[][] current;
		Double[] to_add;
		Polygon p;
		for (int i = 1; i <= arm.getLinks(); i++) {
			current = arm.getLinkBox(i);

			to_add = new Double[2 * current.length];
			for (int j = 0; j < current.length; j++) {
				// System.out.println("plotArmRobot: " + current[j][0] + ", " +
				// current[j][1]);
				to_add[2 * j] = current[j][0];
				// to_add[2*j+1] = current[j][1];
				to_add[2 * j + 1] = winh - current[j][1];
			}
			p = new Polygon();
			p.getPoints().addAll(to_add);
			p.setStroke(Color.WHITE);
			p.setFill(Color.rgb(255 - 255 * idx / length, 255 - 255 * idx / length, 125));
			g.getChildren().add(p);
		}

	}

	public void plotArmRobotSample(Group g, ArmRobot arm, Double[] config) {
		arm.set(config);
		double[][] current;
		Double[] to_add;
		Polygon p;
		for (int i = 1; i <= arm.getLinks(); i++) {
			current = arm.getLinkBox(i);

			to_add = new Double[2 * current.length];
			for (int j = 0; j < current.length; j++) {
				// System.out.println("plotArmRobot: " + current[j][0] + ", " +
				// current[j][1]);
				to_add[2 * j] = current[j][0];
				// to_add[2*j+1] = current[j][1];
				to_add[2 * j + 1] = winh - current[j][1];
			}
			p = new Polygon();
			p.getPoints().addAll(to_add);
			p.setStroke(Color.rgb(200, 200, 200));
			p.setFill(Color.WHITE);
			g.getChildren().add(p);
		}

	}

	public void plotWorld(Group g, World w) {
		int len = w.getNumOfObstacles();
		double[][] current;
		Double[] to_add;
		Polygon p;

		// plot the walls
		current = w.wall.get();
		to_add = new Double[2 * current.length];
		for (int j = 0; j < current.length; j++) {
			to_add[2 * j] = current[j][0];
			// to_add[2*j+1] = current[j][1];
			to_add[2 * j + 1] = winh - current[j][1];
		}
		p = new Polygon();
		p.getPoints().addAll(to_add);
		p.setStroke(Color.GRAY);
		p.setFill(Color.WHITE);
		g.getChildren().add(p);

		for (int i = 0; i < len; i++) {
			current = w.getObstacle(i);
			to_add = new Double[2 * current.length];
			for (int j = 0; j < current.length; j++) {
				to_add[2 * j] = current[j][0];
				// to_add[2*j+1] = current[j][1];
				to_add[2 * j + 1] = winh - current[j][1];
			}
			p = new Polygon();
			p.getPoints().addAll(to_add);
			g.getChildren().add(p);
		}
	}

	// The start function; will call the drawing;
	// You can run your PRM or RRT to find the path;
	// call them in start; then plot the entire path using
	// interfaces provided;
	@Override
	public void start(Stage primaryStage) {

		// setting up javafx graphics environments;
		primaryStage.setTitle("CS 76 2D world");

		Group root = new Group();
		Scene scene = new Scene(root, winw, winh);

		primaryStage.setScene(scene);

		Group g = new Group();

		// setting up the world;

		// creating polygon as obstacles;

		double bg[][] = { { 0, 0 }, { 0, winh },
				{ winw, winh }, { winw, 0 }, { 0, 0 } };
		Poly bgc = new Poly(bg);

		double al = .04, bet = 0.85, basey = 0.;
		double a[][] = { { winw/2 - al * winw, basey },
				{ winw/2 - al * winw, bet * winh + basey},
				{ winw/2 + al * winw, bet * winh + basey },
				{ winw/2 + al * winw, basey} };
		Poly obstacle1 = new Poly(a);

		al = .35; bet = 0.1; basey = 0.75 * winh;
		double b[][] = { { winw/2 - al * winw, basey },
				{ winw/2 - al * winw, bet * winh + basey},
				{ winw/2 + al * winw, bet * winh + basey },
				{ winw/2 + al * winw, basey} };
		Poly obstacle2 = new Poly(b);

		double c[][] = { { 110, 220 }, { 250, 380 }, { 320, 220 } };
		Poly obstacle3 = new Poly(c);

		double wa[][] = { { 0, 0 }, { 0, winh },
				{ winw, winh }, { winw, 0 }, { 0, 0 },
				{ -100, -100 }, { winw + 100, -100 },
				{ winw + 100, winh + 100 },
				{ -100, winh + 100 }, { -100, -100 } };
		Poly wall = new Poly(wa);

		// Declaring a world;
		World w = new World(winw, winh);
		// Add obstacles to the world;
		w.addObstacle(obstacle1);
				w.addObstacle(obstacle2);
//				w.addObstacle(obstacle3);
		w.addWall(bgc);

		plotWorld(g, w);

		ArmRobot arm = new ArmRobot(2);

		Double[] config1 = { 10., 20., 80., Math.PI / 4, 80., Math.PI / 4 };
		Double[] config2 = { 425., 50., 80., .1, 80., .2 };

		/*		Double[] config1 = {500, 300, 80, Math.PI/4, 80, Math.PI/4};
				Double[] config2 = {450, 250, 80, .1, 80, .2};*/

		arm.set(config2);

		// Plan path between two configurations;
		ArmLocalPlanner ap = new ArmLocalPlanner();

		// get the time to move from config1 to config2;
		Double time = ap.moveInParallel(config1, config2);
		System.out.println("time: " + time);

		// plot robot arm

		List<SearchNode> solutionPath = null;
		RoadMapProblem rmp = new RoadMapProblem(w, config1, config2, 1000, 10);
		solutionPath = rmp.astarSearch();
		
		// System.out.println("size: " + rmp.samplings.size());
		for (ArmRobot ar : rmp.samplings) {
			// System.out.println(ar);
			plotArmRobotSample(g, ar, ar.config);
		}
		
		if (solutionPath == null)
			System.out.println("try to debug!!");
		else {
			System.out.println("path lenght: " + solutionPath.size());
			int i = 0;
			for (SearchNode sn : solutionPath) {
				RoadMapNode thissn = (RoadMapNode) sn;
				//System.out.println("path: " + thissn.arm);
				plotArmRobotPath(g, thissn.arm.config, solutionPath.size(), i++);
			}
		}


//		plotArmRobot(g, config2);
//		plotArmRobot(g, config1);

		scene.setRoot(g);
		primaryStage.show();

		try {
			ImageIO.write(
					SwingFXUtils.fromFXImage(g.snapshot(null, null), null),
					"png", new File("1-4.png"));
		} catch (Exception s) {

		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
