package Boot.MDELite;

import Boot.MDELite.CategoryConform;
import Boot.MDELite.MetaConform;
import Boot.MDELite.fsm2meta;
import Boot.MDELite.meta2java;
import org.junit.Test;

public class ToolTest {

    public static final String Correct = "test/BootMDELite/Correct/";
    public static final String stateViolet = ".state.violet";
    public static final String fsmpl = ".fsm.pl";
    public static final String metapl= ".meta.pl";
    public static final String java = ".java";
    public static final String jav = ".jav";

    public ToolTest() {
    }

    void doit(String filename) {
        String task = "";
        try {
            task =  "StateParser";
            Violett.StateParser.main(Correct+filename + stateViolet);
            RegTest.Utility.validate(filename+fsmpl, Correct+filename+fsmpl, false);
            task = "CategoryConform";
            CategoryConform.main(filename+fsmpl);
            task = "fsm2meta";
            fsm2meta.main(filename+fsmpl);
            RegTest.Utility.validate(filename+metapl, Correct+filename+metapl, false);
            task = "metaconform";
            MetaConform.main(filename+metapl);
            task = "meta2java";
            meta2java.main(filename+metapl);
            RegTest.Utility.validate(filename+java, Correct+filename+jav, false);
        } catch (Exception e) {
            System.out.println(task);
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void fsm() {
        doit("fsm");
    }
    
    @Test
    public void catalina() {
        doit("catalina");
    }
    
    @Test
    public void alleg() {
        doit("alleg");
    }
    
}
