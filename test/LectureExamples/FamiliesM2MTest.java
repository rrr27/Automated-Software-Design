package LectureExamples;

import org.junit.Test;


public class FamiliesM2MTest {
    static final String correct = "test/LectureExamples/Correct/";
    static final String inria = "inria.persons.pl";
    
    public FamiliesM2MTest() {
    }

    @Test
    public void families() {
        RegTest.Utility.redirectStdOut("out.txt");
        FamiliesM2M.main(correct+"inria.families.pl", "out.txt");
        RegTest.Utility.validate("out.txt", correct+inria, false);
    }
    
}
