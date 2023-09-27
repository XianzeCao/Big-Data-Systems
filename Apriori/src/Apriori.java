import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

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


        Map<String, Integer> freqItems = supportMap.entrySet().stream().filter(entry -> entry.getValue() >= support)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        // 2nd pass
        Map<String, Integer> secondPass = new HashMap<>();

        in = new BufferedReader(new FileReader("data/retail.txt"));

        while ((curLine = in.readLine()) != null) {
            buckets = Arrays.asList(curLine.split(" "));

            for (String item1 : buckets) {
                int index = buckets.indexOf(item1);
                for (String item2 : buckets.subList(index + 1, buckets.size())) {
                    if (item1.equals(item2)) {
                        continue;
                    }

                    if (freqItems.containsKey(item1) && freqItems.containsKey(item2)) {
                        String key = item1 + "," + item2;
                        secondPass.merge(key, 1, Integer::sum);

                    }


                }
            }

        }

        Map<String, Integer> freqPairs = secondPass.entrySet().stream()
                .filter(e -> e.getValue() >= support).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


    }
}










