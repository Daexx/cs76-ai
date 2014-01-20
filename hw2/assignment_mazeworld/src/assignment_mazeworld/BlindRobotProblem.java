package assignment_mazeworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import assignment_mazeworld.SimpleMazeProblem.SimpleMazeNode;

// Find a path for a single agent to get from a start location (xStart, yStart)
//  to a goal location (xGoal, yGoal)

public class BlindRobotProblem extends InformedSearchProblem {

	private static int actions[][] = { Maze.NORTH, Maze.EAST, Maze.SOUTH,
			Maze.WEST };

	private int xStart, yStart;

	private Maze maze;

	public class Coordinate {
		public int x;
		public int y;

		Coordinate(int a, int b) {
			x = a;
			y = b;
		}
	}

	public BlindRobotProblem(Maze m, int sx, int sy) {
		maze = m;
		xStart = sx;
		yStart = sy;
		BlindRobotNode tmp = null;
		startNode = new BlindRobotNode(sx, sy, false, tmp, 0);
	}

	// node class used by searches. Searches themselves are implemented
	// in SearchProblem.
	public class BlindRobotNode implements SearchNode {

		// location of the agent in the maze
		protected int[] previous;
		protected int[] current;
		protected boolean wallTouched;
		protected ArrayList<Coordinate> candidates;

		// how far the current node is from the start. Not strictly required
		// for uninformed search, but useful information for debugging,
		// and for comparing paths
		private double cost;

		public BlindRobotNode(int cx, int cy, boolean wt, BlindRobotNode prev,
				double c) {
			// initiate the positions
			wallTouched = wt;
			cost = c;
			current = new int[2];
			previous = new int[2];
			candidates = new ArrayList<>();
			current[0] = cx;
			current[1] = cy;
			if (prev != null) {
				previous[0] = prev.current[0];
				previous[1] = prev.current[1];
			}else {
				previous[0] = current[0];
				previous[1] = current[1];
			}

			// initiate the candidates
			// if it is at startnode, where previous = null
			if (prev == null) {
				for (int x = 0; x < maze.width; x++) {
					for (int y = 0; y < maze.height; y++) {
						if (maze.getInt(x, y) == 0) {
							//System.out.println(x + "," + y);
							Coordinate tmp = new Coordinate(x, y);
							candidates.add(tmp);
						}
					}
				}
			} else {
				// if now is one of the searching node, compute derection first
				int dx = current[0] - previous[0], 
						dy = current[1]	- previous[1];
				for (Coordinate cd : prev.candidates) {
					// if it comes from in wall node, or if the action matches
					// we add this candidate
					if (prev.wallTouched 
							|| maze.isLegal(cd.x + dx, cd.y + dy) == !wt) {
						Coordinate tmp = new Coordinate(cd.x + dx, cd.y + dy);
						candidates.add(tmp);
					}
				}
			}
		}

		public int getX() {
			return current[0];
		}

		public int getY() {
			return current[1];
		}

		public ArrayList<SearchNode> getSuccessors() {

			ArrayList<SearchNode> successors = new ArrayList<SearchNode>();

			// if you not in wall, you may try any direction you want
			if (!wallTouched) {
				for (int[] action : actions) {
					int xNew = current[0] + action[0];
					int yNew = current[1] + action[1];
					SearchNode succ = new BlindRobotNode(xNew, yNew,
							!maze.isLegal(xNew, yNew), this, getCost() + 1.0);
					successors.add(succ);
				}
			}
			// if you in the wall, the only successor is the backward node
			// cost - 1, because it is backward
			else {
				SearchNode succ = new BlindRobotNode(previous[0], previous[1],
						false, this, getCost() - 1.0);
				successors.add(succ);
			}

			return successors;

		}

		@Override
		public boolean goalTest() {
			System.out.println("candidates.size(): " + candidates.size() + ". At " + getX() + "," + getY());
			return candidates.size() == 1 && !wallTouched;
		}

		// an equality test is required so that visited sets in searches
		// can check for containment of states
		@Override
		public boolean equals(Object other) {
			return candidates == ((BlindRobotNode) other).candidates;
		}

		@Override
		public int hashCode() {
			return 10000 * candidates.size() + current[0] * 100 + current[1];
		}

		@Override
		public String toString() {
			return new String("Maze state " + current[0] + ", " + current[1] + " "
					+ " depth " + getCost());
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
