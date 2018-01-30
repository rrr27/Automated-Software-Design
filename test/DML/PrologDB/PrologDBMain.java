package DML.PrologDB;

import RegTest.Utility;
import org.junit.Test;

public class PrologDBMain {
    static final String testdata = "test/DML/PrologDB/TestData/";
    static final String correct = "test/DML/PrologDB/Correct/";
    static final String errout = "error.txt";
    static final String stdout = "error2.txt";

    /* this test validates calls to PrologDB.ReadSchema
    */
    public PrologDBMain() {
    }

    @Test
    public void test1() {
        MDL.OO2schema.main(testdata+"starTrek.ooschema.pl");// MDL.OO2schema.main("TestData/PrologDBMain/starTrek.ooschema.pl");
        Utility.validate(testdata+"starTrek.schema.pl", correct+"starTrek.schema.pl", false);
    }

   @Test
    public void test2() {
        Utility.redirectStdErr(errout);
        Utility.redirectStdOut(stdout);
        MDL.ReadSchema.main(testdata+"starTrek.schema.pl");// MDL.ReadSchema.main("TestData/PrologDBMain/starTrek.schema.pl");
        Utility.validate(errout, correct+"empty.txt",true);
        Utility.validate(stdout, correct+ "empty.txt",true);
    }
    
    @Test
    public void test3() {
        Utility.redirectStdErr(errout);
        Utility.redirectStdOut(stdout);
        MDL.ReadDB.main(testdata+"enterprise.starTrek.pl");
        Utility.validate(errout, correct+"empty.txt",true);
        Utility.validate(stdout, correct+"empty.txt",true);
    }

}
