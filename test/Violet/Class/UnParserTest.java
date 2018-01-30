package Violet.Class;

import org.junit.Test;

public class UnParserTest implements VioletClassTestConstants {
    
    public UnParserTest() {
    }
    
    public void work(String file) {
        Violett.ClassUnParser.main(correct+file+endVpl,violetFile);
        RegTest.Utility.validate(violetFile, correct+file+endViolet, false);
    }

    @Test
    public void nolinks() {
        work("nolinks");
    }
    
    @Test
    public void int1() {
        work("int1");
    }
    
    @Test
    public void int2() {
        work("int2");
    }
    
    @Test
    public void int3() {
        work("int3");
    }
    
    @Test
    public void c1() {
        work("c1");
    }
    
    @Test
    public void c2() {
        work("c2");
    }
    
    @Test
    public void c3() {
        work("c3");
    }
    
    @Test
    public void c4() {
        work("c4");
    }
    
    @Test
    public void c5() {
        work("c5");
    }
    
    @Test
    public void OneAssoc() {
        work("1Assoc");
    }
    
    @Test
    public void assoc() {
        work("assoc");
    }
    
}
