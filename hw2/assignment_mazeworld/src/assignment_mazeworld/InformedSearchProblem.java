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
			SearchNode current = frontiers.poll();
			// discard the node if a shorter one is visitedxx
			if (visited.containsKey(current)
					&& visited.get(current) <= current.priority())
				continue;
			// mark the goal
			if (current.goalTest())
				return backchain(current, reachedFrom);
			// keep adding the frontiers and update visited
			ArrayList<SearchNode> successors = current.getSuccessors();
			for (SearchNode n : successors) {
				if (!visited.containsKey(n) || visited.get(n) > n.priority()) {
					reachedFrom.put(n, current);
					visited.put(n, n.priority());
					frontiers.add(n);
				}
			}
		}
		return null;
	}
}
