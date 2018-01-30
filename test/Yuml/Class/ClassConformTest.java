package Yuml.Class;

import RegTest.Utility;
import org.junit.Test;

public class ClassConformTest implements YumlClassTestConstants {
    
    public ClassConformTest() {
    }
    
    public void doit(String fileName, boolean noErrors) {
        String YumlCorrect;
        Utility.init();
        Utility.redirectStdOut(conformFile);
        String yuml = correct + fileName + endYpl;
        
        YumlCorrect = noErrors?(correct+"noErrors.txt"):(correct + fileName+".txt");
        try {
            Yuml.ClassConform.main(yuml,conformFile);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        RegTest.Utility.validate(conformFile, YumlCorrect, false);
    }
 

    @Test
    public void cycles() {
        doit("cycles",false);
    }  
    
    @Test
    public void errors() {
        doit("errors",false);
    }  

    @Test
    public void small() {
        doit("small",true);
    }
    
    @Test
    public void noLinks() {
        doit("noLinks",true);
    }
    
    @Test
    public void wLinks() {
        doit("wLinks",true);
    }
    
    @Test
    public void big() {
        doit("big",true);
    }
    
    @Test
    public void cool() {
        doit("cool",true);
    }
    
    @Test
    public void testMain0() {
        doit("emptyClass",true);
    }

    @Test
    public void testMain1() {
        doit("1standard",true);
    }

    @Test
    public void testMain2() {
        doit("2standard",true);
    }

    @Test
    public void testMain3() {
        doit("3standard",true);
    }

    @Test
    public void testMain40() {
        doit("Notepad",true);
    }

    @Test
    public void testMain50() {
        doit("graff",true);
    }

    @Test
    public void testMain60() {
        doit("quark",true);
    }
    
}
