package kduraj.rater;

import static java.lang.Math.log;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class BayesianTextRater implements TextRater {

    private Map<String, Integer> negs;
    private Map<String, Integer> pos;
    private int posCount;
    private int negCount;
    private Set<String> stops;
    private Map<String, Integer> bow;

    public BayesianTextRater(File text, File stopWordsFile, File negativeWordsFile, File positiveWordsFile)
            throws IOException {

        stops = readCommaSeparatedWord(stopWordsFile);

        @SuppressWarnings("unchecked")
        List<String> negativeWords = FileUtils.readLines(negativeWordsFile);
        negCount = negativeWords.size();
        negs = readBow(negativeWords);

        @SuppressWarnings("unchecked")
        List<String> positiveWords = FileUtils.readLines(positiveWordsFile);
        posCount = positiveWords.size();
        pos = readBow(positiveWords);

        String blurbOfText = FileUtils.readFileToString(text);
        blurbOfText = blurbOfText.replaceAll("U\\.S\\.", "US");
        String[] words = blurbOfText.split("[^A-Za-z0-9]");
        bow = new HashMap<String, Integer>();
        for (String w : words) {
            if (StringUtils.isBlank(w))
                continue;

            if (stops.contains(w))
                continue;

            if (bow.containsKey(w))
                bow.put(w, bow.get(w) + 1);
            else
                bow.put(w, 1);
        }
    }

    private Set<String> readCommaSeparatedWord(File stopWordsFile) throws IOException {
        String stopWordsBlob = FileUtils.readFileToString(stopWordsFile);
        String[] stopWords = stopWordsBlob.split(", ");
        return new HashSet<String>(Arrays.asList(stopWords));
    }

    private static Map<String, Integer> readBow(List<String> words) throws IOException {
        HashMap<String, Integer> bow = new HashMap<String, Integer>();
        for (String aWord : words) {
            if (bow.containsKey(aWord))
                bow.put(aWord, bow.get(aWord) + 1);
            else
                bow.put(aWord, 1);
        }
        return bow;
    }

    @Override
    public boolean rate() {

        long p = 0;
        long n = 0;

        for (Map.Entry<String, Integer> e : bow.entrySet()) {
            double pi = e.getValue() * log(ratePositive(e.getKey()));
            p += pi;
            double ni = e.getValue() * log(rateNegative(e.getKey()));
            n += ni;
            System.out.println(pi + " : " + ni);
        }

        System.out.println(p + " : " + n);
        return p > n;
    }

    private double ratePositive(String e) {
        return (pOfDGivenPositive(e) * pPositive()) / pOfD(e);
    }

    private double rateNegative(String e) {
        return (pOfDGivenNegative(e) * (1.0 - pPositive())) / pOfD(e);
    }

    private double pPositive() {
        return (posCount + 1.0) / (posCount + negCount + 2.0);
    }

    private double pOfD(String e) {

        double numerator = (pos.containsKey(e) ? pos.get(e) : 0.0) + (negs.containsKey(e) ? negs.get(e) : 0.0) + 1.0;
        double denominator = posCount + negCount + pos.size() + negs.size();
        return numerator / denominator;
    }

    /**
     * <pre>
     * P(word | POSITIVE) = CountPositive(word) + 1 / NumberOfPositiveWords + NumberOfPositiveClasses
     * </pre>
     * 
     * NumberOfPositiveClasses a.k.a. number of distinct positive words
     * 
     * @param e
     * @return
     */
    private double pOfDGivenPositive(String e) {
        return ((pos.containsKey(e) ? pos.get(e) : 0.0) + 1.0d) / (posCount + pos.size());
    }

    /**
     * <pre>
     * P(word | NEGATIVE) = CountNegative(word) + 1 / NumberOfNegativeWords + NumberOfNegativeClasses
     * </pre>
     * 
     * NumberOfNegativeClasses a.k.a. number of distinct positive words
     * 
     * @param e
     * @return
     */
    private double pOfDGivenNegative(String e) {
        return ((negs.containsKey(e) ? negs.get(e) : 0.0) + 1.0d) / (negCount + negs.size());
    }

}
