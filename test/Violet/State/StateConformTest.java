package Violet.State;

import RegTest.Utility;
import org.junit.Test;


public class StateConformTest implements VioletStateTestConstants {
    
    public StateConformTest() {
    }

    public void doit(String fileName, boolean noErrors) {
        Utility.init();
        Utility.redirectStdOut(conformFile);
        String VPL = correct + fileName + endFsm;
        
        String VPLcorrect = noErrors?(correct+"noErrors.txt"):(correct + fileName+".txt");
        try {
            Violett.StateConform.main(VPL,conformFile);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        RegTest.Utility.validate(conformFile, VPLcorrect, false);
    }
    
    @Test
    public void test1() {
        doit("FSM",false);
    }
    
    @Test
    public void test2() {
        doit("DonsEatingHabits",true);
    }
    
    @Test
    public void test3() {
        doit("nodes",false);
    }
    
    @Test
    public void test4() {
        doit("edges",false);
    }
}
