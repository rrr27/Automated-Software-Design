package Yuml.ScannerYuml;

import Parsing.Parsers.LineToParse;
import Parsing.GeneralPrimitives.Token;
import Parsing.YumlPrimitives.Box;
import Parsing.YumlPrimitives.BoxEntry;
import org.junit.Test;


public class YumTokenScannerTest {
    static final String errorfile = "error.txt";
    
    public YumTokenScannerTest() {
    }
    
    public void doit(String input, String outFile, Token tkn ) {
        String correctFile = "test/Yuml/ScannerYuml/Correct/" + outFile + ".txt";
        RegTest.Utility.redirectStdOut(errorfile);
        LineToParse l = new LineToParse(input,-1);
        System.out.println(input);
        try {
            Token t =  tkn.klone(l);
            if (t.canParse())
                t.parse();
            for (String s : l.parseList) System.out.print(s+ " ");
            System.out.println();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        RegTest.Utility.validate(errorfile, correctFile, false);
    }

    @Test
    public void t1() {
        doit("'dobee' |", "t1", new BoxEntry());
    }

    @Test
    public void t2() {
        doit("]", "t2", new BoxEntry());
    }
    
    @Test
    public void t3() {
        doit(".'alpha']", "t3", new BoxEntry());
    }   
    
    @Test
    public void t4() {
        doit("[c1]", "t4", new Box());
    }   
    
    @Test
    public void t5() {
        doit("[c1 | 'c2']", "t5", new Box());
    }  
    
    @Test
    public void t6() {
        doit("[c1 | 'c2'|'d7']", "t6", new Box());
    } 
    
    @Test
    public void t7() {
        doit("['don']", "t7", new Box());
    }  
    
    @Test
    public void t8() {
        doit("| 'dowah", "t8", new BoxEntry());
    }
    
    @Test
    public void t9() {
        doit("dowah|", "t9", new BoxEntry());
    }
   
    @Test
    public void t10() {
        doit("['don'|batory]", "t10", new Box());
    } 

    @Test
    public void t11() {
        doit("['don'||batory]", "t11", new Box());
    } 
    
    /* error tests */
    
    @Test
    public void e1() {
        doit(".'alpha'", "e1", new BoxEntry());
    }   
    
    @Test
    public void e4() {
        doit("[]", "e4", new Box());
    }  
    
    @Test
    public void e6() {
        doit("not even close", "e6", new Box());
        // doit succeeds because nothing is parsed.
    }  
}
