package assignment_robots;

import java.util.*;

import assignment_robots.RoadMapProblem.AdjacentCfg;
import assignment_robots.SearchProblem.SearchNode;
import assignment_robots.InformedSearchProblem;

public class Bi_RRT extends InformedSearchProblem {
	
	static boolean TreeA = true, TreeB = false;

	CarRobot startCar, goalCar;
	HashSet<CarRobot> connectedA = new HashSet<>(),
			connectedB = new HashSet<>(); // world sampling
	HashMap<CarRobot, HashSet<AdjacentCfg>> RRTreeA = new HashMap<>(),
			RRTreeB = new HashMap<>(); // RRTree,
	CarRobot bridgeAside = null, bridgeBside = null;
	World map;
	int num4grow;

	public Bi_RRT(World m, CarState config1, CarState config2, int density) {
		map = m;
		num4grow = density;
		startCar = new CarRobot(config1);
		goalCar = new CarRobot(config2);
		startNode = new BiRRTnode(startCar, 0, TreeA);
		RRTreeA.put(startCar, new HashSet<AdjacentCfg>());
		RRTreeB.put(goalCar, new HashSet<AdjacentCfg>());
		connectedA.add(startCar);
		connectedB.add(goalCar);
		biGrowTree2Goal();

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

	private CarRobot findNearestInTree(CarRobot newRandCar, boolean tree) {
		HashSet<CarRobot> connected = tree == TreeA ? connectedA : connectedB;
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
		CarRobot newAdded = null, newCarRobot = new CarRobot();
		// try to expand to 6 different directions
		for (int i = 0; i <= 5; i++) {
			newCarRobot = new CarRobot(sc.move(nearest.getCarState(), i, 1.));
			// System.out.println("newCarRobot: " + newCarRobot);
			if (!map.carCollision(newCarRobot)) {
				double dis = newCarRobot.getDistance(newRandCar);
				if (minDis > dis) {
					minDis = dis;
					newAdded = newCarRobot;
				}
			}
		}
		return newAdded;
	}

	private void addNewNode2Tree(CarRobot newNode, CarRobot parentNode, boolean tree) {
		HashSet<CarRobot> connected = tree ? connectedA : connectedB;
		HashMap<CarRobot, HashSet<AdjacentCfg>> RRTree = tree ? RRTreeA : RRTreeB;
		connected.add(newNode);
		if(tree) {
			// in treeA, parent points to its children, 1 to n
			RRTree.get(parentNode).add(new AdjacentCfg(newNode, 1));
			RRTree.put(newNode, new HashSet<AdjacentCfg>());
		}else {
			// in treeB, the child points to their parent, 1 to 1
			RRTree.put(newNode, new HashSet<AdjacentCfg>());
			RRTree.get(newNode).add(new AdjacentCfg(parentNode, 1));
		}
	}
	
	boolean growTheOther(boolean tree, CarRobot target) {
		HashSet<CarRobot> connected = tree ? connectedA : connectedB;
		CarRobot nearest = findNearestInTree(target, tree);
		CarRobot newAdded = expandTree(target, nearest);
		if (!connected.contains(newAdded)) {
			addNewNode2Tree(newAdded, nearest, tree);
			// terminate the iteration if reaching the goal
			if (newAdded.getDistance(target) < 20) {
				if(tree) {
					bridgeAside = newAdded;
					bridgeBside = target;
				}
				else {
					bridgeAside = target;
					bridgeBside = newAdded;
					RRTreeB.put(newAdded, new HashSet<AdjacentCfg>());
					RRTreeB.get(newAdded).add(new AdjacentCfg(nearest, 1));
				}
				return true;
			}
			num4grow--;
		}
		return false;
	}

	public void biGrowTree2Goal() {
		boolean tree = TreeA;
		while (num4grow > 0) {
			// generate new random car
			CarRobot newRandCar = new CarRobot(getRandCfg(map));
			// initiate current tree set
			HashSet<CarRobot> connected = tree ? connectedA : connectedB,
									theOther = tree ? connectedA : connectedB;
			// if the car is valid
			if (!map.carCollision(newRandCar)) {
				CarRobot nearest = findNearestInTree(newRandCar, tree);
				CarRobot newAdded = expandTree(newRandCar, nearest);
				if (!connected.contains(newAdded)) {
					addNewNode2Tree(newAdded, nearest, tree);
					num4grow--;
					// terminate the iteration if two tree connected
					if (growTheOther(!tree, newAdded)) break;
				}
			}
			// swap if current tree size exceeds the other
			if(connected.size() > theOther.size()) tree = !tree;
		}
	}

	// /////////////////////////////////////////////////////////////////////

	public class BiRRTnode implements SearchNode {
		public CarRobot car;
		private double cost;
		private boolean tree;

		// construct the connected graph
		public BiRRTnode(CarRobot inAr, double c, boolean t) {
			car = inAr;
			cost = c;
			tree = t;
		}

		public ArrayList<SearchNode> getSuccessors() {
			HashMap<CarRobot, HashSet<AdjacentCfg>> RRTree = tree ? RRTreeA : RRTreeB;
			ArrayList<SearchNode> successors = new ArrayList<SearchNode>();
			// no brdige, no way
			if(bridgeAside == null) return successors;
			for (AdjacentCfg adj : RRTree.get(car)) {
				if(tree && bridgeAside.getDistance(adj.ar) < 20) {
					System.out.println("going to B");
					successors.clear();
					successors.add(new BiRRTnode(bridgeBside, cost, !tree));
					return successors;
				}else {
					successors.add(new BiRRTnode(adj.ar, adj.dis + cost, tree));
				}
			}
			return successors;
		}

		// override functions
		@Override
		public boolean equals(Object other) {
			return car == ((BiRRTnode) other).car;
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
			// if in the treeB, we should simply go straight to the goal
			// so I set priority as high as possible
			if(tree)
				return car.getDistance(goalCar);
			else
				return -cost;
		}

		@Override
		public double priority() {
			// TODO Auto-generated method stub
			return heuristic() + getCost();
		}
	}

}
