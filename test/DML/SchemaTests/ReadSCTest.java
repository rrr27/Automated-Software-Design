package DML.SchemaTests;

import org.junit.Test;
import RegTest.*;
import MDL.ReadSchema;
import PrologDB.DBSchema;

public class ReadSCTest {
    static final String testdata = "test/DML/SchemaTests/TestData/";
    static final String correct  = "test/DML/SchemaTests/Correct/";
    static final String errorfile = "error.txt";

    public ReadSCTest() {
    }
    
    public void doit(String filename) {
        try {
            Utility.init();
            Utility.redirectStdErr(errorfile);
            ReadSchema.main(testdata + filename + ".pl"); 
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
        System.err.close();
        Utility.validate(errorfile, correct + filename + ".txt", false); 
    }

    @Test
    public void test1() {
        doit("complex.schema");
    }

    @Test
    public void test2() {
        doit("complex.ooschema");
    }
    
    @Test
    public void test3() {
        doit("bad.ooschema");
    }
    
    @Test
    public void test4() {
        doit("nada.ooschema");
    }
    
    @Test
    public void test5() {
        doit("nonsense.ooschema");
    }
    
    @Test
    public void school() {
        Utility.init();
        Utility.redirectStdOut(errorfile);
        DBSchema dbs = DBSchema.readSchema(testdata+"school.schema.pl");
        dbs.print(System.out);
        Utility.validate(errorfile, correct + "school.schema.txt", false); 
    }
}
