package Indexer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import Pair.Pair;

public class Calc {
    public HashMap<String, Pair<Double, HashMap<String, Double>>> invIndex = new HashMap<String, Pair<Double, HashMap<String, Double>>>();
    public HashMap<String, Double> docLong = new HashMap<String, Double>();

    public HashMap<String, Integer> calcTFStep1(ArrayList<String> terms) {
        HashMap<String, Integer> frec = new HashMap<String, Integer>();
        for(String word : terms) {
            if(!frec.containsKey(word)) frec.put(word, 1); // word = 1
            else frec.put(word, frec.get(word)+1); // word++
        }
        return frec;
    }

    public void
    calcTFStep2(HashMap<String, Integer> TF, String doc) {

        HashMap<String, Double> docIdWeight;

        for(Map.Entry<String, Integer> el : TF.entrySet()) {
            double tf  = 1 + Math.log(el.getValue())/Math.log(2);

            if(invIndex.containsKey(el.getKey())) {
                docIdWeight = invIndex.get(el.getKey()).getSecond();
            }else docIdWeight = new HashMap<String, Double>();

            docIdWeight.put(doc, tf);
            invIndex.put(el.getKey(), new Pair<Double, HashMap<String, Double>>(0.0, docIdWeight));
        }
    }

    public void addIDF(int N) {
        for(Map.Entry<String, Pair<Double, HashMap<String, Double>>> el : invIndex.entrySet()) {
            int ni = el.getValue().getSecond().size();
            double idf = (Math.log((1.0 * N)/ni) / Math.log(2));
            el.getValue().setFirst(idf);
        }
    }

    public void docLongitude() {

        double idf_tf = 0;
        for (String term : invIndex.keySet()) {
            for (String doc : invIndex.get(term).getSecond().keySet()) {
                double idf = invIndex.get(term).getFirst();
                double tf = invIndex.get(term).getSecond().get(doc);
                idf_tf = Math.pow(idf * tf, 2);
                if (docLong.containsKey(doc)) {
                    idf_tf += docLong.get(doc);
                }
                docLong.put(doc, idf_tf);
            }
        }
        // add the square_root
        for(String doc : docLong.keySet()) {
            docLong.put(doc, Math.sqrt(docLong.get(doc)));
        }
    }
}
