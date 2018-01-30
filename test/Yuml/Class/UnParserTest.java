package Yuml.Class;

import org.junit.Test;

public class UnParserTest implements YumlClassTestConstants {
    
    public UnParserTest() {
    }

    public void doit(String name) {
        Yuml.ClassUnParser.main(correct + name + endYpl, yumlFile);
        RegTest.Utility.validate(yumlFile, correct + name + endYuml, false);
    }
    
//    @Test   // for debugging test files only
//    public void straight() {
//        doit("straight");
//    }

    @Test
    public void small() {
        doit("small");
    }

    @Test
    public void noLinks() {
        doit("noLinks");
    }

    @Test
    public void wLinks() {
        doit("wLinks");
    }

    @Test
    public void big() {
        doit("big");
    }

    @Test
    public void cool() {
        doit("cool");
    }
  
    @Test
    public void testMain0() {
        doit("emptyClass");
    }

    @Test
    public void testMain1() {
        doit("1standard");
    }

    @Test
    public void testMain2() {
        doit("2standard");
    }

    @Test
    public void testMain3() {
        doit("3standard");
    }

    @Test
    public void testMain40() {
        doit("Notepad");
    }

    @Test
    public void testMain50() {
        doit("graff");
    }

    @Test
    public void testMain60() {
        doit("quark");
    }
}
