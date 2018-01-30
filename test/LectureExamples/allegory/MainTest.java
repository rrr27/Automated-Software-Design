package LectureExamples.allegory;

import org.junit.Before;
import org.junit.Test;


public class MainTest {

    public MainTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void PDD() {
        RegTest.Utility.redirectStdOut("out.txt");
        try {
            Main.main("test/LectureExamples/Correct/x.PDD.pl");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        RegTest.Utility.validate("out.txt", "test/LectureExamples/Correct/PDD.txt", false);
    }

}
