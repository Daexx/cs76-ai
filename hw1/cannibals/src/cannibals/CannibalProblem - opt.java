package cannibals;

import java.util.ArrayList;
import java.util.Arrays;

// for the first part of the assignment, you might not extend UUSearchProblem,
//  since UUSearchProblem is incomplete until you finish it.

public class CannibalProblem extends UUSearchProblem {

  // the following are the only instance variables you should need.
  // (some others might be inherited from UUSearchProblem, but worry
  // about that later.)

  private int goalm, goalc, goalb;
  private int totalMissionaries, totalCannibals, totalBoats;

  public CannibalProblem(int sm, int sc, int sb, int gm, int gc, int gb) {
    // I (djb) wrote the constructor; nothing for you to do here.

    startNode = new CannibalNode(sm, sc, 1, 0);
    goalm = gm;
    goalc = gc;
    goalb = gb;
    totalMissionaries = sm;
    totalCannibals = sc;
    totalBoats = sb;
  }

  // node class used by searches. Searches themselves are implemented
  // in UUSearchProblem.
  private class CannibalNode implements UUSearchNode {

    // do not change BOAT_SIZE without considering how it affect
    // getSuccessors.

    private final static int BOAT_SIZE = 2;

    // how many missionaries, cannibals, and boats
    // are on the starting shore
    private int[] state;

    // how far the current node is from the start. Not strictly required
    // for search, but useful information for debugging, and for comparing
    // paths
    private int depth;

    public CannibalNode(int m, int c, int b, int d) {
      state = new int[3];
      this.state[0] = m;
      this.state[1] = c;
      this.state[2] = b;

      depth = d;

    }

    public ArrayList<UUSearchNode> getSuccessors() {
      ArrayList<UUSearchNode> successors = new ArrayList<>();

      // assuming that every time I use as much boat as possible
      if (state[2] != 0) {
        // search from the possible largest number
        for (int i = Math.min(state[2] * BOAT_SIZE, state[0]); i >= 0 ; i--) {
          for (int j = Math.min(state[2] * BOAT_SIZE - i,
              state[1]); j >= 0; j--) {
            if(i + j == 0) continue;
            CannibalNode tryNode = new CannibalNode(state[0] - i,
                state[1] - j, 0, 0);
            if (isSafeState(tryNode)) {
              successors.add(tryNode);
            }
          }
        }
      } else {
        CannibalNode tryNode = new CannibalNode(state[0], state[1],
            totalBoats, 0);
        successors.add(tryNode);
      }

      return successors;
    }

    private boolean isSafeState(CannibalNode tryNode) {
      if (tryNode.state[0] >= tryNode.state[1]
          && (totalMissionaries - tryNode.state[0]) >= (totalCannibals - tryNode.state[1])
          && tryNode.state[0] >= 0 && tryNode.state[1] >= 0)
        return true;
      else
        return false;
    }

    @Override
    public boolean goalTest() {
      return equals(new CannibalNode(goalm, goalc, goalb, 0));
    }

    // an equality test is required so that visited lists in searches
    // can check for containment of states
    @Override
    public boolean equals(Object other) {
      return Arrays.equals(state, ((CannibalNode) other).state);
    }

    @Override
    public int hashCode() {
      return state[0] * 100 + state[1] * 10 + state[2];
    }

    @Override
    public String toString() {
      return Integer.toString(hashCode());
    }

    /*
     * You might need this method when you start writing (and debugging)
     * UUSearchProblem.
     */
    @Override
    public int getDepth() {
      return depth;
    }

  }

}























public List<UUSearchNode> IDSearch(int maxDepth) {
  resetStats();

  HashSet<UUSearchNode> currentPath = new HashSet<UUSearchNode>();
  List<UUSearchNode> path;

  for (int i = 1; i <= maxDepth; i++) {
    currentPath.clear();
    path = dfsrpc(startNode, currentPath, 0, i);
    if (path != null)
      return path;
  }
  return null;
}






