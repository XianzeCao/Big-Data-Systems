import java.io.*;

public class Apriori {
    private static final double percentageThreshold = 0.1;

    private static final int datasize = 100000;

    private static final int support = (int) (datasize * percentageThreshold);

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader("data/retail.txt"));
        BufferedWriter output = new BufferedWriter(new FileWriter("data/results.txt"));
    }


}
