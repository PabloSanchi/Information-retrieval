package Recovery;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting Recovery Process");
        long start = System.nanoTime();

        Scanner sc = new Scanner(System.in);
        Calc calc = new Calc();
        TextProcessor tp = new TextProcessor();
        calc.loadData();
        tp.createStopWordsSet();

        while(true) {
            System.out.println("Enter query (exit to abandon): ");
            String query = sc.nextLine();
            if(Objects.equals(query, "exit")) break;

            // apply filters to query
            ArrayList<String> vTerms = tp.filterQuery(query);

            HashMap<String, Double> ranking = new HashMap<String, Double>();
            ranking = calc.getDocs(vTerms);
            ranking = calc.sortDocs(ranking);

            int elements = 10;

            for(String file : ranking.keySet()) {
                if(elements-- >= 0) {
                    System.out.println("Document ID:" + file + " (weight: " + ranking.get(file) + ")");
                    File f = new File("./corpus/" + file);
                    String content = new String(Files.readAllBytes(Paths.get(f.getPath())));
                    System.out.println("Summary: " + content.substring(0, Math.min(content.length(), 200)) + "...\n");
                }
            }
        }

        long finish = System.nanoTime();
        long elapsedTime = finish - start;
        double elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000;
        System.out.println("Recovery Process Finished");
        System.out.println("Elapsed time: " + elapsedTimeInSecond + " seconds");
    }
}