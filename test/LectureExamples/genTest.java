package LectureExamples;

import org.junit.Test;

public class genTest {
    static final String correct = "test/LectureExamples/Correct/";
    
    public genTest() {
    }

    @Test
    public void d1() {
        gen.main(correct+"d1.data.pl");
        RegTest.Utility.validate("account.java", correct+"account.java", false);
        RegTest.Utility.validate("city.java", correct+"city.java", false);
    }
    
}
