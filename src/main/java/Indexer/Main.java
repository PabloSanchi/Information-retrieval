package Indexer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void printToFile(Calc calc) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            PrintWriter file = new PrintWriter(new FileWriter("./invIndex.json"));
            String json = gson.toJson(calc.invIndex);
            file.write(json);
            file.close();

            file = new PrintWriter(new FileWriter("./docLong.json"));
            json = gson.toJson(calc.docLong);
            file.write(json);
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        long start = System.nanoTime();

        TextProcessor tp = new TextProcessor();
        File corpus = new File("./corpus");
        File[] filePaths = corpus.listFiles();

        System.out.println("Starting Indexing Process");

        if(filePaths == null) {
            System.out.println("Cannot find dir"); return;
        }else System.out.println("Files: " + filePaths.length);

        int numOfFiles = filePaths.length, i = 1;
        ArrayList<String> vTerm;
        Calc calc = new Calc();
        HashMap<String, Double> docLong;

        tp.createStopWordsSet();
        for (File file : filePaths) {
            if (file.isFile()) {
                if(i++ % 1000 == 0) System.out.println(i + " / " + numOfFiles);
                vTerm = tp.preprocessDoc(file);
                HashMap<String, Integer> termsFrec = calc.calcTFStep1(vTerm);
                calc.calcTFStep2(termsFrec, file.getName());
            }
        }

        calc.addIDF(numOfFiles);
        calc.docLongitude();

        printToFile(calc);

        long finish = System.nanoTime();
        long elapsedTime = finish - start;
        double elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000;
        System.out.println("Indexing Process Finished");
        System.out.println("Elapsed time: " + elapsedTimeInSecond + " seconds");
    }
}