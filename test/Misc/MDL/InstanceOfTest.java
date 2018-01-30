package Misc.MDL;

import org.junit.Test;

public class InstanceOfTest {
    static final String errorfile = "error.txt";
    static final String testdata = "test/Misc/MDL/TestData/";
    static final String correct = "test/Misc/MDL/Correct/";

    public InstanceOfTest() {
    }

    @Test
    public void conn() {
        try {
            RegTest.Utility.redirectStdErr(errorfile);
            MDL.InstanceOf.main(testdata + "conn.schema.pl", testdata + "ex.conn.pl");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        System.err.close();
        RegTest.Utility.validate(errorfile, correct + "conn.txt", false);
    }

    @Test
    public void pets() {
        RegTest.Utility.redirectStdErr(errorfile);
        MDL.InstanceOf.main(testdata + "petdb.schema.pl", testdata + "pets.petdb.pl");
        RegTest.Utility.validate(errorfile, correct + "pets.txt", false);
    }
    
    @Test
    public void violetdb() {
        RegTest.Utility.redirectStdErr(errorfile);
        MDL.InstanceOf.main(testdata + "violetdb.schema.pl", testdata + "wLinks.violetdb.pl");
        RegTest.Utility.validate(errorfile, correct + "violetdb.txt", false);
    }
    
    @Test
    public void badconn() {
        RegTest.Utility.redirectStdErr(errorfile);
        MDL.InstanceOf.main(testdata+"conn.schema.pl", testdata+"bad.conn.pl");
        System.err.close();
        RegTest.Utility.validate(errorfile, correct+"badconn.txt", false);
    }

    @Test
    public void bad1() {
        try {
            RegTest.Utility.redirectStdErr(errorfile);
            MDL.InstanceOf.main(testdata+"violetdb.schema.pl", testdata+"bad.violetdb.pl");
            RegTest.Utility.validate(errorfile, correct+"bad1.txt", false);
        } catch (Exception e) {
            System.err.print(e.getMessage());
            System.err.flush();
            System.err.close();
            RegTest.Utility.validate(errorfile, correct+"bad1.txt", false);
        }
    }

    @Test
    public void bad2() {
        try {
            RegTest.Utility.redirectStdErr(errorfile);
            MDL.InstanceOf.main(testdata + "bad.schema.pl", testdata + "wLinks.bad.pl");
            RegTest.Utility.validate(errorfile, correct + "bad1.txt", false);
            return;
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
        System.err.close();
        RegTest.Utility.validate(errorfile, correct + "bad2.txt", false);
    }
}
