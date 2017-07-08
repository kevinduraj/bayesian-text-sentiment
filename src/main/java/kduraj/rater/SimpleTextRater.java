package kduraj.rater;

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

public class SimpleTextRater implements TextRater {

    private Set<String> negs;
    private Set<String> pos;
    private Set<String> stops;
    private Map<String, Integer> bow;

    public SimpleTextRater(File text, File stopWordsFile, File negativeWordsFile, File positiveWordsFile)
            throws IOException {
        String blurbOfText = FileUtils.readFileToString(text);
        blurbOfText = blurbOfText.replaceAll("U\\.S\\.", "US");
        String[] words = blurbOfText.split("[^A-Za-z0-9]");
        String stopWordsBlob = FileUtils.readFileToString(stopWordsFile);
        String[] stopWords = stopWordsBlob.split(", ");
        stops = new HashSet<String>(Arrays.asList(stopWords));

        @SuppressWarnings("unchecked")
        List<String> negWordsBlob = FileUtils.readLines(negativeWordsFile);
        negs = new HashSet<String>(negWordsBlob);

        @SuppressWarnings("unchecked")
        List<String> posWordsBlob = FileUtils.readLines(positiveWordsFile);
        pos = new HashSet<String>(posWordsBlob);

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

    public boolean rate() {

        int p = 0;
        int n = 0;
        for (Map.Entry<String, Integer> e : bow.entrySet()) {
            if (pos.contains(e.getKey()))
                p += e.getValue();

            if (negs.contains(e.getKey()))
                n += e.getValue();
        }
        return p > n;
    }
}
