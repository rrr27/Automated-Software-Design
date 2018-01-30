package LectureExamples;

import org.junit.Test;


public class DogStreamTest {
     static final String correct = "test/LectureExamples/Correct/";
    
    public DogStreamTest() {
    }

    @Test
    public void ds() {
        RegTest.Utility.redirectStdOut("out.txt");
        DogStream.main(correct+"dogOwner.do.pl");
        RegTest.Utility.validate("out.txt", correct+"dogStream.txt", false);
    }
    
}
