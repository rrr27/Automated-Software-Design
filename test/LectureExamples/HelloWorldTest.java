package LectureExamples;

import org.junit.Test;

public class HelloWorldTest {
    static final String correct = "test/LectureExamples/Correct/";
    
    public HelloWorldTest() {
    }
   
    @Test
    public void hw() {
        RegTest.Utility.redirectStdOut("out.txt");
        HelloWorld.main("ignore");
        RegTest.Utility.validate("out.txt", correct+"hw.txt", false); 
    }
    
}
