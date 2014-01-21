package assignment_mazeworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;

import assignment_mazeworld.SimpleMazeProblem.SimpleMazeNode;

// Find a path for a single agent to get from a start location (xStart, yStart)
//  to a goal location (xGoal, yGoal)

public class BlindRobotProblem extends InformedSearchProblem {

	private static int actions[][] = { Maze.NORTH, Maze.EAST, Maze.SOUTH,
			Maze.WEST };

	private Coordinate coordStart;

	private Maze maze;

	public class Coordinate {
		public int x;
		public int y;

		Coordinate(int a, int b) {
			x = a;
			y = b;
		}

		Coordinate(Coordinate other) {
			x = other.x;
			y = other.y;
		}

		@Override
		public boolean equals(Object other) {
			return hashCode() == ((Coordinate) other).hashCode();
		}

		@Override
		public int hashCode() {
			return x + 10000 * y;
		}
	}

	public BlindRobotProblem(Maze m, int sx, int sy) {
		maze = m;
		coordStart = new Coordinate(sx, sy);
		startNode = new BlindRobotNode(coordStart, 0);
	}

	// node class used by searches. Searches themselves are implemented
	// in SearchProblem.
	public class BlindRobotNode implements SearchNode {

		// location of the agent in the maze
		protected Coordinate current, previous;
		protected HashSet<Coordinate> candidates;

		// how far the current node is from the start. Not strictly required
		// for uninformed search, but useful information for debugging,
		// and for comparing paths
		private double cost;

		public BlindRobotNode(Coordinate curr, double c) {
			candidates = new HashSet<>();
			current = new Coordinate(curr);
			cost = c;

			// initiate the candidates
			// if it is at startnode, where previous = null
			for (int x = 0; x < maze.width; x++) {
				for (int y = 0; y < maze.height; y++) {
					if (maze.getInt(x, y) == 0) {
						// System.out.println(x + "," + y);
						candidates.add(new Coordinate(x, y));
					}
				}
			}
		}

		public BlindRobotNode(Coordinate curr, BlindRobotNode prevNode,
				boolean noWall, double c) {
			// initiate the positions
			candidates = new HashSet<>();
			current = new Coordinate(curr);
			previous = new Coordinate(prevNode.current);
			cost = c;

			// if now is one of the searching node, compute derection first
			int dx = current.x - previous.x, dy = current.y - previous.y;
			for (Coordinate cd : prevNode.candidates) {
				// if it comes from in wall node, or if the action matches
				// we add this candidate
				if (maze.isLegal(cd.x + dx, cd.y + dy) == noWall) {
					Coordinate tmp = new Coordinate(cd.x + dx, cd.y + dy);
					candidates.add(tmp);
				}
			}

		}

		public boolean inCandidates(Coordinate cd) {
			return candidates.contains(cd);
		}

		public int getX() {
			return current.x;
		}

		public int getY() {
			return current.y;
		}

		public ArrayList<SearchNode> getSuccessors() {

			ArrayList<SearchNode> successors = new ArrayList<SearchNode>();

			for (int[] action : actions) {
				Coordinate next = new Coordinate(current.x + action[0],
						current.y + action[1]);
				SearchNode succ;
				if (maze.isLegal(next.x, next.y)) {
					succ = new BlindRobotNode(next, this, true, getCost() + 1.0);
				} else {
					succ = new BlindRobotNode(current, this, true,
							getCost() + 1.0);
				}
				successors.add(succ);
			}

			return successors;

		}

		@Override
		public boolean goalTest() {
//			System.out.println("candidates.size(): " + candidates.size()
//					+ ". At " + getX() + "," + getY());
			return candidates.size() == 1;
		}

		// an equality test is required so that visited sets in searches
		// can check for containment of states
		@Override
		public boolean equals(Object other) {
			return candidates == ((BlindRobotNode) other).candidates;
		}

		@Override
		public int hashCode() {
			return 10000 * candidates.size() + current.x * 1000 + current.y;
		}

		@Override
		public String toString() {
			return new String("Maze state " + current.x + ", " + current.y
					+ " " + " depth " + getCost());
		}

		@Override
		public double getCost() {
			return cost;
		}

		@Override
		public double heuristic() {
			// manhattan distance metric for simple maze with one agent:
			return candidates.size() - 1;
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
