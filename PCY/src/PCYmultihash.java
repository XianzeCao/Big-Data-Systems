import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class PCYmultihash {
    private static final double percentageThreshold = 0.01;
    private static final double percentageThreshold2 = 0.02;


    public static void main(String[] args) throws Exception {


        Instant start = Instant.now();
        // define I/O utility
//        BufferedReader in = new BufferedReader(new FileReader("data/retail.dat"));
        BufferedReader in = new BufferedReader(new FileReader("data/netflix.data"));
        //  BufferedWriter output = new BufferedWriter(new FileWriter("data/results.txt"));
        int dataSize = 0;
        while (in.readLine() != null) {
            dataSize++;
        }

        int hashSpace = dataSize * 1000;

        int[] hashtable = new int[hashSpace];

//        in = new BufferedReader(new FileReader("data/retail.dat"));
        in = new BufferedReader(new FileReader("data/netflix.data"));

        Map<Integer, Integer> supportMap = new HashMap<>();
        Map<Long, Integer> supportMap2 = new HashMap<>();

        Map<Integer, Integer> hashedPairs = new HashMap<>();

//        Set<Integer> uniqueItems;
        String curLine;
        // 1st pass, record the frequency of each item
        while ((curLine = in.readLine()) != null) {

            List<Integer> curBasket = Arrays.stream(curLine.split(" "))
                    .map(Integer::valueOf).toList();

            for (Integer item1 : curBasket) {
                supportMap.merge(item1, 1, Integer::sum);
                for (Integer item2 : curBasket) {
                    if (item1 >= item2) continue;
                    hashtable[HashFunction.hash(item1, item2, dataSize)]++;
                }

            }


        }


        int threshold = (int) (dataSize * percentageThreshold);

        BitSet bitmap = new BitSet(hashSpace);
        for (int i = 0; i < hashSpace; i++) {
            if (hashtable[i] >= threshold) {
                bitmap.set(i);
            }
        }

        // filter out the items that are below threshold
        Map<Integer, Integer> freqItems = supportMap.entrySet().stream().filter(entry -> entry.getValue() >= threshold)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        // covert hashtable to bitmap , since this is a java program the smallest object is byte, so we're using byte instead


        // 2nd pass
//        in = new BufferedReader(new FileReader("data/retail.dat"));
        in = new BufferedReader(new FileReader("data/netflix.data"));

        while ((curLine = in.readLine()) != null) {
            List<Integer> curBasket = Arrays.stream(curLine.split(" "))
                    .map(Integer::valueOf).filter(freqItems::containsKey).toList();


            for (Integer item1 : curBasket) {
                for (Integer item2 : curBasket) {
                    if (item1 >= item2) continue;
                    if (bitmap.get(HashFunction.hash(item1, item2, dataSize))) {
                        Long key = generateKey(item1, item2);
                        supportMap2.merge(key, 1, Integer::sum);
                    }
                }
            }


        }

        // filter out the items that are below threshold
        Map<Long, Integer> freqItems2 = supportMap2.entrySet().stream().filter(entry -> entry.getValue() >= threshold)
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








