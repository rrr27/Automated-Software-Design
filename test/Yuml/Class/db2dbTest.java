package Yuml.Class;

import org.junit.Test;

public class db2dbTest implements YumlClassTestConstants {
    
    public db2dbTest() {
    }
    
    void doit(String yumlFileName) {
        String startingFile = correct+yumlFileName+endYpl;
        Yuml.ClassUnParser.main(startingFile, yumlFile);
        Yuml.ClassParser.main(yumlFile, yplFile);
        RegTest.Utility.validate(yplFile, startingFile, false);
    }

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
