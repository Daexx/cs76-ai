package assignment_mazeworld;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Maze {
	final static Charset ENCODING = StandardCharsets.UTF_8;

	// A few useful constants to describe actions
	public static int[] NORTH = {0, 1};
	public static int[] EAST = {1, 0};
	public static int[] SOUTH = {0, -1};
	public static int[] WEST = {-1, 0};
	public static int[] STAY = {0, 0};
	
	public int width;
	public int height;
	
	private Integer[][] grid;

	public static Maze readFromFile(String filename) {
		Maze m = new Maze();

		try {
			List<String> lines = readFile(filename);
			m.height = lines.size();

			int y = 0;
			m.grid = new Integer[m.height][];
			for (String line : lines) {
				m.width = line.length();
				m.grid[m.height - y - 1] = new Integer[m.width];
				for (int x = 0; x < line.length(); x++) {
					// (0, 0) should be bottom left, so flip y as 
					//  we read from file into array:
					Character tmpc = line.charAt(x);
					if(tmpc == '.')
						m.grid[m.height - y - 1][x] = 0;
					else if (tmpc == '#'){
						m.grid[m.height - y - 1][x] = -1;
					}else {
						m.grid[m.height - y - 1][x] = Character.getNumericValue(tmpc);
					}
				}
				y++;

				// System.out.println(line.length());
			}

			return m;
		} catch (IOException E) {
			return null;
		}
	}

	private static List<String> readFile(String fileName) throws IOException {
		Path path = Paths.get(fileName);
		return Files.readAllLines(path, ENCODING);
	}

	public Integer getInt(int x, int y) {
		return grid[y][x];
	}
	
	// is the location x, y on the map, and also a legal floor tile (not a wall)?
	public boolean isLegal(int x, int y) {
		// on the map
		if(x >= 0 && x < width && y >= 0 && y < height) {
			// and it's a floor tile, not a wall tile:
			return getInt(x, y) != -1;
		}
		return false;
	}
	
	// is the location x, y on the map, and also a legal floor tile (not a wall)?
	public boolean isLegal(Double x, Double y) {
		// on the map
		if(x >= 0 && x < width && y >= 0 && y < height) {
			// and it's a floor tile, not a wall tile:
			return getInt(x.intValue(), y.intValue()) != -1;
		}
		return false;
	}
	
	
	public String toString() {
		String s = "";
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				s += grid[y][x];
			}
			s += "\n";
		}
		return s;
	}

	public static void main(String args[]) {
		Maze m = Maze.readFromFile("simple.maz");
		System.out.println(m);
	}

}
