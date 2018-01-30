package DDL.ScannerDB;

import Parsing.Parsers.LineToParse;
import Parsing.DBPrimitives.TableStmt;
import org.junit.Test;

public class tableTest {
    static final String errorfile = "error.txt";

    public tableTest() {
    }

    public void doit(String input, String outFile) {
        String correctFile = "test/DDL/ScannerDB/Correct/" + outFile + ".txt";
        RegTest.Utility.redirectStdOut(errorfile);
        LineToParse ds = new LineToParse(input,-1);
        System.out.println(input);
        try {
            TableStmt ts = new TableStmt(ds);
            ts.parse();
            
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
        doit("table( abc, [ id,\"abc\",def ] ).", "test1");
    }
    
    @Test
    public void test1a() {
        doit("table( abc, [ 'id','abc','def' ] ).", "test1a");
    }

    @Test
    public void test2() {
        doit("table(abc,[id,\"abc\",def]).", "test2");
    }

    @Test
    public void test3() {
        doit("table (   a,  [id] ).", "test3");
    }

    @Test
    public void test4() {
        doit("dbas ((abc,[id]).", "test4");
    }

    @Test
    public void test5() {
        doit("table((abc,[id]).", "test5");
    }
    
    @Test
    public void test6() {
        doit("table(abc,[]).", "test6");
    }
    
    @Test
    public void test7() {
        doit("table(abc[id]).", "test7");
    }
    
    @Test
    public void test8() {
        doit("table(abc,[id])", "test8");
    }
}
