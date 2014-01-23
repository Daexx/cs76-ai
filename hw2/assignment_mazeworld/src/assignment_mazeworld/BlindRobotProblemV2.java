package assignment_mazeworld;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;

import assignment_mazeworld.SimpleMazeProblem.SimpleMazeNode;

// Find a path for a single agent to get from a start location (xStart, yStart)
//  to a goal location (xGoal, yGoal)

public class BlindRobotProblemV2 extends InformedSearchProblem {

	private static int actions[][] = { Maze.NORTH, Maze.EAST, Maze.SOUTH,
			Maze.WEST };

	private Coordinate coordGoal;

	private Maze maze;

	public class Coordinate {
		public Double x;
		public Double y;

		void add2center(Coordinate other, int newN) {
			x = (x * (newN - 1) + other.x) / newN;
			y = (y * (newN - 1) + other.y) / newN;
		}

		Coordinate(int a, int b) {
			x = (double) a;
			y = (double) b;
		}

		Coordinate(double a, double b) {
			x = a;
			y = b;
		}

		Coordinate(Coordinate other) {
			x = other.x;
			y = other.y;
		}

		public int getX() {
			return x.intValue();
		}

		public int getY() {
			return y.intValue();
		}

		private boolean equalWithCast(Object other) {
			return Math.abs((x - ((Coordinate) other).x)) < 0.01
					&& (Math.abs(y - ((Coordinate) other).y) < 0.01);
		}

		private boolean isZero() {
			return (x < 0.01) && (y < 0.01);
		}

		@Override
		public boolean equals(Object other) {
			return hashCode() == ((Coordinate) other).hashCode();
		}

		@Override
		public int hashCode() {
			return (int) (x + 1000 * y);
		}

		@Override
		public String toString() {
			DecimalFormat df = new DecimalFormat("0.0");
			return "(" + df.format(x) + "," + df.format(y) + ")";
		}
	}

	public BlindRobotProblemV2(Maze m, int gx, int gy) {
		maze = m;
		coordGoal = new Coordinate(gx, gy);
		// initiate the allCoord
		HashSet<Coordinate> newCoords = new HashSet<>();
		for (int x = 0; x < maze.width; x++) {
			for (int y = 0; y < maze.height; y++) {
				if (maze.getInt(x, y) == 0) {
					Coordinate newNode = new Coordinate(x, y);
					newCoords.add(newNode);
				}
			}
		}
		startNode = new BlindRobotNode(newCoords, 0);
	}

	// node class used by searches. Searches themselves are implemented
	// in SearchProblem.
	public class BlindRobotNode implements SearchNode {

		// location of the agent in the maze
		protected Coordinate center, sDeviation; // center & standard deviation
		protected HashSet<Coordinate> allCoord;

		// how far the current node is from the start. Not strictly required
		// for uninformed search, but useful information for debugging,
		// and for comparing paths
		private double cost;

		public BlindRobotNode(HashSet<Coordinate> coords, double c) {
			// initiate the positions
			allCoord = coords;
			cost = c;
			newCenDev();
		}

		// dev = E(x^2) - (Ex)^2
		private void newCenDev() {
			center = new Coordinate(0, 0);
			sDeviation = new Coordinate(0, 0);
			for (Coordinate xy : allCoord) {
				center.x += xy.x;
				center.y += xy.y;
				sDeviation.x += Math.pow(xy.x, 2);
				sDeviation.y += Math.pow(xy.y, 2);
			}
			center.x /= allCoord.size();
			center.y /= allCoord.size();
			sDeviation.x = Math.pow(sDeviation.x - center.x * center.x, 0.5);
			sDeviation.y = Math.pow(sDeviation.y - center.y * center.y, 0.5);
		}

		public boolean inallCoord(Coordinate xy) {
			return allCoord.contains(xy);
		}

		public int getX() {
			return center.x.intValue();
		}

		public int getY() {
			return center.y.intValue();
		}

		public ArrayList<SearchNode> getSuccessors() {
			// PrintWriter writer;
			// try {
			// writer = new PrintWriter(new FileOutputStream(new File(
			// "debug.log"), true));
			// writer.println("---------------------------------------");
			// writer.close();
			// } catch (FileNotFoundException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			DecimalFormat df = new DecimalFormat("0.0");
			ArrayList<SearchNode> successors = new ArrayList<SearchNode>();
			for (int[] action : actions) {
				HashSet<Coordinate> newCoords = new HashSet<>();
				// move all the node in the belief set to direction dxdy
				Coordinate dxdy = new Coordinate(action[0], action[1]);
				for (Coordinate xy : this.allCoord) {
					if (maze.isLegal(xy.x + dxdy.x, xy.y + dxdy.y)) {
						newCoords.add(new Coordinate(xy.x + dxdy.x, xy.y
								+ dxdy.y));
					} else {
						// if not legal, simply not moving
						newCoords.add(new Coordinate(xy.x, xy.y));
					}
					// System.out.println(xy + "+" + dxdy + "=" + newCoords);
				}

				// new java.util.Scanner(System.in).nextLine();
				SearchNode succ = new BlindRobotNode(newCoords, getCost() + 1.0
						* newCoords.size());
				successors.add(succ);

				// try {
				// writer = new PrintWriter(new FileOutputStream(new File(
				// "debug.log"), true));
				// writer.println("avg: " + this.center + " dev: "
				// + this.sDeviation + " set: " + this.allCoord
				// + " pty: " + df.format(priority()) + " cost: "
				// + getCost() + " " + " dir: " + dxdy + " new set: "
				// + newCoords + " new pty:" + df.format(succ.priority()));
				// writer.close();
				// } catch (FileNotFoundException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}
			return successors;
		}

		public int hashCoord(int x, int y) {
			return x + 1000 * y;
		}

		@Override
		public boolean goalTest() {
			// System.out.println(center + " and " + sDeviation + " and "
			// + coordGoal);
			return center.equalWithCast(coordGoal) && sDeviation.isZero();
		}

		// an equality test is required so that visited sets in searches
		// can check for containment of states
		@Override
		public boolean equals(Object other) {
			return allCoord == ((BlindRobotNode) other).allCoord;
		}

		@Override
		public int hashCode() {
			return (int) (((center.x * 100 + center.y) + (sDeviation.x * 100 + sDeviation.y) * 100) * 10.);
		}

		@Override
		public String toString() {
			return new String("Maze state " + center.x + ", " + center.y + " "
					+ " depth " + getCost());
		}

		@Override
		public double getCost() {
			return cost;
		}

		@Override
		public double heuristic() {
			// manhattan distance metric for simple maze with one agent:
			double dx = coordGoal.x - center.x;
			double dy = coordGoal.y - center.y;
//			return Math.max((Math.abs(dx) + Math.abs(dy)) ,
//					allCoord.size());
			return Math.max((Math.abs(dx) + Math.abs(dy)) ,
					(sDeviation.x + sDeviation.y));
			// return allCoord.size() - 1;
		}

		@Override
		public int compareTo(SearchNode o) {
			return (int) Math.signum(priority() - o.priority());
		}

		@Override
		public double priority() {
			return heuristic() + getCost();
		}

	}

}
