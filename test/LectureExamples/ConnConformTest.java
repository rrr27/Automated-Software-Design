package LectureExamples;

import org.junit.Test;

public class ConnConformTest {
    static final String correct = "test/LectureExamples/Correct/";
    
    public ConnConformTest() {
    }

    @Test
    public void exconn() {
        RegTest.Utility.redirectStdOut("out.txt");
        RegTest.Utility.redirectStdErr("err.txt");
        ConnConform.main(correct+"ex.con.pl");
        RegTest.Utility.validate("out.txt", correct+"ex.out.txt", false);
        RegTest.Utility.validate("err.txt", correct+"ex.err.txt", false);
    }
    
    @Test
    public void exconnWnegation() {
        RegTest.Utility.redirectStdOut("out.txt");
        RegTest.Utility.redirectStdErr("err.txt");
        ConnConformWNegation.main(correct+"ex.con.pl");
        RegTest.Utility.validate("out.txt", correct+"ex.out.txt", false);
        RegTest.Utility.validate("err.txt", correct+"ex.err.txt", false);
    }
}
