package LectureExamples;

import org.junit.Before;
import org.junit.Test;

public class DogConstraintsTest {
    static final String correct = "test/LectureExamples/Correct/";
    static final String outputFile = "out.txt";

    
    public DogConstraintsTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void onlyTest() {
        RegTest.Utility.init();
        RegTest.Utility.redirectStdOut(outputFile);
        DogConstraints.main("dummy");
        RegTest.Utility.validate(outputFile, correct+"DogConstraints.txt", false);
    }
    
}
