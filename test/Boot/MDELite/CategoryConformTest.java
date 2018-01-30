package Boot.MDELite;

import org.junit.Before;
import org.junit.Test;


public class CategoryConformTest {
    static final String test = "test/Boot/MDELite/Correct/";
    
    public CategoryConformTest() {
    }
    
    @Before
    public void setUp() {
    }

    public void doit(String appName) {
        RegTest.Utility.init();
        RegTest.Utility.redirectStdOut("error.txt");
        CategoryConform.main(test+appName+".fsm.pl");
        RegTest.Utility.validate("error.txt",test+"noErrors.txt",false);
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
