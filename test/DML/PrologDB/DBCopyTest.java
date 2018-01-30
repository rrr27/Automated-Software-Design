package DML.PrologDB;

import PrologDB.DB;
import RegTest.Utility;
import java.io.PrintStream;
import org.junit.Test;

public class DBCopyTest {
    static final String testdata = "test/DML/PrologDB/TestData/";
    static final String correct = "test/DML/PrologDB/Correct/";
    static final String errorfile = "error.txt";

    public DBCopyTest() {
    }
    
    void genericCopyDBTest(String localFileName) {
        // localfilename <dbname>.<schemaname>.pl
        DB result = null;
        try {
            result = DB.readDataBase(testdata + localFileName);
            String newName = result.getName() + "C";
            result.rename(newName);
            String fullName = result.getFullName();
            result.print(new PrintStream(errorfile));
            Utility.validate(errorfile, correct + fullName, false);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            org.junit.Assert.fail();
        }
    }

    @Test
    public void test1() {
        genericCopyDBTest("noLinks.violetdb.pl");
    }

    @Test
    public void test2() {
        genericCopyDBTest("school.violetdb.pl");
    }
    
    @Test
    public void test3() {
        genericCopyDBTest("violet.violetdb.pl");
    }
    
    @Test
    public void test4() {
        genericCopyDBTest("wLinks.violetdb.pl");
    }
    
   @Test
    public void test5() {
        genericCopyDBTest("i.inh.pl");
    }
    
    @Test
    public void test6() {
        genericCopyDBTest("c.complex.pl");
    }
    
    @Test
    public void test7() {
        genericCopyDBTest("h.hierarchy.pl");
    }
}
