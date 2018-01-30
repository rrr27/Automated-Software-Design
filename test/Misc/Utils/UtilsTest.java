package Misc.Utils;

import MDELite.Utils;
import org.junit.Test;

public class UtilsTest {
    static final String errorfile = "error.txt";

    public UtilsTest() {
    }

    void printit(String[] result) {
        int j = 0;
        for (String i : result) {
            System.out.println(j++ + " " +i);
        }
        System.out.println();
    }

    @Test
    public void printer() {
        // TODO review the generated test code and remove the default call to fail.
        RegTest.Utility.redirectStdOut(errorfile);
        printit(Utils.parseFileName("ABC.DEF.HIJ"));
        printit(Utils.parseFileName("/abc/def.hi.jk"));
        printit(Utils.parseFileName("\\abc\\def\\hi.jk\\l\\m\\pl"));
        RegTest.Utility.validate(errorfile, "test/Misc/Utils/Correct/printer.txt", false);
        
    }
    
    @Test
    public void endswith() {
        // TODO review the generated test code and remove the default call to fail.
        assert(Utils.endsWith(Utils.parseFileName("ABC.DEF.HIJ"),"DEF","HIJ"));
        assert(Utils.endsWith(Utils.parseFileName("ABC.DEF.HIJ"),"HIJ"));
        assert(!Utils.endsWith(Utils.parseFileName("ABC.DEF.HIJ"),"DEF"));
        
        assert(Utils.endsWith(Utils.parseFileName("/abc/def.hi.jk"),"hi","jk"));
        assert(Utils.endsWith(Utils.parseFileName("/abc/def.hi.jk"),"jk"));
        assert(!Utils.endsWith(Utils.parseFileName("/abc/def.hi.jk"),"dk"));
        
        assert(Utils.endsWith(Utils.parseFileName("\\abc\\def\\hi.jk\\l\\m\\pl"),"m","pl"));
        assert(Utils.endsWith(Utils.parseFileName("\\abc\\def\\hi.jk\\l\\m\\pl"),"pl"));
        assert(!Utils.endsWith(Utils.parseFileName("\\abc\\def\\hi.jk\\l\\m\\pl"),"m"));
    }
}
