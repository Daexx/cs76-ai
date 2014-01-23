package assignment_mazeworld;

import java.io.*;

public class MazeGen {
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		String filename = "simple.maz";
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		
		int h = 7, w = 7;
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				Double rnd = SimplexNoise.noise(y, x);
				if(rnd > 0.45) {
//					if(x > size / 2 && y < size / 2)
//						writer.print((int)(Math.random() * 10. % 2 + 1));
//					else if(x < size / 2 && y > size / 2)
//						writer.print((int)(Math.random() * 10. % 4 + 5));
//					else 
//						writer.print((int)(Math.random() * 10. % 3 + 3));
//					writer.print((int)(rnd * 10. % 9 + 1));
					//writer.print((int)(Math.random() * 10. % 9 + 1));
					writer.print("#");
				}
				else
					//writer.print(0);
					writer.print(".");
			}
			writer.println();
		}
		
		writer.close();
	}
}