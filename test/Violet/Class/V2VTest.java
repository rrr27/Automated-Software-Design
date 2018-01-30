package Violet.Class;

import RegTest.Utility;
import org.junit.Test;
import static org.junit.Assert.*;

public class V2VTest implements VioletClassTestConstants {
    
    public void vpl2xml2vpl(String fileName) {
        Utility.init();
        String VPL = correct + fileName + endVpl;
        try {
            Violett.ClassUnParser.main(VPL, violetFile);
            Violett.ClassParser.main(violetFile, vplFile);

        } catch (RuntimeException e) {
            fail(e.getMessage());
        }
        RegTest.Utility.validate(vplFile, VPL, false);
    }

   @Test
    public void nolinks() {
        vpl2xml2vpl("noLinks");
    }
    
    @Test
    public void wlinks() {
        vpl2xml2vpl("wLinks");
    }
   
    @Test
    public void violet() {
        vpl2xml2vpl("violet");
    }

    @Test
    public void test1() {
        vpl2xml2vpl("test1");
    }
    
    @Test
    public void c1() {
        vpl2xml2vpl("c1");
    }
    
    @Test
    public void c2() {
        vpl2xml2vpl("c2");
    }
    
    @Test
    public void c3() {
        vpl2xml2vpl("c3");
    }
    
    @Test
    public void c4() {
        vpl2xml2vpl("c4");
    }
    
    @Test
    public void c5() {
        vpl2xml2vpl("c5");
    }
    
    @Test
    public void int1() {
        vpl2xml2vpl("int1");
    }
    
    @Test
    public void int2() {
        vpl2xml2vpl("int2");
    }
    
    @Test
    public void int3() {
        vpl2xml2vpl("int3");
    }
    
    @Test
    public void assoc() {
        vpl2xml2vpl("assoc");
    }
    
    @Test
    public void OneAssoc() {
        vpl2xml2vpl("1Assoc");
    }
}
