import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Sampling {
    private static final double percentageThreshold = 0.01;
    private static final double percentageThreshold2 = 0.02;


    public static void main(String[] args) throws Exception {

        Instant start = Instant.now();


        // define I/O utility
//        BufferedReader in = new BufferedReader(new FileReader("data/retail.dat"));
        BufferedReader in = new BufferedReader(new FileReader("data/netflix.data"));
        //  BufferedWriter output = new BufferedWriter(new FileWriter("data/results.txt"));


        int dataSize = 0;
        Map<Integer, Integer> supportMap = new HashMap<>();
        Map<Long, Integer> supportMap2 = new HashMap<>();
//        Set<Integer> uniqueItems;
        String curLine;
        // 1st pass, record the frequency of each item
        while ((curLine = in.readLine()) != null) {

            List<Integer> curBasket = Arrays.asList(curLine.split(" ")).stream()
                    .map(Integer::valueOf).collect(Collectors.toList());

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
//             in = new BufferedReader(new FileReader("data/retail.dat"));
        in = new BufferedReader(new FileReader("data/netflix.data"));

        while ((curLine = in.readLine()) != null) {
            List<Integer> curBasket = Arrays.asList(curLine.split(" ")).stream()
                    .map(Integer::valueOf).filter(freqItems::containsKey).collect(Collectors.toList());


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

        long finishTime = System.currentTimeMillis();

        System.out.println(freqItems.size());
        System.out.println(freqItems2.size());
        Instant end = Instant.now();
        System.out.println(Duration.between(start, end));

    }

    static Long generateKey(int x, int y) {
        long key = ((long) x << 32) | y;
        return Long.valueOf(key);
    }

}









