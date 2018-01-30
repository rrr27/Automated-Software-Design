package Misc.MDL;

import org.junit.Test;

public class OO2schemaTest {
    static final String errorfile = "error.txt";
    static final String testdata = "test/Misc/MDL/TestData/";
    static final String correct = "test/Misc/MDL/Correct/";
    
    public OO2schemaTest() {
    }

    @Test
    public void test1() {
        MDL.OO2schema.main(testdata+"figure.ooschema.pl");
        RegTest.Utility.validate(testdata+"figure.schema.pl", correct + "figure.schema.pl", false);
    }  
}
