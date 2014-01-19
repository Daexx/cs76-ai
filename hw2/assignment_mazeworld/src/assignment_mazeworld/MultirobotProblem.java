package assignment_mazeworld;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;
import java.io.*;

// Find a path for a single agent to get from a start location (xStart, yStart)
//  to a goal location (xGoal, yGoal)

public class MultirobotProblem extends InformedSearchProblem {

	private static int actions[][] = { Maze.NORTH, Maze.EAST, Maze.SOUTH,
			Maze.WEST, Maze.STAY };

	private int R; // number of robots

	Integer[] xStart, yStart, xGoal, yGoal;

	private Maze maze;

	public MultirobotProblem(Maze m, int sr, Integer[] sx, Integer[] sy,
			Integer[] gx, Integer[] gy) {

		R = sr;
		startNode = new MultirobotNode(sx, sy, 0);
		xStart = sx;
		yStart = sy;
		xGoal = gx;
		yGoal = gy;
		maze = m;
	}

	// node class used by searches. Searches themselves are implemented
	// in SearchProblem.
	public class MultirobotNode implements SearchNode {

		// location of the agent in the maze
		protected int[][] robots;

		// how far the current node is from the start. Not strictly required
		// for uninformed search, but useful information for debugging,
		// and for comparing paths
		private double cost;

		public MultirobotNode(Integer[] x, Integer[] y, double c) {
			robots = new int[R][2];
			for (int i = 0; i < R; i++) {
				this.robots[i][0] = x[i];
				this.robots[i][1] = y[i];
			}
			cost = c;
		}

		public int getX(int r) {
			return robots[r][0];
		}

		public int getY(int r) {
			return robots[r][1];
		}

		public ArrayList<SearchNode> getSuccessors() {
			ArrayList<SearchNode> successors = new ArrayList<SearchNode>();
			Integer[] xNew = new Integer[R];
			Integer[] yNew = new Integer[R];

			// initiate the potential new coordinate
			for (int r = 0; r < R; r++) {
				xNew[r] = robots[r][0];
				yNew[r] = robots[r][1];
			}

			getSuccessorsDFS(xNew, yNew, successors, 0, 0);

			// System.out.println(this.toString() + ":\n" + successors);
			return successors;
		}

		private void getSuccessorsDFS(Integer[] xNow, Integer[] yNow,
				ArrayList<SearchNode> successors, double theirCost, int r) {
			// this is the base case of dfs recursion
			if (r == R)
				return;

			Integer[] xNew = new Integer[3], yNew = new Integer[3];
			boolean actionAvailable = false;
			// this is the recursive function
			for (int[] action : actions) {
				if(action == Maze.STAY && actionAvailable) break;
				iniNew(xNew, yNew, xNow, yNow);
				xNew[r] = robots[r][0] + action[0];
				yNew[r] = robots[r][1] + action[1];
				double myCost = Math.abs(action[0]) + Math.abs(action[1]);
				if (maze.isLegal(xNew[r], yNew[r]) && noCollision(xNew, yNew)) {
					SearchNode succ = new MultirobotNode(xNew, yNew, getCost()
							+ myCost + theirCost);
					successors.add(succ);
					getSuccessorsDFS(xNew, yNew, successors,
							myCost + theirCost, r + 1);
					actionAvailable = true;
				}
			}
		}

		private void iniNew(Integer[] xNew, Integer[] yNew, Integer[] xNow,
				Integer[] yNow) {
			for (int r = 0; r < R; r++) {
				xNew[r] = xNow[r];
				yNew[r] = yNow[r];
			}
		}

		private boolean noCollision(Integer[] xNew, Integer[] yNew) {
			HashSet<Integer> existed = new HashSet<>();
			for (int r = 0; r < R; r++) {
				Integer tmpHash = oneHash(xNew[r], yNew[r]);
				if (!existed.contains(tmpHash)) {
					existed.add(tmpHash);
				} else {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean goalTest() {
			for (int i = 0; i < R; i++)
				if (robots[i][0] != xGoal[i] || robots[i][1] != yGoal[i])
					return false;
			return true;
		}

		// an equality test is required so that visited sets in searches
		// can check for containment of robotss
		@Override
		public boolean equals(Object other) {
			return Arrays.equals(robots, ((MultirobotNode) other).robots);
		}

		@Override
		public int hashCode() {
			int hashcode = 0;
			for (int i = 0; i < R; i++) {
				hashcode += Math.pow(100, i)
						* (oneHash(robots[i][0], robots[i][1]));
			}
			return hashcode;
		}

		public int oneHash(Integer x, Integer y) {

			return x * 100 + y;
		}

		@Override
		public String toString() {
			String s = new String();
			s = "{";

			for (int i = 0; i < R; i++) {
				s += "(" + robots[i][0] + "," + robots[i][1] + ")";
			}
			return s + "}\n";
		}

		@Override
		public double getCost() {
			return cost;
		}

		@Override
		public double heuristic() {
			// manhattan distance metric for simple maze with one agent:
			double hValue = 0;
			for (int i = 0; i < R; i++)
				hValue += Math.abs(xGoal[i] - robots[i][0])
						+ Math.abs(yGoal[i] - robots[i][1]);
			return hValue;
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
