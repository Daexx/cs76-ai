package assignment_mazeworld;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import assignment_mazeworld.SearchProblem.SearchNode;
import assignment_mazeworld.BlindRobotDriver.circlePlusCoord;
import assignment_mazeworld.BlindRobotProblemV2.BlindRobotNode;
import assignment_mazeworld.BlindRobotProblemV2.Coordinate;

;

;

public class BlindRobotDriverV2 extends Application {

	public class circlePlusCoord {
		Circle cir;
		Coordinate xy;

		circlePlusCoord(Circle c, Coordinate coord) {
			cir = c;
			xy = coord;
		}
	}

	Maze maze;

	// instance variables used for graphical display
	private static final int PIXELS_PER_SQUARE = 40;
	MazeView mazeView;
	List<AnimationPath> animationPathList;

	// some basic initialization of the graphics; needs to be done before
	// runSearches, so that the mazeView is available
	private void initMazeView() {
		maze = Maze.readFromFile("simple.maz");

		animationPathList = new ArrayList<AnimationPath>();
		// build the board
		mazeView = new MazeView(maze, PIXELS_PER_SQUARE);

	}

	// assumes maze and mazeView instance variables are already available
	private void runSearches() {

		BlindRobotProblemV2 mazeProblem = new BlindRobotProblemV2(maze, 0, 0,
				3, 4);

		List<SearchNode> astarPath = mazeProblem.astarSearch();
		animationPathList.add(new AnimationPath(mazeView, astarPath));
		System.out.println("A*:  ");
		mazeProblem.printStats();
		System.out.println("  path length: " + astarPath.size());
		System.out.println("  cost: "
				+ astarPath.get(astarPath.size() - 1).getCost());
	}

	public static void main(String[] args) {
		launch(args);
	}

	// javafx setup of main view window for mazeworld
	@Override
	public void start(Stage primaryStage) {

		initMazeView();

		primaryStage.setTitle("CS 76 Mazeworld");

		// add everything to a root stackpane, and then to the main window
		StackPane root = new StackPane();
		root.getChildren().add(mazeView);
		primaryStage.setScene(new Scene(root));

		primaryStage.show();

		// do the real work of the driver; run search tests
		runSearches();

		// sets mazeworld's game loop (a javafx Timeline)
		Timeline timeline = new Timeline(0.1);
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.seconds(.05), new GameHandler()));
		timeline.playFromStart();

	}

	// every frame, this method gets called and tries to do the next move
	// for each animationPath.
	private class GameHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			// System.out.println("timer fired");
			for (AnimationPath animationPath : animationPathList) {
				// note: animationPath.doNextMove() does nothing if the
				// previous animation is not complete. If previous is complete,
				// then a new animation of a piece is started.
				animationPath.doNextMove();
			}
		}
	}

	// each animation path needs to keep track of some information:
	// the underlying search path, the "piece" object used for animation,
	// etc.
//	private class AnimationPath {
//		private Circle[] piece;
//		private List<SearchNode> searchPath;
//		private int currentMove = 0;
//
//		private int[] lastX;
//		private int[] lastY;
//
//		private int cntR;
//
//		boolean animationDone = true;
//
//		public AnimationPath(MazeView mazeView, List<SearchNode> path) {
//			searchPath = path;
//			BlindRobotNode firstNode = (BlindRobotNode) searchPath.get(0);
//			cntR = firstNode.allCoord.size();
//			piece = new Circle[cntR];
//			lastX = new int[cntR];
//			lastY = new int[cntR];
//			int r = 0;
//			for (Coordinate xy : firstNode.allCoord) {
//				piece[r] = mazeView.addPiece(xy.getX(), xy.getX());
//				lastX[r] = xy.getX();
//				lastY[r] = xy.getY();
//			}
//		}
//
//		// try to do the next step of the animation. Do nothing if
//		// the mazeView is not ready for another step.
//		public void doNextMove() {
//
//			// animationDone is an instance variable that is updated
//			// using a callback triggered when the current animation
//			// is complete
//			if (currentMove < searchPath.size() && animationDone) {
//				BlindRobotNode mazeNode = (BlindRobotNode) searchPath
//						.get(currentMove);
//				int[] dx = new int[cntR];
//				int[] dy = new int[cntR];
//				int r = 0;
//				for (Coordinate xy : mazeNode.allCoord) {
//					dx[r] = xy.getX() - lastX[r];
//					dy[r] = xy.getY() - lastY[r];
//					r++;
//				}
//				// System.out.println("animating " + dx + " " + dy);
//				// mazeView.footPrint(lastX, lastY, piece, (dx + 2) * 10 + dy +
//				// 2);
//				animateMove(piece, dx, dy);
//				mazeView.footPrint4Multi(lastX, lastY, piece, dx, dy);
//				r = 0;
//				for (Coordinate xy : mazeNode.allCoord) {
//					lastX[r] = xy.getX() - lastX[r];
//					lastY[r] = xy.getY() - lastY[r];
//					r++;
//				}
//				currentMove++;
//			}
//
//		}
//
//		// move the piece n by dx, dy cells
//		public void animateMove(Circle[] H, int[] dx, int[] dy) {
//			animationDone = false;
//			for (int r = 0; r < cntR; r++) {
//				TranslateTransition tt = new TranslateTransition(
//						Duration.millis(100), H[r]);
//				tt.setByX(PIXELS_PER_SQUARE * dx[r]);
//				tt.setByY(-PIXELS_PER_SQUARE * dy[r]);
//				// set a callback to trigger when animation is finished
//				tt.setOnFinished(new AnimationFinished());
//				tt.play();
//			}
//		}
//
//		// when the animation is finished, set an instance variable flag
//		// that is used to see if the path is ready for the next step in the
//		// animation
//		private class AnimationFinished implements EventHandler<ActionEvent> {
//			@Override
//			public void handle(ActionEvent event) {
//				animationDone = true;
//			}
//		}
//	}
	private class AnimationPath {
		private Circle piece;
		private ArrayList<circlePlusCoord> candidates;
		private List<SearchNode> searchPath;
		private int currentMove = 0;

		private int lastX;
		private int lastY;

		boolean animationDone = true;

		public AnimationPath(MazeView mazeView, List<SearchNode> path) {
			searchPath = path;
			BlindRobotNode firstNode = (BlindRobotNode) searchPath.get(0);
			candidates = new ArrayList<circlePlusCoord>();
			for (Coordinate xy : firstNode.allCoord) {
				candidates.add(new circlePlusCoord(mazeView.addCandidate(xy.getX(),
						xy.getY()), xy) );
			}
			piece = mazeView.addPiece(firstNode.getX(), firstNode.getY());
			piece.setVisible(false);
			lastX = firstNode.getX();
			lastY = firstNode.getY();
		}

		// try to do the next step of the animation. Do nothing if
		// the mazeView is not ready for another step.
		public void doNextMove() {

			// animationDone is an instance variable that is updated
			// using a callback triggered when the current animation
			// is complete
			if (currentMove < searchPath.size() && animationDone) {
				BlindRobotNode mazeNode = (BlindRobotNode) searchPath
						.get(currentMove);
				for (circlePlusCoord cd : candidates) {
					if (!mazeNode.allCoord.contains(cd.xy)) {
						cd.cir.setVisible(false);
					}
					else
						cd.cir.setVisible(true);
				}
				int dx = mazeNode.getX() - lastX;
				int dy = mazeNode.getY() - lastY;
				// System.out.println("animating " + dx + " " + dy);
				//mazeView.footPrint(lastX, lastY, piece, (dx + 2) * 10 + dy + 2);
				animateMove(piece, dx, dy);
				lastX = mazeNode.getX();
				lastY = mazeNode.getY();

				currentMove++;
			}

		}

		// move the piece n by dx, dy cells
		public void animateMove(Node n, int dx, int dy) {
			animationDone = false;
			TranslateTransition tt = new TranslateTransition(
					Duration.millis(100), n);
			tt.setByX(PIXELS_PER_SQUARE * dx);
			tt.setByY(-PIXELS_PER_SQUARE * dy);
			// set a callback to trigger when animation is finished
			tt.setOnFinished(new AnimationFinished());

			tt.play();

		}

		// when the animation is finished, set an instance variable flag
		// that is used to see if the path is ready for the next step in the
		// animation
		private class AnimationFinished implements EventHandler<ActionEvent> {
			@Override
			public void handle(ActionEvent event) {
				animationDone = true;
			}
		}
	}
}