package kduraj.rater;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class BayesianTextRaterTest {

    @Test
    public void test() throws IOException {
        File f = new File("src/test/resources/blurb_of_text (1) (3).txt");
        File sf = new File("src/test/resources/stop_words (1) (3).txt");
        File nf = new File("src/test/resources/negative_sentiment_words (1) (3).txt");
        File pf = new File("src/test/resources/positive_sentiment_words (2) (3).txt");

        TextRater tr = new BayesianTextRater(f, sf, nf, pf);
        System.out.println(tr.rate() ? "positive" : "negative");
    }

}
