package RunningBear;

import org.junit.Test;

public class RunningBearTest {
    static final String testdata = "test/RunningBear/TestData/";
    static final String correct = "test/RunningBear/Correct/";
    static final String errorfile = "error.txt";
    
    public RunningBearTest() {
    }

    @Test
    public void helloworld() throws Exception {
        helloworld.main(testdata+"noLinks.nopos.pl",errorfile); // supply dummy database file
        RegTest.Utility.validate(errorfile, correct+"test10.txt", false);
    }
    
    @Test
    public void sdpnopos2dot() throws Exception {
        sdpnopos2dot.main(testdata+"noLinks.nopos.pl",errorfile);
        RegTest.Utility.validate(errorfile, correct+"test11.txt", false);
    }
    
    @Test
    public void dump() throws Exception {
        dump.main(testdata+"noLinks.nopos.pl",errorfile);
        RegTest.Utility.validate(errorfile, correct+"test2.txt", false);
    }
    
    @Test
    public void demo() throws Exception {
        demo.main(testdata+"x1.classes.pl",errorfile);
        RegTest.Utility.validate(errorfile, correct+"x1.txt", false);
    }
}
