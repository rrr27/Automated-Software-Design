package DML.PrologDB;

import MDL.ReadDB;
import RegTest.Utility;
import org.junit.Test;

public class ReadDBTest {
    static final String testdata = "test/DML/PrologDB/TestData/";
    static final String correct = "test/DML/PrologDB/Correct/";
    public static final String errorFile = "error.txt";

    public ReadDBTest() {
    }

    public void doit(String name) {
        Utility.init();
        Utility.redirectStdOut(errorFile);
        String db = testdata + name + ".pl"; // String db = "TestData/DB/" + name + ".pl";
        try {
            ReadDB.main(db);
        } catch (Exception e) {
            System.out.print(e.getMessage());
            Utility.validate(errorFile, correct + name + "ERR.txt", false); // Utility.validate(errorFile, "Correct/DB/" + name + "ERR.txt", false);
        }
    }

    @Test
    public void c() {
        doit("c.complex");
    }

    @Test
    public void dogOwner() {
        doit("dogOwner.do");
    }

    @Test
    public void enterprise() {
        doit("enterprise.starTrek");
    }

    @Test
    public void nolinks() {
        doit("noLinks.violetdb");
    }

    @Test
    public void bad1() {
        doit("bad1.starTrek");
    }

    @Test
    public void bad2() {
        doit("bad2.starTrek");
    }

    @Test
    public void bad3() {
        doit("bad3.starTrek");
    }

    @Test
    public void bad4() {
        doit("bad4.starTrek");
    }

    @Test
    public void bad5() {
        doit("bad5.starTrek");
    }
}
