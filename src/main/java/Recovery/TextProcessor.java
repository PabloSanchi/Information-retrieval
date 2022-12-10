package Recovery;

import Filters.*;

import java.io.File;
import java.util.*;

public class TextProcessor {

    private Set<String> stopWords = new HashSet<String>();

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
    }

    public ArrayList<String> filterQuery(String query) {
        query = characterPreprocess(query);
        ArrayList<String> vTerm = new ArrayList<String>(Arrays.asList(query.split("\\s")));
        return removeStopWords(vTerm);
    }
}
