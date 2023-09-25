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





    }


}



        while((currentLine=in.readLine())!=null){
        //output.write(currentLine+"\n");
        buckets=Arrays.asList(currentLine.split(" "));

        for(String item_1:buckets){

        int index=buckets.indexOf(item_1);

        for(String item_2:buckets.subList(index+1,buckets.size())){
        //skip if 2 items are the same
        if(item_1.equals(item_2)){
        continue;
        }

        //check if each of the items is a frequent item
        if(frequentItems.containsKey(item_1)&&frequentItems.containsKey(item_2)){
        String key=item_1+" "+item_2;
        if(secondPass.containsKey(key)){
        secondPass.put(key,secondPass.get(key)+1);
        }else{
        secondPass.put(key,1);
        }

        }

        }

        }
        }

        Hashtable<String, Integer> frequentPairs=new Hashtable<>();
        secondPass.forEach((k,v)->{
        if(v>=support){
        frequentPairs.put(k,v);
        }
        });

        //output frequent items to file
        frequentItems.forEach((k,v)->{
        try{
        output.write("Item: "+k+" Frequencies: "+v+"\n");
        }catch(Exception e){
        e.printStackTrace();
        }
        });

        frequentPairs.forEach((k,v)->{
        try{
        output.write("Item: "+k+" Frequencies: "+v+"\n");
        }catch(Exception e){
        e.printStackTrace();
        }
        });

        output.close();
        in.close();

        long endTime=System.currentTimeMillis();
        System.out.println("Datasize: "+datasize+" Support threshold: "+support);
        System.out.println("Time elapsed: "+(endTime-startTime));

        }
