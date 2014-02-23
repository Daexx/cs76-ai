// CLEARLY INDICATE THE AUTHOR OF THE FILE HERE (YOU),
//  AND ATTRIBUTE ANY SOURCES USED (INCLUDING THIS STUB, BY
//  DEVIN BALKCOM).

package cannibals;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.swing.event.ListSelectionEvent;

import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

public abstract class UUSearchProblem {

	// used to store performance information about search runs.
	// these should be updated during the process of searches

	// see methods later in this class to update these values
	protected int nodesExplored;
	protected int maxMemory;

	protected UUSearchNode startNode;

	protected interface UUSearchNode {

		public ArrayList<UUSearchNode> getSuccessors();

		public boolean goalTest();

		public int getDepth();
	}

	// breadthFirstSearch: return a list of connecting Nodes, or null
	// no parameters, since start and goal descriptions are problem-dependent.
	// therefore, constructor of specific problems should set up start
	// and goal conditions, etc.

	public List<UUSearchNode> breadthFirstSearch() {
		resetStats();

		UUSearchNode node = startNode;
		HashMap<UUSearchNode, UUSearchNode> visited = new HashMap<>();
		Queue<UUSearchNode> nqueue = new LinkedList<UUSearchNode>();
		List<UUSearchNode> successors;

		nqueue.add(startNode);
		while (!nqueue.isEmpty()) {
			// get node from the queue
			
			node = nqueue.poll();
			//System.out.println(node);
			
			// check if arrives destination
			if (node.goalTest()) {
				updateMemory(visited.size() + 1); // add the start node
				nodesExplored = visited.size() + 1;
				return backchain(node, visited);
			}

			// if not destination, keep searching and tracking
			successors = node.getSuccessors();
			for (UUSearchNode n : successors) {
				if (!visited.containsValue(n)) {
					visited.put(n, node);
					nqueue.add(n);
				}
			}
		}
		// if destination not found, return null
		return null;
	}

	// backchain should only be used by bfs, not the recursive dfs
	private List<UUSearchNode> backchain(UUSearchNode node,
			HashMap<UUSearchNode, UUSearchNode> visited) {
		List<UUSearchNode> path = new ArrayList<UUSearchNode>();
		path.add(node);

		while (node != startNode) {
			node = visited.get(node);
			path.add(node);
		}
		return path;
	}

	public List<UUSearchNode> depthFirstMemoizingSearch(int maxDepth) {
		resetStats();
		HashMap<UUSearchNode, Integer> visited = new HashMap<>();
		return dfsrm(startNode, visited, 0, maxDepth);
	}

	// recursive memoizing dfs. Private, because it has the extra
	// parameters needed for recursion.
	private List<UUSearchNode> dfsrm(UUSearchNode currentNode,
			HashMap<UUSearchNode, Integer> visited, int depth, int maxDepth) {

		// keep track of stats; these calls charge for the current node
		updateMemory(visited.size());
		incrementNodeCount();

		// you write this method. Comments *must* clearly show the
		// "base case" and "recursive case" that any recursive function has.

		//System.out.println(currentNode);
		List<UUSearchNode> tryPath, path = new ArrayList<UUSearchNode>(
				Arrays.asList(currentNode));
		List<UUSearchNode> successors;

		visited.put(currentNode, depth);
		if (depth > maxDepth)
			return null;			
		
		if (currentNode.goalTest()) {
			return path;
		} else {
			successors = currentNode.getSuccessors();
			for (UUSearchNode n : successors) {
				if(!visited.containsKey(n) || visited.get(n) > depth + 1) {
					tryPath = dfsrm(n, visited, depth + 1, maxDepth);
					if (tryPath != null) {
						path.addAll(tryPath);
						return path;
					}
				}
			}
		}
		return null;
	}

	// set up the iterative deepening search, and make use of dfspc
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

	// set up the depth-first-search (path-checking version),
	// but call dfspc to do the real work
	public List<UUSearchNode> depthFirstPathCheckingSearch(int maxDepth) {
		resetStats();
		HashSet<UUSearchNode> currentPath = new HashSet<UUSearchNode>();
		return dfsrpc(startNode, currentPath, 0, maxDepth);
	}

	// recursive path-checking dfs. Private, because it has the extra
	// parameters needed for recursion.
	private List<UUSearchNode> dfsrpc(UUSearchNode currentNode,
			HashSet<UUSearchNode> currentPath, int depth, int maxDepth) {

		// keep track of stats; these calls charge for the current node
		updateMemory(currentPath.size());
		incrementNodeCount();

		// you write this method. Comments *must* clearly show the
		// "base case" and "recursive case" that any recursive function has.

		List<UUSearchNode> successors, tryPath, path = new ArrayList<UUSearchNode>(
				Arrays.asList(currentNode));

		//System.out.println(currentNode);
		if (depth > maxDepth)
			return null;
		else 
			currentPath.add(currentNode);

		if (currentNode.goalTest()) {
			return path;
		} else {
			successors = currentNode.getSuccessors();
			for (UUSearchNode n : successors) {
				if (!currentPath.contains(n)) {
					//System.out.println(currentPath.size());
					tryPath = dfsrpc(n, currentPath, depth + 1, maxDepth);
					if (tryPath != null) {
						path.addAll(tryPath);
						return path;
					}
				}
			}
		}
		currentPath.remove(currentNode);
		return null;
	}

	protected void resetStats() {
		nodesExplored = 0;
		maxMemory = 0;
	}

	protected void printStats() {
		System.out.println("Nodes explored during last search:  "
				+ nodesExplored);
		System.out.println("Maximum memory usage during last search "
				+ maxMemory);
	}

	protected void updateMemory(int currentMemory) {
		//System.out.println(currentMemory);
		maxMemory = Math.max(currentMemory, maxMemory);
	}

	protected void incrementNodeCount() {
		nodesExplored++;
	}

}
