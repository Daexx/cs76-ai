package assignment_mazeworld;

import java.util.ArrayList;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class MazeView extends Group {

	private int pixelsPerSquare;
	private Maze maze;
	private ArrayList<Node> pieces;
	
	private int numCurrentAnimations;
	
	private static Color[] colors = {Color.RED, Color.ORANGE, Color.BLACK, Color.BROWN,
		Color.DARKGOLDENROD, Color.GREEN, Color.BLUE, Color.VIOLET, Color.CRIMSON};

	int currentColor;
	
	public MazeView(Maze m, int pixelsPerSquare) {
		currentColor = 0;
		
		pieces = new ArrayList<Node>();
		
		maze = m;
		this.pixelsPerSquare = pixelsPerSquare;

//		Color colors[] = { Color.LIGHTGRAY, Color.WHITE };
	//	int color_index = 1; // alternating index to select tile color

		for (int c = 0; c < maze.width; c++) {
			for (int r = 0; r < maze.height; r++) {

				int x = c * pixelsPerSquare;
				int y = (maze.height - r - 1) * pixelsPerSquare;

				Rectangle square = new Rectangle(x, y, pixelsPerSquare,
						pixelsPerSquare);

				square.setStroke(Color.GRAY);
				if(maze.getChar(c, r) == '.') {
					square.setFill(Color.WHITE);
				} else {
					square.setFill(Color.LIGHTGRAY);
				}
				

				//Text t = new Text(x, y + 12, "" + Chess.colToChar(c)
					//	+ Chess.rowToChar(r));

				this.getChildren().add(square);
				//this.getChildren().add(t);

		
			}
		
		}

		

	}

	private int squareCenterX(int c) {
		return c * pixelsPerSquare + pixelsPerSquare / 2;
		
	}
	private int squareCenterY(int r) {
		return (maze.height - r) * pixelsPerSquare - pixelsPerSquare / 2;
	}
	
	// create a new piece on the board.
	//  return the piece as a Node for use in animations
	public Circle addPiece(int c, int r) {
		
		int radius = (int)(pixelsPerSquare * .4);

		Circle piece = new Circle(squareCenterX(c), squareCenterY(r), radius);
		piece.setFill(colors[currentColor]);
		currentColor++;
		this.getChildren().add(piece);
		return piece;
	}
	
	// create a new piece on the board.
	//  return the piece as a Node for use in animations
	public void footPrint(int c, int r, Circle oldPiece, int direction) {
		int radius = (int)(pixelsPerSquare * .4);
		Double x = squareCenterX(c) + (oldPiece.getFill().hashCode() / 2500000.);
		Double y = squareCenterY(r) + (oldPiece.getFill().hashCode() / 2500000.);
		
		if(direction == 22) return;

		Polygon piece = new Polygon();
		piece.getPoints().addAll(new Double[]{
			     x, y + radius,
			    x - radius * 0.466, y - radius / 2.0,
			    x + radius * 0.466, y - radius / 2.0 });
		piece.setFill(oldPiece.getFill());
		if(direction == 32)
			piece.setRotate(270);
		else if(direction == 23)
			piece.setRotate(180);
		else if(direction == 12)
			piece.setRotate(90);
		else if(direction == 21)
			piece.setRotate(0);
		
		this.getChildren().add(piece);		
	}
	
	public void footPrint4Multi(int[] lastX, int[] lastY, Circle[] oldPiece, int[] dx, int[] dy) {
		int radius = (int)(pixelsPerSquare * .4);
		
		for(int r = 0; r < lastX.length; r++) {
			Double x = squareCenterX(lastX[r]) + (oldPiece[r].getFill().hashCode() / 2500000.);
			Double y = squareCenterY(lastY[r]) + (oldPiece[r].getFill().hashCode() / 2500000.);
			
			int direction = (dx[r] + 2) * 10 + dy[r] + 2;
			if(direction == 22) return;
	
			Polygon piece = new Polygon();
			piece.getPoints().addAll(new Double[]{
				     x, y + radius,
				    x - radius * 0.466, y - radius / 2.0,
				    x + radius * 0.466, y - radius / 2.0 });
			piece.setFill(oldPiece[r].getFill());
			if(direction == 32)
				piece.setRotate(270);
			else if(direction == 23)
				piece.setRotate(180);
			else if(direction == 12)
				piece.setRotate(90);
			else if(direction == 21)
				piece.setRotate(0);
			
			this.getChildren().add(piece);
		}
	}
	
	/*
	public boolean doMove(short move) {
	
		
		Timeline timeline = new Timeline();

		if (timeline != null) {
			timeline.stop();
		}

		animateMove(l, c2 - c1, r2 - r1);

		this.game.doMove(move);

		return true;



	}
	
	*/




}
