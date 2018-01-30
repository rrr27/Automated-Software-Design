package DML.SchemaTests;

import PrologDB.DBSchema;
import RegTest.Utility;
import java.io.File;
import org.junit.Test;

public class SchemaErrorsTest extends CommonTst {
    static final String testdata = "test/DML/SchemaTests/TestData/ERR";
    static final String correct  = "test/DML/SchemaTests/Correct/ERR";
    static final String errorfile = "error.txt";

    public SchemaErrorsTest() {
    }

    protected void genericError(String localSchemaName) {
        String inputFile = testdata + localSchemaName;
        String correctFile = correct + localSchemaName;
        try {
            File schemafile = new File(inputFile);
            Utility.redirectStdErr(errorfile);
            DBSchema.readSchema(schemafile);
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
        System.err.close();
        Utility.validate(errorfile, correctFile, false);
    }

    @Test
    public void test1() {
        genericError("complex.schema.pl");
    }
    
    @Test
    public void test2() {
        genericError("violetdb.schema.pl");
    }
    
    @Test
    public void test3() {
        genericError("inh.schema.pl");
    }
    
    @Test
    public void test4() {
        genericError("hierarchy.schema.pl");
    }
    
    @Test
    public void test5() {
        genericError("hierarchy2.schema.pl");
    }
    
    @Test
    public void test6() {
        genericError("complex2.schema.pl");
    }
    
    @Test
    public void test7() {
        genericError("complex3.schema.pl");
    }
}
