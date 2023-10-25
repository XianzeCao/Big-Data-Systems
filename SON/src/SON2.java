import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class SON2 {


    public static void main(String[] args) throws Exception {


        String file1 = "data/retail.dat";
        String file2 = "data/netflix.data";
        String outputPrefix = "output/SON-out";
        String suffix = ".txt";

        double percentageThreshold1 = 0.01;
        double percentageThreshold2 = 0.02;

//        run(file1, outputPrefix + 1 + suffix, percentageThreshold1);
//        run(file1, outputPrefix + 2 + suffix, percentageThreshold2);
//
//        run(file2, outputPrefix + 3 + suffix, percentageThreshold1);
        run(file2, outputPrefix + 4 + suffix, percentageThreshold2);
    }

    static void run(String file, String outputFile, double percentageThreshold) throws Exception {
        Instant start = Instant.now();


        BufferedReader in = new BufferedReader(new FileReader(file));
        BufferedWriter output = new BufferedWriter(new FileWriter(outputFile));
        int dataSize = 0;
        while (in.readLine() != null) {
            dataSize++;
        }

        int threshold = (int) (dataSize * percentageThreshold);

        int numPartition = 10;
        int segmentSize = dataSize / numPartition;

        int segmentThreshold = threshold / numPartition;


        in = new BufferedReader(new FileReader(file));

        Map<Integer, Integer> supportMap = new HashMap<>();
        Map<Long, Integer> supportMap2 = new HashMap<>();

        Set<Integer> candidates = new HashSet<>();
        String curLine;

        // 1st pass, record the frequency of each item
        int counter = 0;
        while ((curLine = in.readLine()) != null) {

            if (counter == segmentSize) {
                candidates.addAll(supportMap.entrySet().stream().filter(entry -> entry.getValue() >= segmentThreshold)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet()));
                supportMap.clear();
                counter = 0;
            }

            List<Integer> curBasket = Arrays.stream(curLine.split(" "))
                    .map(Integer::valueOf).toList();

            for (Integer item1 : curBasket) {
                supportMap.merge(item1, 1, Integer::sum);

            }
            counter++;


        }


        in = new BufferedReader(new FileReader(file));

        Set<Long> seen = new HashSet<>();


        while ((curLine = in.readLine()) != null) {
            List<Integer> curBasket = Arrays.stream(curLine.split(" "))
                    .map(Integer::valueOf).filter(candidates::contains).toList();


            for (Integer item1 : curBasket) {
                for (Integer item2 : curBasket) {
                    if (item1 >= item2) continue;
                    Long key = generateKey(item1, item2);
                    supportMap2.merge(key, 1, Integer::sum);
                    if (supportMap2.get(key) >= threshold && !seen.contains(key)) {
                        seen.add(key);
                        output.write("pair count : " + seen.size() + " items: ");
                        output.write(decomposeKey(key) + "\n");

                    }

                }
            }


        }

        // filter out the items that are below threshold
        Map<Long, Integer> freqItems2 = supportMap2.entrySet().stream().filter(entry -> entry.getValue() >= threshold)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        output.write("Total number of frequent pairs:" + freqItems2.size() + "\n");


        Instant end = Instant.now();
        output.write("Time cost:" + Duration.between(start, end).toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase()
                + "\n"
        );

//        freqItems2.forEach((key, value) -> {
//            try {
//                output.write(decomposeKey(key) + "\n");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });


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









