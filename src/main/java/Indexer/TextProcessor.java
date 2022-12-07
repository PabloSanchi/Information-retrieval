package Indexer;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import Filters.*;

public class TextProcessor {

    private Set<String> stopWords = new HashSet<String>();

    /**
     * 
     * @param file
     * @return
     * @throws Exception
     */
    public String readText(File file) throws Exception {
        byte[] bytes = Files.readAllBytes(file.toPath());
        return new String (bytes);
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
     * 
     * @param file
     * @return
     * @throws Exception
     */
    public ArrayList<String> preprocessDoc(File file) throws Exception {
        String text = readText(file);
        String preText = characterPreprocess(text);
        ArrayList<String> vTerm = new ArrayList<String>(Arrays.asList(preText.split("\\s")));
        return removeStopWords(vTerm);
    }
}