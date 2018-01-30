package Violet.Class;


import RegTest.Utility;
import org.junit.Test;

public class ParserTest implements VioletClassTestConstants {

    public ParserTest() {
    }

    public void work(String fileName) {
        String inputXML = correct + fileName + endViolet;
        Violett.ClassParser.main(inputXML, vplFile); // always works, as Violet always produces a sensible file to itself
        Utility.validate(vplFile, correct + fileName + endVpl, false);
    }

    @Test
    public void PrettyBad() {
        work("PrettyBad");
    }
    
    @Test
    public void nolinks() {
        work("noLinks");
    }
    
    @Test
    public void wlinks() {
        work("wLinks");
    }
   
    @Test
    public void violet() {
        work("violet");
    }

    @Test
    public void test1() {
        work("test1");
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
    public void assoc() {
        work("assoc");
    }
    
    @Test
    public void OneAssoc() {
        work("1Assoc");
    }
}
