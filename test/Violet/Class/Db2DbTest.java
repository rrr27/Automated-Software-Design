package Violet.Class;

import RegTest.Utility;
import org.junit.Test;
import static org.junit.Assert.*;

public class Db2DbTest implements VioletClassTestConstants {
    
    public void xml2vpl2xml(String fileName) {
        Utility.init();
        String XML = correct + fileName + endViolet;
        try {
            Violett.ClassParser.main(XML, vplFile);
            Violett.ClassUnParser.main(vplFile, violetFile);

        } catch (RuntimeException e) {
            fail(e.getMessage());
        }
        RegTest.Utility.validate(violetFile, XML, false);
    }

   @Test
    public void nolinks() {
        xml2vpl2xml("noLinks");
    }
    
    @Test
    public void wlinks() {
        xml2vpl2xml("wLinks");
    }
   
    @Test
    public void violet() {
        xml2vpl2xml("violet");
    }

    @Test
    public void test1() {
        xml2vpl2xml("test1");
    }
    
    @Test
    public void c1() {
        xml2vpl2xml("c1");
    }
    
    @Test
    public void c2() {
        xml2vpl2xml("c2");
    }
    
    @Test
    public void c3() {
        xml2vpl2xml("c3");
    }
    
    @Test
    public void c4() {
        xml2vpl2xml("c4");
    }
    
    @Test
    public void c5() {
        xml2vpl2xml("c5");
    }
    
    @Test
    public void int1() {
        xml2vpl2xml("int1");
    }
    
    @Test
    public void int2() {
        xml2vpl2xml("int2");
    }
    
    @Test
    public void int3() {
        xml2vpl2xml("int3");
    }
    
    @Test
    public void assoc() {
        xml2vpl2xml("assoc");
    }
    
    @Test
    public void OneAssoc() {
        xml2vpl2xml("1Assoc");
    }
}
