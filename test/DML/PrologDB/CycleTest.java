package DML.PrologDB;

import PrologDB.Constraints;
import PrologDB.ErrorReport;
import PrologDB.Table;
import org.junit.Test;

public class CycleTest extends CommonTst {

    public CycleTest() {
    }

    public void doit(String file) {
        String input = testdata + file + ".trans.pl";
        String cor = correct + file + ".txt";
        Table t = Table.readTable(input);
        RegTest.Utility.redirectStdOut(errorfile);
        ErrorReport er = new ErrorReport();
        Constraints.cycleCheck(t, er);
        try {
            er.printReportEH(System.out);
        } catch (Exception e) {
        }
        System.out.println();
        RegTest.Utility.validate(errorfile, cor, false);
    }

    @Test
    public void d1() {
        doit("d1");
    }

    @Test
    public void d2() {
        doit("d2");
    }
    
    @Test
    public void d3() {
        doit("d3");
    }
}
