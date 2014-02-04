package assignment_robots;

import java.util.*;

import assignment_robots.RoadMapProblem.AdjacentCfg;
import assignment_robots.SearchProblem.SearchNode;
import assignment_robots.InformedSearchProblem;

public class RapidlyExpTree extends InformedSearchProblem {

	CarRobot startCar, goalCar;
	HashSet<CarRobot> connected = new HashSet<>(); // world sampling
	HashMap<CarRobot, HashSet<AdjacentCfg>> roadmap = new HashMap<>(); // roadmap,
																	// a
	// graph
	World map;

	public RapidlyExpTree(World m, CarState config1, CarState config2,
			int density) {
		map = m;
		startCar = new CarRobot(config1);
		goalCar = new CarRobot(config2);
		startNode = new RRTnode(startCar, 0);
		roadmap.put(startCar, new HashSet<AdjacentCfg>());
		connected.add(startCar);
		growTree2Goal(density);

		// TODO Auto-generated constructor stub
	}

	public class AdjacentCfg implements Comparable<AdjacentCfg> {
		public CarRobot ar;
		public double dis;

		AdjacentCfg(CarRobot a, double d) {
			ar = a;
			dis = d;
		}

		@Override
		public int compareTo(AdjacentCfg other) {
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
		while (density > 0) {
			Double minDis = Double.MAX_VALUE;
			SteeredCar sc = new SteeredCar();
			CarRobot newRandCar = new CarRobot(getRandCfg(map)), nearest = null, newCarRobot = new CarRobot(), newNearest = null;
			if (!map.carCollision(newRandCar)) {
				for (CarRobot cr : connected) {
					double dis = newRandCar.getDistance(cr);
					if (minDis > dis) {
						minDis = dis;
						nearest = cr;
					}
				}

				minDis = Double.MAX_VALUE;
				for (int i = 0; i < 5; i++) {
					newCarRobot.set(sc.move(nearest.getCarState(), 0, 1));
					if (!map.carCollision(newCarRobot)) {
						double dis = newCarRobot.getDistance(newRandCar);
						if (minDis > dis) {
							minDis = dis;
							newNearest = newCarRobot;
						}
					}
				}
			}

			if (newNearest != null) {
				connected.add(newNearest);
				roadmap.get(nearest).add(new AdjacentCfg(newNearest, 1));
				roadmap.put(newNearest, new HashSet<AdjacentCfg>());
				if(newNearest.getDistance(goalCar) < 1){
					connected.add(goalCar);
					roadmap.get(newNearest).add(new AdjacentCfg(goalCar, 1));
					roadmap.put(goalCar, new HashSet<AdjacentCfg>());
				}
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////

	public class RRTnode implements SearchNode {
		private CarRobot car;
		private double cost;

		// construct the connected graph
		public RRTnode(CarRobot inAr, double c) {
			car = inAr;
			cost = c;
		}

		public ArrayList<SearchNode> getSuccessors() {
			ArrayList<SearchNode> successors = new ArrayList<SearchNode>();
			for (AdjacentCfg adj : roadmap.get(car)) {
				successors.add(new RRTnode(adj.ar, adj.dis + cost));
			}
			return successors;
		}

		// override functions
		@Override
		public boolean equals(Object other) {
			return car == ((RRTnode) other).car;
		}

		@Override
		public String toString() {
			return car.toString();
		}

		@Override
		public int compareTo(SearchNode o) {
			return (int) Math.signum(priority() - o.priority());
		}

		@Override
		public boolean goalTest() {
			// TODO Auto-generated method stub
			return goalCar == car;
		}

		@Override
		public double getCost() {
			// TODO Auto-generated method stub
			return cost;
		}

		@Override
		public double heuristic() {
			// TODO Auto-generated method stub
			return car.getDistance(goalCar);
		}

		@Override
		public double priority() {
			// TODO Auto-generated method stub
			return heuristic() + getCost();
		}
	}

}
