package Boot.MDELite;

import org.junit.Test;

public class MetaConformTest {

    public static final String Correct = "test/Boot/MDELite/Correct/";
    public static final String outputFile = "normal.txt";
    public static final String errorFile = "error.txt";

    public MetaConformTest() {
    }

    void doit(String filename) {
        RegTest.Utility.redirectStdOut(outputFile);
        RegTest.Utility.redirectStdErr(errorFile);
        try {
            MetaConform.main(Correct + filename + ".meta.pl");
            RegTest.Utility.validate(outputFile, Correct + "noErrors.txt", false);
        } catch (Exception e) {
            RegTest.Utility.done();
            RegTest.Utility.validate(errorFile, Correct + filename + ".txt", false);
        }
    }

    @Test
    public void bad1() {
        doit("bad1");
    }

    @Test
    public void bad2() {
        doit("bad2");
    }

    @Test
    public void bad3() {
        doit("bad3");
    }

    @Test
    public void bad4() {
        doit("bad4");
    }
}
