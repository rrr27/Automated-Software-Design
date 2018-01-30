package Yuml.Class;

import org.junit.Test;

public class ParserTest implements YumlClassTestConstants {
    
    public ParserTest() {
    }
    
    void doit(String yumlFileName) {
        Yuml.ClassParser.main(correct+yumlFileName+endYuml, yplFile);
        RegTest.Utility.validate(yplFile, correct+yumlFileName+endYpl, false);
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
    public void standard1() {
        doit("1standard");
    }

    @Test
    public void standard2() {
        doit("2standard");
    }

    @Test
    public void standard3() {
        doit("3standard");
    }

    @Test
    public void notepad() {
        doit("notepad");
    }

    @Test
    public void emptyClass() {
        doit("emptyClass");
    }

    @Test
    public void graff() {
        doit("graff");
    }

    @Test
    public void quark() {
        doit("quark");
    }

    @Test
    public void yumlparser() {
        doit("yumlparser");
    }
}
