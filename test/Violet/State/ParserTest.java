package Violet.State;

import org.junit.Test;

public class ParserTest implements VioletStateTestConstants {

    
    public ParserTest() {
    }
    
    public void doit(String filePrefix) {
        String fname = correct + filePrefix + endViolet;
        String correctName = correct + filePrefix + endFsm;
        Violett.StateParser.main(fname, fsmFile);
        RegTest.Utility.validate(fsmFile, correctName, false);
    }

    @Test
    public void test1() {
        doit("FSM");
    }
    
    @Test
    public void test2() {
        doit("DonsEatingHabits");
    }
    
    @Test
    public void test3() {
        doit("nodes");
    }
    
    @Test
    public void test4() {
        doit("edges");
    }
}
