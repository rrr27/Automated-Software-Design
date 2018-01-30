package Yuml.ScannerYuml;

import Parsing.Parsers.LineToParse;
import Parsing.GeneralPrimitives.Token;
import Parsing.YumlPrimitives.YumlLine;
import org.junit.Test;

public class YumlLineScanner2Test {
    static final String errorfile = "error.txt";
    
    public YumlLineScanner2Test() {
    }
    
    public void doit(String input, String outFile ) {
        String correctFile = "test/Yuml/ScannerYuml/Correct/" + outFile + ".txt";
        RegTest.Utility.redirectStdOut(errorfile);
        LineToParse l = new LineToParse(input,-1);
        System.out.println(input);
        try {
            Token t =  new YumlLine(l);
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
    public void c1() {
        doit("[class0]", "c1");
    }
    
    @Test
    public void c2() {
        doit("[class0 | 'klass1']", "c2");
    }
    
    @Test
    public void c3() {
        doit("[class0  |  'klass1'   |   'j4']", "c3");
    }
    
    @Test
    public void c3A() {
        doit("[class0  |  | 'klass1' ]", "c3a");
    }
    
    @Test
    public void c4() {
        doit("[class0]-[class1]", "c4");
    }
    
    @Test
    public void c5() {
        doit("[class0]don-batory[class1]", "c5");
    }
    
    @Test
    public void c6() {
        doit("[class0]++-++[class1]", "c6");
    }
    
    @Test
    public void c7() {
        doit("[class0]^-^[class1]", "c7");
    }
    
    @Test
    public void c8() {
        doit("[class0]<>-<>[class1]", "c8");
    }
    
    @Test
    public void c9() {
        doit("[class0]<->[class1]", "c9");
    }
    
    @Test
    public void c10() {
        doit("[class0|'ab;cd;ef]", "c10");
    }
    
    @Test
    public void c11() {
        doit("[class0|'ab;cd;ef'|4]", "c11");
    }
    
    @Test
    public void c12() {
        doit("[c0]-.-[c1]", "c12");
    }
    
     /* errors */
    
    @Test
    public void error4() {
        doit("[class0]>-[class1]", "error4");
    }
    
    @Test
    public void error5() {
        doit("[class0]-<[class1]", "error5");
    }
    
    @Test
    public void error6() {
        doit("[class0]>-[class1]", "error6");
    }
    
    @Test
    public void error7() {
        doit("[aclass]b;c-a>[bclass]", "error7");
    }
    
}
