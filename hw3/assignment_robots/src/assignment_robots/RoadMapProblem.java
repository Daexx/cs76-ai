package assignment_robots;

import java.util.*;

import assignment_robots.RoadMapProblem.AdjacentCfg;
import assignment_robots.SearchProblem.SearchNode;
import assignment_robots.InformedSearchProblem;

public class RoadMapProblem extends InformedSearchProblem {
	
	ArmRobot startArm, goalArm;
	HashSet<ArmRobot> samplings = new HashSet<>(); // world sampling
	HashMap<ArmRobot, PriorityQueue<AdjacentCfg>> roadmap = new HashMap<>(); // roadmap, a graph
	World map;
	int k_neighbour;
	
	public RoadMapProblem(World m, Double[] config1, Double[] config2, int density, int K) {
		k_neighbour = K;
		map = m;
		startArm = new ArmRobot(config1);
		goalArm = new ArmRobot(config2);
		startNode = new RoadMapNode(startArm, 0.);
		getSampling(density);
		getConnected();

		// TODO Auto-generated constructor stub
	}
	
	public class AdjacentCfg implements Comparable<AdjacentCfg> {
		public ArmRobot ar;
		public Double dis;
		
		AdjacentCfg(ArmRobot a, Double d){
			ar = new ArmRobot(a.config);
			dis = d;
		}
		
		@Override
		public int compareTo(AdjacentCfg other)  {
			// sorted from large to small
			return (int) Math.signum(other.dis - dis);
		}
	}
	
	public void getSampling(int density) {
		while(density > 0) {
			Double [] rConfig = getRandCfg(startArm.links, map);
			ArmRobot toBeAdded = new ArmRobot(rConfig);
			if(true || !map.armCollision(toBeAdded)) {
				samplings.add(toBeAdded);
				//System.out.println(toBeAdded);
				density--;
			}
		}
		samplings.add(startArm);
		samplings.add(goalArm);
		System.out.println("startArm" + startArm);
	}
	
	private Double[] getRandCfg(int num, World map) {
		Random rd = new Random();
		Double[] cfg = new Double[2 * num + 2];
		// randomize the start position
		cfg[0] = rd.nextDouble() * map.getW();
		cfg[1] = rd.nextDouble() * map.getH();
		//System.out.println(cfg[0]/map.getW() + "," + cfg[1]/map.getH());
		for (int i = 1; i <= num; i++) {
			// the length of each arm remains the same
			cfg[2 * i] = startArm.config[2 * i];
			// randomize the angle
			cfg[2 * i + 1] = rd.nextDouble() * Math.PI * 2;
		}
		return cfg;
	}
	
	public void getConnected() {
		// initiate the connecting with start arm
		ArmLocalPlanner ap = new ArmLocalPlanner();
		PriorityQueue<AdjacentCfg> tmpq;
		for(ArmRobot ar : samplings) 
			roadmap.put(ar, new PriorityQueue<AdjacentCfg>());
		
		for(ArmRobot ar : samplings) {
			//System.out.println("startArm3" + startArm);
			int base_conn = roadmap.get(ar).size();
			for(ArmRobot arOther : samplings) {
				if(ar != arOther) {
					Double dis = ap.moveInParallel(ar.config, arOther.config);
					if(!map.armCollisionPath(ar, ar.config, arOther.config)) {
						tmpq = roadmap.get(ar);
						tmpq.add(new AdjacentCfg(arOther, dis));
						if(tmpq.size() > k_neighbour + base_conn)
							tmpq.poll();
						//roadmap.get(arOther).add(new AdjacentCfg(ar, dis));
					}
				}
			}
		}

			for(AdjacentCfg adj : roadmap.get(startArm)) {
				System.out.println(adj.ar);
			}

	}
	
	
	///////////////////////////////////////////////////////////////////////
	
	
	public class RoadMapNode implements SearchNode {
		public ArmRobot arm;
		private Double cost;
		
		// construct the connected graph
		public RoadMapNode(ArmRobot inAr, Double c) {
			arm = new ArmRobot(inAr.config);
			cost = c;
		}
		
		public ArrayList<SearchNode> getSuccessors() {
			ArrayList<SearchNode> successors = new ArrayList<SearchNode>();
			for(AdjacentCfg adj : roadmap.get(arm)) {
				successors.add(new RoadMapNode(adj.ar, adj.dis + cost));
			}
			return successors;
		}
		
		// override functions
		@Override
		public boolean equals(Object other) {
			return arm == ((RoadMapNode) other).arm;
		}

		@Override
		public String toString() {
			return arm.toString();
		}

		@Override
		public int compareTo(SearchNode o) {
			return (int) Math.signum(priority() - o.priority());
		}

		@Override
		public boolean goalTest() {
			// TODO Auto-generated method stub
			//System.out.println("arm: " + arm);
			return goalArm == arm;
		}

		@Override
		public double getCost() {
			// TODO Auto-generated method stub
			return cost;
		}

		@Override
		public double heuristic() {
			// TODO Auto-generated method stub
			ArmLocalPlanner ap = new ArmLocalPlanner();
			return ap.moveInParallel(arm.config, goalArm.config);
		}

		@Override
		public double priority() {
			// TODO Auto-generated method stub
			return heuristic() + getCost();
		}
		
		@Override
		public int hashCode() {
			return arm.hashCode();
		}
		
	}

}
