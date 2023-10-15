import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Sampling {
    private static final double percentageThreshold = 0.1;
    private static final double percentageThreshold2 = 0.02;


    public static void main(String[] args) throws Exception {

        Instant start = Instant.now();


        // define I/O utility
//        BufferedReader in = new BufferedReader(new FileReader("data/retail.dat"));
        BufferedReader in = new BufferedReader(new FileReader("data/netflix.data"));


        List<String> rawData = new ArrayList<>();
        String curLine;
        while ((curLine = in.readLine()) != null) {
            rawData.add(curLine);

        }
        int dataSize = rawData.size();


        double percentage = 0.3;
        double support = dataSize * percentageThreshold * percentage;

        int numToSelect = (int) (dataSize * percentage);

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









