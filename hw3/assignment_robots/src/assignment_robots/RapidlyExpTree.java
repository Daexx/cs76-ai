package assignment_robots;

import java.util.*;

import assignment_robots.RoadMapProblem.AdjacentCfg;
import assignment_robots.SearchProblem.SearchNode;
import assignment_robots.InformedSearchProblem;

public class RapidlyExpTree extends InformedSearchProblem {
	
	CarRobot startCar, goalCar;
	HashSet<CarRobot> connected = new HashSet<>(); // world sampling
	HashMap<CarRobot, AdjacentCfg> roadmap = new HashMap<>(); // roadmap, a graph
	World map;
	
	public RapidlyExpTree(World m, CarState config1, CarState config2, int density) {
		map = m;
		startCar = new CarRobot(config1);
		goalCar = new CarRobot(config2);
		startNode = new RRTnode(startCar, 0);
		growTree2Goal(density);

		// TODO Auto-generated constructor stub
	}
	
	public class AdjacentCfg implements Comparable<AdjacentCfg> {
		public CarRobot ar;
		public double dis;
		
		AdjacentCfg(CarRobot a, double d){
			ar = a;
			dis = d;
		}
		
		@Override
		public int compareTo(AdjacentCfg other)  {
			// sorted from large to small
			return (int) Math.signum(other.dis - dis);
		}
	}
	
/*	public void getSampling(int density) {
		while(density > 0) {
			double [] rConfig = getRandCfg(startCar.links, map);
			CarRobot toBeAdded = new CarRobot(rConfig);
			if(!map.armCollision(toBeAdded)) {
				samplings.add(toBeAdded);
				density--;
			}
		}
		samplings.add(startCar);
		samplings.add(goalCar);
	}*/
	
	private CarState getRandCfg(World map) {
		Random rd = new Random();
		double[] cfg = new double[3];
		// randomize the start position
		cfg[0] = rd.nextDouble() * map.getW();
		cfg[1] = rd.nextDouble() * map.getH();
		cfg[2] = (rd.nextDouble() - 0.5) * 1000 % 6 * Math.PI / 3;
		return new CarState(cfg);
	}
	
	public void growTree2Goal(int density) {
		while(density > 0) {
			Double minDis = Double.MAX_VALUE;
			CarRobot toBeAdded = new CarRobot(getRandCfg(map)), nearest;
			if(!map.carCollision(toBeAdded)) {
				for(CarRobot cr : connected) {
					double dis = toBeAdded.getDistance(cr);
					if(minDis > dis) {
						minDis = dis;
						nearest = cr;
					}
				}
				for(CarRobot cr : connected) {
					double dis = toBeAdded.getDistance(cr);
					if(minDis > dis) {
						minDis = dis;
						nearest = cr;
					}
				}
			}
		}
		
		// initiate the connecting with start arm
		ArmLocalPlanner ap = new ArmLocalPlanner();
		PriorityQueue<AdjacentCfg> tmpq;
		for(CarRobot ar : samplings) 
			roadmap.put(ar, null);
		
		for(CarRobot ar : samplings) {
			for(CarRobot arOther : samplings) {
				int base_conn = roadmap.get(ar).size();
				if(ar != arOther) {
					double dis = ap.moveInParallel(ar.config, arOther.config);
					if(!map.armCollisionPath(ar, ar.config, arOther.config)) {
						tmpq = roadmap.get(ar);
						tmpq.add(new AdjacentCfg(arOther, dis));
						if(tmpq.size() > k_neighbour + base_conn)
							tmpq.poll();
						roadmap.get(arOther).add(new AdjacentCfg(ar, dis));
					}
				}
			}
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////
	
	
	public class RRTnode implements SearchNode {
		private CarRobot arm;
		private double cost;
		
		// construct the connected graph
		public RRTnode(CarRobot inAr, double c) {
			arm = inAr;
			cost = c;
		}
		
		public ArrayList<SearchNode> getSuccessors() {
			ArrayList<SearchNode> successors = new ArrayList<SearchNode>();
			for(AdjacentCfg adj : roadmap.get(arm)) {
				successors.add(new RRTnode(adj.ar, adj.dis + cost));
			}
			return successors;
		}
		
		// override functions
		@Override
		public boolean equals(Object other) {
			return arm == ((RRTnode) other).arm;
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
			return goalCar == arm;
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
			return ap.moveInParallel(arm.config, goalCar.config);
		}

		@Override
		public double priority() {
			// TODO Auto-generated method stub
			return heuristic() + getCost();
		}
	}

}
