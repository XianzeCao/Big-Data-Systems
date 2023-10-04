import java.io.*;
import java.util.*;


public class PCY {
    private static final double percentageThreshold = 0.1;

    private static final int datasize = 100000;

    private static final int support = (int) (datasize * percentageThreshold);

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();

        BufferedReader in = new BufferedReader(new FileReader("data/retail.txt"));
        BufferedWriter output = new BufferedWriter(new FileWriter("data/results.txt"));


        Set<String> uniqueItems = new HashSet<>();
        List<String> buckets;
        Map<String, Integer> firstBucket = new HashMap<>();
        Map<String, Integer> supportMap = new HashMap<>();

        String curLine;

        BitSet bitSet = new BitSet(88000);
        // 1st pass
        while ((curLine = in.readLine()) != null) {
            buckets = Arrays.asList(curLine.split(" "));
            uniqueItems.addAll(buckets);

            Set<String> temp = new HashSet<>(buckets);
            for (String item : temp) {
                supportMap.merge(item, 1, Integer::sum);
            }

            //What PCY improved from Apriori
            for (String item_1 : buckets) {

                int index = buckets.indexOf(item_1);

                for (String item_2 : buckets.subList(index + 1, buckets.size())) {

                    if (item_1.equals(item_2)) {
                        continue;
                    }

                    String items = item_1 + " " + item_2;

                    if (firstBucket.containsKey(items)) {
                        firstBucket.put(items, firstBucket.get(items) + 1);
                    } else {
                        firstBucket.put(items, 1);
                    }

                }

            }

        }

        firstBucket.forEach((k, v) -> {
            if (v >= support) {
                bitSet.set(hashFunction(k));
            }
        });

        //generate 1st sequences
        Hashtable<String, Integer> frequentItems = new Hashtable<>();
        supportMap.forEach((k, v) -> {
            if (v >= support) {
                frequentItems.put(k, v);
            }
        });

        //second pass
        //Hashtable<String,Integer> secondPass = new Hashtable<>();
        LinkedHashMap<String, Integer> frequentBucket = new LinkedHashMap<>();
        in = new BufferedReader(new FileReader("data/retail.txt"));

        while ((curLine = in.readLine()) != null) {

            buckets = Arrays.asList(curLine.split(" "));

            for (String item_1 : buckets) {

                int index = buckets.indexOf(item_1);

                for (String item_2 : buckets.subList(index + 1, buckets.size())) {
                    //skip if 2 items are the same
                    if (item_1.equals(item_2)) {
                        continue;
                    }

                    String items = item_1 + " " + item_2;

                    //check if the bit vector is set OR not set
                    if (!bitSet.get(hashFunction(items))) {
                        continue;
                    }
                    if (frequentBucket.containsKey(items)) {

                        frequentBucket.put(items, frequentBucket.get(items) + 1);

                    } else {
                        frequentBucket.put(items, 1);
                    }

                }

            }
        }


    }

    public static Integer hashFunction(String key) {
        return Math.abs(key.hashCode() % datasize);

    }
}
