import java.io.*;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by JackGuan on 2/26/14.
 */
public class DriverCSoneGen {
    public static void main(String args[]) throws IOException {
        int leaders = 3;
        int students = 30;
        Random rand = new Random(11);
        HashSet<Integer> exit = new HashSet<>();
        int time;

        File file = new File("dataset.txt");
        FileWriter filewriter = new FileWriter(file);
        for (int i = 0; i < leaders; i++) {
            int id = 100 + i;
            filewriter.write(id + "\t");
//            int availableTimes = rand.nextInt(10) + 3;
            int availableTimes = 14;
            exit.clear();
            for (int d = 0; d < availableTimes; d++) {
                do {
                    time = (rand.nextInt(5) + 1) * 10 + rand.nextInt(3) + 1;
                } while (!exit.contains(time));
                filewriter.write(time + "\t");
            }
            filewriter.write("\n");
        }
        for (int i = 0; i < students; i++) {
            int id = i;
            filewriter.write(id + "\t");
//            int availableTimes = rand.nextInt(10) + 5;
            int availableTimes = 14;
            for (int d = 0; d < availableTimes; d++) {
                do {
                    time = (rand.nextInt(5) + 1) * 10 + rand.nextInt(3) + 1;
                } while (!exit.contains(time));
                filewriter.write(time + "\t");
            }
            filewriter.write("\n");
        }
        filewriter.close();
    }
}
