package DDL.ScannerDB;

import Parsing.Parsers.LineToParse;
import Parsing.DBPrimitives.SubTableStmt;
import org.junit.Test;

public class subTableTest {
    static final String errorfile = "error.txt";

    public subTableTest() {
    }

    public void doit(String input, String outFile) {
        String correctFile = "test/DDL/ScannerDB/Correct/" + outFile + ".txt";
        RegTest.Utility.redirectStdOut(errorfile);
        LineToParse ds = new LineToParse(input,-1);
        System.out.println(input);
        try {
           SubTableStmt st = new SubTableStmt(ds);
           st.parse();
           
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
        doit("subtable( abc, [ id,abc,def ] ).", "st1");
    }

    @Test
    public void test2() {
        doit("subtable(abc,[id]).", "st2");
    }

    @Test
    public void test3() {
        doit("subtable (   a,  [id] ).", "st3");
    }

    // Error testing
    @Test
    public void test4() {
        doit("sub ((abc,[id]).", "st4");
    }

    @Test
    public void test5() {
        doit("subtable((abc,[id]).", "st5");
    }
    
    @Test
    public void test6() {
        doit("subtable(abc,[]).", "st6");
    }
    
    @Test
    public void test7() {
        doit("subtable(abc[id]).", "st7");
    }
    
    @Test
    public void test8() {
        doit("subtable(abc,[id,'abc'])", "st8");
    }
    
    @Test
    public void test9() {
        doit("subtable(abc,[id,])", "st9");
    }
}
