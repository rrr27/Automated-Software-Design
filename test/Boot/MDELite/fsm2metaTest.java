package Boot.MDELite;

import static org.junit.Assert.fail;
import org.junit.Test;

public class fsm2metaTest {

    public static String errorFile = "error2.txt";
    public static final String Correct = "test/Boot/MDELite/Correct/";

    public fsm2metaTest() {
    }

    public void doit(String filename) {
        RegTest.Utility.redirectStdOut(errorFile);
        try {
            fsm2meta.main(Correct + filename + ".fsm.pl", errorFile);
            RegTest.Utility.validate(errorFile, Correct + filename+".meta.pl", false);
            return;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            fail();
        }
    }
    
    @Test
    public void catalina() {
        doit("catalina");
    }
    
    @Test
    public void fsm() {
        doit("fsm");
    }
    
    @Test
    public void alleg() {
        doit("alleg");
    }
}
