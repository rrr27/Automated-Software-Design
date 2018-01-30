package DML.PrologDBCSV;

import PrologDB.Table;
import java.io.PrintStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVTableTest {
    static final String outpl = "error.pl";
    static final String outcsv = "error.csv";
    static final String testdata = "test/DML/PrologDBCSV/TestData/";
    static final String correct = "test/DML/PrologDBCSV/Correct/";
    static final String[] eliminate = {"true","false","TRUE","FALSE" };

    public CSVTableTest() {
    }

    @Test
    public void getNameTests() {
        String result;
        // TODO review the generated test code and remove the default call to fail.
        result = Table.getCSVName("abc.csv");
        if (!result.equals("abc")) {
            fail("abc.csv test failed");
        }
        result = Table.getCSVName("xy/zw/def.csv");
        if (!result.equals("def")) {
            fail("xy/zw/def.csv test failed");
        }
        result = Table.getCSVName("xy\\zw\\qrs.tuv.csv");
        if (!result.equals("qrs.tuv")) {
            fail("xy\\zw\\qrs.tuv.csv test failed");
        }
    }


    public void doit(String FileName) {
        try {
            PrintStream ps1 = new PrintStream(outpl);
            Table table = Table.readTable(testdata+FileName+".csv");
            table.print(ps1);
            ps1.close();
            RegTest.Utility.validate(outpl, correct+FileName+".pl", false);

            table.writeTable(outcsv);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        RegTest.Utility.validate(outcsv, correct+FileName+".csv", false,eliminate);
    }
    
    @Test
    public void test1() {
        doit("MixinPass6");
    }
    
    @Test
    public void test2() {
        doit("simple");
    }

}
