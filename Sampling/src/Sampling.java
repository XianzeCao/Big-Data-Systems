import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Sampling {
    private static final double percentageThreshold = 0.1;
    private static final double percentageThreshold2 = 0.02;


    public static void main(String[] args) throws Exception {


        String file1 = "data/retail.dat";
        String file2 = "data/netflix.data";
        String outputPrefix = "output/Sampling-out";
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


        // define I/O utility

        BufferedReader in = new BufferedReader(new FileReader(file));
        BufferedWriter output = new BufferedWriter(new FileWriter(outputFile));

        List<String> rawData = new ArrayList<>();
        String curLine;
        while ((curLine = in.readLine()) != null) {
            rawData.add(curLine);

        }
        int dataSize = rawData.size();


        double samplingFactor = 0.3;
        double support = dataSize * percentageThreshold * samplingFactor;

        int numToSelect = (int) (dataSize * samplingFactor);

        Collections.shuffle(rawData);
        List<String> selectedData = rawData.subList(0, numToSelect);


        Map<Integer, Integer> supportMap = new HashMap<>();
        Map<Long, Integer> supportMap2 = new HashMap<>();


        for (String line : selectedData) {
            List<Integer> curBasket = Arrays.asList(line.split(" ")).stream()
                    .map(Integer::valueOf).collect(Collectors.toList());
            for (Integer item : curBasket) {
                supportMap.merge(item, 1, Integer::sum);
            }
        }


        Map<Integer, Integer> freqItems = supportMap.entrySet().stream().filter(entry -> entry.getValue() >= support)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        for (String line : selectedData) {
            List<Integer> curBasket = Arrays.stream(line.split(" "))
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









