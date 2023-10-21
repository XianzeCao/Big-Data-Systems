import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MultithreadSON {
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

        int threshold = (int) (dataSize * percentageThreshold);

        int numPartition = 1000;
        int segmentSize = dataSize / numPartition;

        int segmentThreshold = threshold / numPartition;
        for (int i = 0; i < numPartition; i++) {

        }


//        in = new BufferedReader(new FileReader("data/retail.dat"));
        in = new BufferedReader(new FileReader("data/netflix.data"));


        String curLine;

        ExecutorService executor = Executors.newFixedThreadPool(100);
        // 1st pass, record the frequency of each item

        ConcurrentHashMap<Long, Integer> supportMap = new ConcurrentHashMap<>();

        while ((curLine = in.readLine()) != null) {


            // Submit tasks to the thread pool
            List<Integer> curBasket = Arrays.stream(curLine.split(" "))
                    .map(Integer::valueOf).toList();

            Map<Long, Integer> curMap = new HashMap<>();
            Runnable worker = new Worker(curBasket, threshold, curMap);



            executor.execute(worker);
            curMap.forEach((key, value) ->
                    supportMap.merge(key, value, Integer::sum));
        }
        // Shutdown the executor after tasks are done
        executor.shutdown();



        // 2nd pass
        in = new BufferedReader(new FileReader("data/retail.dat"));
//        in = new BufferedReader(new FileReader("data/netflix.data"));


        // filter by threshold


        while ((curLine = in.readLine()) != null) {
            List<Integer> curBasket = Arrays.stream(curLine.split(" "))
                    .map(Integer::valueOf).filter(freqItems::containsKey).collect(Collectors.toSet());


            // verifying that whether each candidate is actually frequent or not


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
        return ((long) x << 32) | y;
    }

    static class Worker implements Runnable {

        List<Integer> curBasket;

        public Worker(List<Integer> curBasket, int threshold, Map<Long, Integer> candidates) {
            this.curBasket = curBasket;
        }

        @Override
        public void run() {

            while ((curLine = in.readLine()) != null) {
                List<Integer> curBasket = Arrays.stream(curLine.split(" "))
                        .map(Integer::valueOf).filter(freqItems::containsKey).toList();


                for (Integer item1 : curBasket) {
                    for (Integer item2 : curBasket) {
                        if (item1 >= item2) continue;
                        Long key = generateKey(item1, item2);
                        supportMap2.merge(key, 1, Integer::sum);

                    }
                }


            }


        }
    }


}










