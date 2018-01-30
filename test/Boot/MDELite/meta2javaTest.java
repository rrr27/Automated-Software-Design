package Boot.MDELite;

import org.junit.Test;

public class meta2javaTest {
    static final String correct = "test/Boot/MDELite/Correct/";
    static final String errorFile = "error.txt";
    static final String normalFile = "normal.java";
    
    public meta2javaTest() {
    }

    private void doit(String filename) {
        RegTest.Utility.redirectStdErr(errorFile);
        try {
            meta2java.main(correct+filename+".meta.pl", normalFile);
            RegTest.Utility.validate(normalFile, correct+filename+".jav", false);
            return;
        } catch (Exception e) {
            System.out.format("---> failure in meta2Java test %s\n",filename);
            throw e;
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
