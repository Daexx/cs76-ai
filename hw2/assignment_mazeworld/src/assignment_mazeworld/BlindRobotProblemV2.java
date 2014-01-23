package assignment_mazeworld;

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

	private Coordinate coordStart, coordGoal;

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

		private boolean equalWithCast(Object other) {
			return (x - ((Coordinate) other).x < 0.01)
					&& (y - ((Coordinate) other).y < 0.01);
		}

		@Override
		public boolean equals(Object other) {
			return hashCode() == ((Coordinate) other).hashCode();
		}

		@Override
		public int hashCode() {
			return (int) (x + 1000 * y);
		}
	}

	public BlindRobotProblemV2(Maze m, int sx, int sy, int gx, int gy) {
		maze = m;
		coordStart = new Coordinate(sx, sy);
		coordGoal = new Coordinate(gx, gy);
		startNode = new BlindRobotNode(coordStart, 0);
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

		public BlindRobotNode(double c) {
			allCoord = new HashSet<>();
			cost = c;

			// initiate the allCoord
			// if it is at start node, where previous = null
			for (int x = 0; x < maze.width; x++) {
				for (int y = 0; y < maze.height; y++) {
					if (maze.getInt(x, y) == 0) {
						Coordinate newNode = new Coordinate(x, y);
						allCoord.add(newNode);
					}
				}
			}
			newCenDev();
		}

		public BlindRobotNode(Coordinate dxdy, double c) {
			// initiate the positions
			allCoord = new HashSet<>();
			cost = c;
			// move all the node in the belief set to direction dxdy
			for (Coordinate bs : allCoord) {
				if (maze.isLegal((int)(bs.x + dxdy.x), (int)(bs.y + dxdy.y))) {
					allCoord.add(new Coordinate(bs.x + dxdy.x, bs.y + dxdy.y));
				} else {
					// if not legal, simply not moving
					allCoord.add(new Coordinate(bs.x, bs.y));
				}
			}
			newCenDev();
		}

		// dev = E(x^2) - (Ex)^2
		private void newCenDev() {
			center = new Coordinate(0, 0);
			sDeviation = new Coordinate(0, 0);
			for (Coordinate cd : allCoord) {
				center.x += cd.x;
				center.y += cd.y;
				sDeviation.x += Math.pow(cd.x, 2);
				sDeviation.y += Math.pow(cd.y, 2);
			}
			center.x /= allCoord.size();
			center.y /= allCoord.size();
			sDeviation.x = Math.pow(sDeviation.x - center.x * center.x, 0.5);
			sDeviation.y = Math.pow(sDeviation.y - center.y * center.y, 0.5);
		}

		public boolean inallCoord(Coordinate cd) {
			return allCoord.contains(cd);
		}

		/*
		 * public int getX() { return current.x; }
		 * 
		 * public int getY() { return current.y; }
		 */

		public ArrayList<SearchNode> getSuccessors() {
			ArrayList<SearchNode> successors = new ArrayList<SearchNode>();
			for (int[] action : actions) {
				Coordinate dxdy = new Coordinate(action[0], action[1]);
				successors.add(new BlindRobotNode(dxdy, getCost() + 1.0));
			}
			return successors;
		}

		public int hashCoord(int x, int y) {
			return x + 1000 * y;
		}

		@Override
		public boolean goalTest() {
			// System.out.println("allCoord.size(): " + allCoord.size()
			// + ". At " + getX() + "," + getY());
			return allCoord.size() == 1 && center == coordGoal;
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
			return new String("Maze state " + center.x + ", " + center.y
					+ " " + " depth " + getCost());
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
			return Math.abs(dx) + Math.abs(dy) + sDeviation.x + sDeviation.y;
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
