package assignment_robots;


import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import assignment_robots.RapidlyExpTree.RRTnode;
import assignment_robots.RoadMapProblem.RoadMapNode;
import assignment_robots.SearchProblem.SearchNode;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.shape.Polygon;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class CarDriver extends Application {
	// default window size
	protected int winw = 600;
	protected int winh = 400;
	
	// Draw a polygon;
	public void addPolygon(Group g, Double[] points) {
		Polygon p = new Polygon();
	    p.getPoints().addAll(points);
	    
	    g.getChildren().add(p);
	}
	
	public void plotCarRobotPath(Group g, CarState s, int length, int idx) {
		//System.out.println(car);
		//System.out.println(s);
		CarRobot car = new CarRobot(s);
		double[][] current = car.get();
		Double[] to_add = new Double[2*current.length];
		for (int j = 0; j < current.length; j++) {
			//System.out.println(current[j][0] + ", " + current[j][1]);
			to_add[2*j] = current[j][0];
			//to_add[2*j+1] = current[j][1];
			to_add[2*j+1] = winh - current[j][1];
		}
		Polygon p = new Polygon();
		p.getPoints().addAll(to_add);
		p.setStroke(Color.WHITE);
		p.setFill(Color.rgb(255 - 255 * idx / length, 255 - 255 * idx / length, 125));
		g.getChildren().add(p);
	}
	
	// plot a car robot
	public void plotCarRobotTree(Group g, CarState s) {
		//System.out.println(car);
		//System.out.println(s);
		CarRobot car = new CarRobot(s);
		double[][] current = car.get();
		Double[] to_add = new Double[2*current.length];
		for (int j = 0; j < current.length; j++) {
			//System.out.println(current[j][0] + ", " + current[j][1]);
			to_add[2*j] = current[j][0];
			//to_add[2*j+1] = current[j][1];
			to_add[2*j+1] = winh - current[j][1];
		}
		Polygon p = new Polygon();
		p.getPoints().addAll(to_add);
		
		p.setStroke(Color.rgb(200, 200, 200));
		p.setFill(Color.WHITE);
		g.getChildren().add(p);
	}
	
	// plot a car robot
	public void plotCarRobot(Group g, CarState s) {
		//System.out.println(car);
		//System.out.println(s);
		CarRobot car = new CarRobot(s);
		double[][] current = car.get();
		Double[] to_add = new Double[2*current.length];
		for (int j = 0; j < current.length; j++) {
			//System.out.println(current[j][0] + ", " + current[j][1]);
			to_add[2*j] = current[j][0];
			//to_add[2*j+1] = current[j][1];
			to_add[2*j+1] = winh - current[j][1];
		}
		Polygon p = new Polygon();
		p.getPoints().addAll(to_add);
		
		p.setStroke(Color.RED);
		p.setFill(Color.PINK);
		g.getChildren().add(p);
	}
		
	// plot the World with all the obstacles;
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
			to_add = new Double[2*current.length];
			for (int j = 0; j < current.length; j++) {
				to_add[2*j] = current[j][0];
				//to_add[2*j+1] = current[j][1];
				to_add[2*j+1] = winh - current[j][1];
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
	
		primaryStage.setTitle("CS 76 2D world");
		Group root = new Group();
		Scene scene = new Scene(root, winw, winh);

		primaryStage.setScene(scene);
		
		Group g = new Group();
		
		double bg[][] = { { 0, 0 }, { 0, winh },
				{ winw, winh }, { winw, 0 }, { 0, 0 } };
		Poly bgc = new Poly(bg);

		double al = .04, bet = 0.9, basey = 0.;
		double a[][] = { { winw/2 - al * winw, basey },
				{ winw/2 - al * winw, bet * winh + basey},
				{ winw/2 + al * winw, bet * winh + basey },
				{ winw/2 + al * winw, basey} };
		Poly obstacle1 = new Poly(a);

		al = .35; bet = 0.1; basey = 0.8 * winh;
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
//		w.addObstacle(obstacle2);
//		w.addObstacle(obstacle3);
		w.addWall(bgc);
//		w.addObstacle(obstacle4);
//		w.addObstacle(obstacle5);
			
		plotWorld(g, w);
		
		CarRobot car = new CarRobot(new CarState(20, 40, 0));
		CarRobot car2 = new CarRobot(new CarState(475, 50, Math.PI));
		
//		boolean collided = w.carCollisionPath(car, state1, 0, 1.2);
//	    System.out.println(collided);
//		plotCarRobot(g, car, state1);
		
		RapidlyExpTree rrt = new RapidlyExpTree(w, car.getCarState(), car2.getCarState(), 15000);
		List<SearchNode> solutionPath = null;
		solutionPath = rrt.astarSearch();
		
		for (Iterator<CarRobot> carit = rrt.RRTree.keySet().iterator(); carit.hasNext();) {
			// System.out.println(ar);
			CarRobot cr = carit.next();
			plotCarRobotTree(g, cr.getCarState());
		}
		
		if(solutionPath == null)
			System.out.println("try to debug!!");
		else {
			System.out.println("path length: " + solutionPath.size());
			int i = 0;
			for (SearchNode sn : solutionPath) {
				RRTnode thissn = (RRTnode) sn;
				//System.out.println("path: " + thissn.arm);
				plotCarRobotPath(g, thissn.car.getCarState(), solutionPath.size(), i++);
			}
		}
		plotCarRobot(g, car.getCarState());
		plotCarRobot(g, car2.getCarState());
		
	    scene.setRoot(g);
	    primaryStage.show();
	    
		try {
			ImageIO.write(
					SwingFXUtils.fromFXImage(g.snapshot(null, null), null),
					"png", new File("2-3.png"));
		} catch (Exception s) {

		}
		
	}
	public static void main(String[] args) {
		launch(args);
	}
}
