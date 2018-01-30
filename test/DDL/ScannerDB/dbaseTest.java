package DDL.ScannerDB;

import Parsing.Parsers.LineToParse;
import Parsing.DBPrimitives.DBaseStmt;
import org.junit.Test;

public class dbaseTest {
    static final String errorfile = "error.txt";

    public dbaseTest() {
    }

    public void doit(String input, String outFile) {
        String correctFile = "test/DDL/ScannerDB/Correct/" + outFile + ".txt";
        RegTest.Utility.redirectStdOut(errorfile);
        LineToParse ds = new LineToParse(input,-1);
        System.out.println(input);
        try {
            DBaseStmt d = new DBaseStmt(ds);
            d.parse();
            //ds.parser(DBaseStmt.DBaseStmt);
            System.out.println("parse is successful!");
            for (String s : ds.parseList) {
                System.out.print(s + " ");
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        RegTest.Utility.validate(errorfile, correctFile, false);
    }

    @Test
    public void test1() {
        doit("dbase( abc, [ id,abc,def ] ).", "db1");
    }

    @Test
    public void test2() {
        doit("dbase(abc,[id]).", "db2");
    }

    @Test
    public void test3() {
        doit("dbase (   a,  [id] ).", "db3");
    }

    // Error testing
    @Test
    public void test4() {
        doit("dbas ((abc,[id]).", "db4");
    }

    @Test
    public void test5() {
        doit("dbase((abc,[id]).", "db5");
    }
    
    @Test
    public void test6() {
        doit("dbase(abc,[]).", "db6");
    }
    
    @Test
    public void test7() {
        doit("dbase(abc[id]).", "db7");
    }
    
    @Test
    public void test8() {
        doit("dbase(abc,[id,'abc'])", "db8");
    }
    
    @Test
    public void test9() {
        doit("dbase(abc,[id,])", "db9");
    }
}
