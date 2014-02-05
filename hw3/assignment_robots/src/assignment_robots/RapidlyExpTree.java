package assignment_robots;

import java.util.*;

import assignment_robots.RoadMapProblem.AdjacentCfg;
import assignment_robots.SearchProblem.SearchNode;
import assignment_robots.InformedSearchProblem;

public class RapidlyExpTree extends InformedSearchProblem {

	CarRobot startCar, goalCar;
	HashSet<CarRobot> connected = new HashSet<>(); // world sampling
	HashMap<CarRobot, HashSet<AdjacentCfg>> RRTree = new HashMap<>(); // RRTree,
																		// a
	// graph
	World map;

	public RapidlyExpTree(World m, CarState config1, CarState config2,
			int density) {
		map = m;
		startCar = new CarRobot(config1);
		goalCar = new CarRobot(config2);
		startNode = new RRTnode(startCar, 0);
		RRTree.put(startCar, new HashSet<AdjacentCfg>());
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
		cfg[2] = (rd.nextInt(1000)) % 6 * Math.PI / 3;
		return new CarState(cfg);
	}

	private CarRobot findNearestInTree(CarRobot newRandCar) {
		Double minDis = Double.MAX_VALUE;
		CarRobot nearest = null;
		for (CarRobot cr : connected) {
			double dis = newRandCar.getDistance(cr);
			if (minDis > dis) {
				minDis = dis;
				nearest = cr;
			}
		}
		return nearest;
	}

	private CarRobot expandTree(CarRobot newRandCar, CarRobot nearest) {
		// initiate the auxiliary object
		Double minDis = Double.MAX_VALUE;
		SteeredCar sc = new SteeredCar();
		CarRobot newNearest = null, newCarRobot = new CarRobot();
		// try to expand to 6 different directions
		for (int i = 0; i <= 5; i++) {
			newCarRobot = new CarRobot(sc.move(nearest.getCarState(), i, 1.));
			// System.out.println("newCarRobot: " + newCarRobot);
			if (!map.carCollision(newCarRobot)) {
				double dis = newCarRobot.getDistance(newRandCar);
				if (minDis > dis) {
					minDis = dis;
					newNearest = newCarRobot;
				}
			}
		}
		return newNearest;
	}

	private void addNewNode2Tree(CarRobot newNode, CarRobot parentNode) {
		connected.add(newNode);
		RRTree.get(parentNode).add(new AdjacentCfg(newNode, 1));
		RRTree.put(newNode, new HashSet<AdjacentCfg>());
	}

	public void growTree2Goal(int num4grow) {
		while (num4grow > 0) {
			// generate new random car
			CarRobot newRandCar = new CarRobot(getRandCfg(map));
			// if the car is valid
			if (!map.carCollision(newRandCar)) {
				CarRobot nearest = findNearestInTree(newRandCar);
				CarRobot newNearest = expandTree(newRandCar, nearest);
				addNewNode2Tree(newNearest, nearest);
				// terminate the iteration if reaching the goal
				if (newNearest.getDistance(goalCar) < 20) {
					addNewNode2Tree(goalCar, newNearest);
					break;
				}
				num4grow--;
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////

	public class RRTnode implements SearchNode {
		public CarRobot car;
		private double cost;

		// construct the connected graph
		public RRTnode(CarRobot inAr, double c) {
			car = inAr;
			cost = c;
		}

		public ArrayList<SearchNode> getSuccessors() {
			ArrayList<SearchNode> successors = new ArrayList<SearchNode>();
			for (AdjacentCfg adj : RRTree.get(car)) {
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
