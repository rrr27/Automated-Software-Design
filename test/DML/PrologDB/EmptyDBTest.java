package DML.PrologDB;

import PrologDB.DB;
import PrologDB.DBSchema;
import RegTest.Utility;
import java.io.File;
import java.io.PrintStream;
import org.junit.Test;


public class EmptyDBTest {
    static final String correct = "test/DML/PrologDB/Correct/";
    static final String errorfile = "error.txt";

    /* this test copies a database (really its schema)
       and then creates an empty database of this schema
    */
    public EmptyDBTest() {
    }
    
    void genericSchema2EmptyDB(String localFileName) {
        // localfilename <dbname>.<schemaname>.pl

        try {
            File schemaFile = new File("test/DML/SchemaTests/TestData/" + localFileName); 
            DBSchema dbs = DBSchema.readSchema(schemaFile);
            DB dbe = new DB(dbs.getName() + "E", dbs);
            String fullName = dbe.getFullName();
            dbe.print(new PrintStream(errorfile));
            Utility.validate(errorfile, correct + fullName, false);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            org.junit.Assert.fail();
        }
    }

    @Test
    public void test1() {
        genericSchema2EmptyDB("violetdb.schema.pl");
    }

   @Test
    public void test2() {
        genericSchema2EmptyDB("inh.schema.pl");
    }
    
    @Test
    public void test3() {
        genericSchema2EmptyDB("complex.schema.pl");
    }
    
    @Test
    public void test4() {
        genericSchema2EmptyDB("hierarchy.schema.pl");
    }
}
