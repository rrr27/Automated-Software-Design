package Boot.MDELite;

import org.junit.Test;


public class catalinaTest {
    static final String Correct = "test/Boot/MDELite/Correct/";
    
    public catalinaTest() {
    }

    public void doitRight(String filename) {
        Boot.MDELite.catalina.main("all",Correct+filename+".state.violet");
        RegTest.Utility.validate(filename+".fsm.pl", Correct+filename+".fsm.pl", false);
        RegTest.Utility.validate(filename+".meta.pl", Correct+filename+".meta.pl", false);
        RegTest.Utility.validate(filename+".java", Correct+filename+".jav", false);
    }
    
    @Test
    public void bootstrap() {
        doitRight("catalina");
    }
    
    @Test
    public void fsm() {
        doitRight("fsm");
    }
    
    @Test
    public void alleg() {
        doitRight("alleg");
    }
}
