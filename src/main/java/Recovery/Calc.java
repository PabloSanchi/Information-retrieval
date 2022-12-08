package Recovery;

import java.io.File;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import Filters.*;
import Pair.Pair;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javax.sound.midi.Soundbank;

public class Calc {

    public HashMap<String, Pair<Double, HashMap<String, Double>>> invIndex = new HashMap<String, Pair<Double, HashMap<String, Double>>>();
    public HashMap<String, Double> docLong = new HashMap<String, Double>();

    public void loadData() throws Exception {
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get("./docLong.json"));
        docLong = gson.fromJson(reader, HashMap.class);

        reader = Files.newBufferedReader(Paths.get("./invIndex.json"));
        Type type = new TypeToken<HashMap<String, Pair<Double, HashMap<String, Double>>>>(){}.getType();
        invIndex = gson.fromJson(reader, type);
    }

    public HashMap<String, Double> getDocs(ArrayList<String> vTerm) {
        HashMap<String, Double> ranking = new HashMap<String, Double>();

        for(Map.Entry<String, Pair<Double, HashMap<String, Double>>> entry : invIndex.entrySet()) {
            String term = entry.getKey();
            if(vTerm.contains(term)) {
                Pair<Double, HashMap<String, Double>> pair = entry.getValue();
                double idf = pair.getFirst();

                for(Map.Entry<String, Double> docEntry : pair.getSecond().entrySet() ) {
                    String file = docEntry.getKey();
                    double temp;

                    if(ranking.containsKey(file))
                        temp = ranking.get(file) + invIndex.get(term).getSecond().get(file) * Math.pow(idf, 2);
                    else
                        temp = invIndex.get(term).getSecond().get(file) * idf * idf;

                    ranking.put(file, temp);
                }
            }
        }

        for(String file : ranking.keySet()) {
            ranking.put(file, ranking.get(file) / docLong.get(file));
        }

        return ranking;
    }

    public HashMap<String, Double> sortDocs(HashMap<String, Double> ranking) {
        return ranking.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

    }
}
