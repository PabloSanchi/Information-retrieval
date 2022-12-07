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
    private Set<String> stopWords = new HashSet<String>();

    public void loadData() throws Exception {
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get("./docLong.json"));
        docLong = gson.fromJson(reader, HashMap.class);

        reader = Files.newBufferedReader(Paths.get("./invIndex.json"));
        Type type = new TypeToken<HashMap<String, Pair<Double, HashMap<String, Double>>>>(){}.getType();
        invIndex = gson.fromJson(reader, type);
    }

    /**
     * @description remove special characters
     *
     * @param data {String}
     * @return String
     */
    public String characterPreprocess(String data) {

        FilterManager fM = new FilterManager();
        fM.addFilter(new FilterAllBraces());
        fM.addFilter(new FilterAllDotsAndCommas());
        fM.addFilter(new FilterAllMarks());
        fM.addFilter(new FilterAtAndHashSign());
        fM.addFilter(new FilterApostrophe());
        fM.addFilter(new FilterAsterisk());
        fM.addFilter(new FilterDashAndSlash());
        fM.addFilter(new FilterNumbers());
        fM.addFilter(new FilterSpaces());

        return fM.execute(data.toLowerCase());
    }

    /**
     *
     * @param terms {ArratList<String>}
     * @return {ArrayList<String>}
     */
    public ArrayList<String> removeStopWords(ArrayList<String> terms) {
        ArrayList<String> vTerm = new ArrayList<String>();

        for(String term : terms) {
            if(!stopWords.contains(term) && !Objects.equals(term, "")) vTerm.add(term);
        }

        return vTerm;
    }

    /**
     * @description initialize and create the stop words set
     */
    public void createStopWordsSet() {
        try {

            File obj = new File("./stopwords.txt");
            Scanner file = new Scanner(obj);

            while (file.hasNextLine()) {
                String line = file.nextLine();
                line = line.replaceAll("'", "");
                stopWords.add(line);
            }
            file.close();
        } catch (Exception e) {}

        System.out.println("Size of stopwords is: " + stopWords.size());
    }

    public ArrayList<String> filterQuery(String query) {
        query = characterPreprocess(query);
        ArrayList<String> vTerm = new ArrayList<String>(Arrays.asList(query.split("\\s")));
        return removeStopWords(vTerm);
    }

    public HashMap<String, Double> getDocs(ArrayList<String> vTerm) {
        HashMap<String, Double> ranking = new HashMap<String, Double>();

        for(String term : invIndex.keySet()) {
            if(vTerm.contains(term)) {
                double idf = invIndex.get(term).getFirst();
                for(String file : invIndex.get(term).getSecond().keySet()) {
                    double num = 0;
                    if(ranking.containsKey(file)) {
                        num = ranking.get(file) + invIndex.get(term).getSecond().get(file) * Math.pow(idf, 2);
                    }else {
                        num = invIndex.get(term).getSecond().get(file) * idf * idf;
                    }
                    ranking.put(file, num);
                }
            }
        }

        for(String file : ranking.keySet()) {
            ranking.put(file, ranking.get(file) / docLong.get(file));
        }

        return ranking;
    }

    public HashMap<String, Double> sortDocs(HashMap<String, Double> ranking) {
        return ranking.entrySet().stream().sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
