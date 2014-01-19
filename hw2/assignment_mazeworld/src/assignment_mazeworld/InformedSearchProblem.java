package assignment_mazeworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class InformedSearchProblem extends SearchProblem {

	public List<SearchNode> astarSearch() {
		resetStats();
		// implementing priority queue for the frontiers
		PriorityQueue<SearchNode> frontiers = new PriorityQueue<>();
		// implementing hashmap for the chain and the visited nodes
		HashMap<SearchNode, SearchNode> reachedFrom = new HashMap<>();
		HashMap<SearchNode, Double> visited = new HashMap<>();

		// initiate the visited with startnode
		reachedFrom.put(startNode, null);
		visited.put(startNode, startNode.priority());
		// initiate the frontier
		frontiers.add(startNode);
		while (!frontiers.isEmpty()) {
			// keep track of resource
			updateMemory(frontiers.size() + reachedFrom.size());
			incrementNodeCount();
			
			// retrieve from queue
			SearchNode currentNode = frontiers.poll();

			// mark the goal
			if (currentNode.goalTest())
				return backchain(currentNode, reachedFrom);

			// keep adding the frontiers and update visited
			ArrayList<SearchNode> successors = currentNode.getSuccessors();
			for (SearchNode n : successors) {
				if (!visited.containsKey(n)
						|| visited.get(n) > n.priority()) {
					reachedFrom.put(n, currentNode);
					//System.out.println(n + " vs " + currentNode);
					visited.put(n, n.priority());
					frontiers.add(n);
				}
			}
		}

		return null;
	}
}
