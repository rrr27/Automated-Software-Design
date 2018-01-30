package LectureExamples;

import org.junit.Test;


public class OwnersOfMostDogsTest {
    static final String correct = "test/LectureExamples/Correct/";
    
    public OwnersOfMostDogsTest() {
    }

    @Test
    public void mostDogsOwned() {
        RegTest.Utility.redirectStdOut("out.txt");
        OwnersOfMostDogs.main(correct+"dogOwner.do.pl");
        RegTest.Utility.validate("out.txt", correct+"mostDogOwner.txt", false);
    }
    
}
