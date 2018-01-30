package Violet.Class;

import RegTest.Utility;
import static Violet.Class.VioletClassTestConstants.correct;
import static Violet.Class.VioletClassTestConstants.endVpl;
import org.junit.Test;

public class ClassConformTest implements VioletClassTestConstants {
    
    public ClassConformTest() {
    }
    
    public void doit(String fileName, boolean noErrors) {
        String VPLcorrect;
        Utility.init();
        Utility.redirectStdOut(conformFile);
        String VPL = correct + fileName + endVpl;
        
        VPLcorrect = noErrors?(correct+"noErrors.txt"):(correct + fileName+".txt");
        try {
            Violett.ClassConform.main(VPL,conformFile);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        RegTest.Utility.validate(conformFile, VPLcorrect, false);
    }

   @Test
    public void nolinks() {
        doit("noLinks",true);
    }
    
    @Test
    public void wlinks() {
        doit("wLinks",true);
    }
   
    @Test
    public void violet() {
        doit("violet",true);
    }

    @Test
    public void test1() {
        doit("test1",true);
    }
    
    @Test
    public void c1() {
        doit("c1",true);
    }
    
    @Test
    public void c2() {
        doit("c2",true);
    }
    
    @Test
    public void c3() {
        doit("c3",true);
    }
    
    @Test
    public void c4() {
        doit("c4",true);
    }
    
    @Test
    public void c5() {
        doit("c5",true);
    }
    
    @Test
    public void int1() {
        doit("int1",true);
    }
    
    @Test
    public void int2() {
        doit("int2",true);
    }
    
    @Test
    public void int3() {
        doit("int3",true);
    }
    
    @Test
    public void assoc() {
        doit("assoc",false);
    }
    
    @Test
    public void OneAssoc() {
        doit("1Assoc",false);
    }
    
    @Test
    public void errors() {
        doit("errors",false);
    }
    
}
