/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Violet.State;

import static Violet.State.VioletStateTestConstants.correct;
import static Violet.State.VioletStateTestConstants.endFsm;
import static Violet.State.VioletStateTestConstants.endViolet;
import static Violet.State.VioletStateTestConstants.fsmFile;
import org.junit.Test;

/**
 *
 * @author don
 */
public class UnParserTest implements VioletStateTestConstants{
    
    public UnParserTest() { 
    }
    
    public void doit(String filePrefix) {
        String fname = correct + filePrefix + endFsm;
        String correctName = correct + filePrefix + endViolet;
        Violett.StateUnParser.main(fname, violetFile);
        RegTest.Utility.validate(violetFile, correctName, false);
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
