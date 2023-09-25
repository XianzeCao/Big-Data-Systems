import java.io.*;
import java.util.*;

public class Apriori {
    private static final double percentageThreshold = 0.1;

    private static final int datasize = 100000;

    private static final int support = (int) (datasize * percentageThreshold);

    public static void main(String[] args) throws Exception {


        BufferedReader in = new BufferedReader(new FileReader("data/retail.txt"));
        BufferedWriter output = new BufferedWriter(new FileWriter("data/results.txt"));

        long startTime = System.currentTimeMillis();
        Set<String> uniqueItems = new HashSet<>();
        List<String> buckets;
        Map<String, Integer> supportMap = new HashMap<>();

        String curLine;
        // 1st pass
        while ((curLine = in.readLine()) != null) {
            buckets = Arrays.asList(curLine.split(" "));
            uniqueItems.addAll(buckets);

            Set<String> temp = new HashSet<>(buckets);
            for (String item : temp) {
                supportMap.merge(item, 1, Integer::sum);
            }
        }


        // 2nd pass


    }


}
