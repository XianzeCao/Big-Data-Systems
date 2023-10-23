import java.io.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


public class Apriori {


    public static void main(String[] args) throws Exception {
        String file1 = "data/retail.dat";
        String file2 = "data/netflix.data";
        String outputPrefix = "output/Apriori-out";
        String suffix = ".txt";

        double percentageThreshold1 = 0.01;
        double percentageThreshold2 = 0.02;

        run(file1, outputPrefix + 1 + suffix, percentageThreshold1);
        run(file1, outputPrefix + 2 + suffix, percentageThreshold2);

        run(file2, outputPrefix + 3 + suffix, percentageThreshold1);
        run(file2, outputPrefix + 4 + suffix, percentageThreshold2);
    }

    static void run(String file, String outputFile, double percentageThreshold) throws Exception {
        Instant start = Instant.now();


        // define I/O utility
        BufferedReader in = new BufferedReader(new FileReader(file));
        BufferedWriter output = new BufferedWriter(new FileWriter(outputFile));



        int dataSize = 0;
        Map<Integer, Integer> supportMap = new HashMap<>();
        Map<Long, Integer> supportMap2 = new HashMap<>();
//        Set<Integer> uniqueItems;
        String curLine;
        // 1st pass, record the frequency of each item
        while ((curLine = in.readLine()) != null) {

            List<Integer> curBasket = Arrays.stream(curLine.split(" "))
                    .map(Integer::valueOf).toList();

            dataSize++;

            for (Integer item : curBasket) {
                supportMap.merge(item, 1, Integer::sum);
            }
        }

        int support = (int) (dataSize * percentageThreshold);

        // filter out the items that are below threshold
        Map<Integer, Integer> freqItems = supportMap.entrySet().stream().filter(entry -> entry.getValue() >= support)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        // 2nd pass
        in = new BufferedReader(new FileReader(file));
//        in = new BufferedReader(new FileReader("data/netflix.data"));

        while ((curLine = in.readLine()) != null) {
            List<Integer> curBasket = Arrays.stream(curLine.split(" "))
                    .map(Integer::valueOf).filter(freqItems::containsKey).toList();


            for (Integer item1 : curBasket) {
                for (Integer item2 : curBasket) {
                    if (item1 >= item2) continue;
                    if (freqItems.containsKey(item1) && freqItems.containsKey(item2)) {
                        Long key = generateKey(item1, item2);
                        supportMap2.merge(key, 1, Integer::sum);
                    }
                }
            }

        }

        // filter out the items that are below threshold

        Map<Long, Integer> freqItems2 = supportMap2.entrySet().stream().filter(entry -> entry.getValue() >= support)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        output.write("Total number of frequent singletons:" + freqItems.size() + "\n");

        output.write("Total number of frequent pairs:" + freqItems2.size() + "\n");


        Instant end = Instant.now();
        output.write("Time cost:" + Duration.between(start, end).toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase()
                + "\n"
        );

        freqItems2.forEach((key, value) -> {
            try {
                output.write(decomposeKey(key) + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        output.close();
        in.close();

    }

    static Long generateKey(int x, int y) {


        long key = ((long) x << 32) | y;

        return Long.valueOf(key);
    }

    static String decomposeKey(long key) {

        int higher = (int) (key >> 32);
        long lower = ((int) key);

        return higher + ", " + lower;
    }


}









