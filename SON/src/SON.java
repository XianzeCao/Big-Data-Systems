import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class SON {
    private static final double percentageThreshold = 0.01;
    private static final double percentageThreshold2 = 0.2;

    private static final int segmentSize = 1000;


    public static void main(String[] args) throws Exception {

        // define I/O utility
        BufferedReader in = new BufferedReader(new FileReader("data/retail.dat"));
//        BufferedReader in = new BufferedReader(new FileReader("data/netflix.data"));
        //  BufferedWriter output = new BufferedWriter(new FileWriter("data/results.txt"));

        long startTime = System.currentTimeMillis();

        int dataSize = 0;
        Map<Integer, Integer> supportMap = new HashMap<>();
        Map<String, Integer> supportMap2 = new HashMap<>();
//        Set<Integer> uniqueItems;
        String curLine;
        // 1st pass, record the frequency of each item

        int lineCount = 0;
        while ((curLine = in.readLine()) != null) {



            List<Integer> curBasket = Arrays.asList(curLine.split(" ")).stream()
                    .map(Integer::valueOf).collect(Collectors.toList());

            dataSize++;
            lineCount++;

            if (lineCount % segmentSize == 0) {

            }

            for (Integer item : curBasket) {
                supportMap.merge(item, 1, Integer::sum);
            }
        }

        int support = (int) (dataSize * percentageThreshold);

        // filter out the items that are below threshold
        Map<Integer, Integer> freqItems = supportMap.entrySet().stream().filter(entry -> entry.getValue() >= support)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));



        // 2nd pass
             in = new BufferedReader(new FileReader("data/retail.dat"));
//        in = new BufferedReader(new FileReader("data/netflix.data"));

        while ((curLine = in.readLine()) != null) {
            List<Integer> curBasket = Arrays.asList(curLine.split(" ")).stream()
                    .map(Integer::valueOf).filter(freqItems::containsKey).collect(Collectors.toList());
            Collections.sort(curBasket);
            for (int i = 0; i < curBasket.size(); i++) {
                for (int j = i + 1; j < curBasket.size(); j++) {
                    Integer item1 = curBasket.get(i);
                    Integer item2 = curBasket.get(j);
                    if (freqItems.containsKey(item1) && freqItems.containsKey(item2)) {
                        String key = item1 + "," + item2;
                        supportMap2.merge(key, 1, Integer::sum);
                    }
                }
            }

        }

        // filter out the items that are below threshold
        Map<String, Integer> freqItems2 = supportMap2.entrySet().stream().filter(entry -> entry.getValue() >= support)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        System.out.println(freqItems.size());
        System.out.println(freqItems2.size());
    }

}









