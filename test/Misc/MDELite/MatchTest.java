package Misc.MDELite;

import CorrectResultsAfterChainingErrorMsgs.UpdateCorrectFiles;
import org.junit.Test;

public class MatchTest {
    
    boolean same(String[] a, String[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i].equals(b[i])) {
                continue;
            }
            return false;
        }
        return true;
    }

    public MatchTest() {
    }

    String[] oldpat1 = "This is a GREAT saying".split(" ");
    String[] toks1 = "This is a foobar/longdale saying".split(" ");
    String result1[] = new String[]{"foobar/longdale"};
    String result2[] = new String[]{"great"};
    String[] toks2 = "This is a great saying".split(" ");
    String[] toks3 = "Is not a great saying".split(" ");

    @Test
    public void oneArgInMiddleTest1() {
        String[] result = UpdateCorrectFiles.match(oldpat1, toks1);
        assert same(result, result1);
    }
    
    @Test
    public void oneArgInMiddleTest2() {
        String[] result = UpdateCorrectFiles.match(oldpat1, toks2);
        assert same(result, result2);
    }
    
    @Test
    public void oneArgInMiddleTest3() {
        String[] result = UpdateCorrectFiles.match(oldpat1, toks3);
        assert result == null;
    }

    String[] oldpat10 = "This is a GREAT and BETTER saying".split(" ");
    String[] toks10 = "This is a foobar/longdale and longdale. saying".split(" ");
    String result10[] = new String[]{"foobar/longdale", "longdale."};
    String result20[] = new String[]{"great", "don-like"};
    String[] toks20 = "This is a great and don-like saying".split(" ");
    String[] toks30 = "Is not a great saying".split(" ");

    @Test
    public void twoArgsInMiddleTest0() {
        String[] result = UpdateCorrectFiles.match(oldpat10, toks10);
        assert same(result, result10);
    }

    @Test
    public void twoArgsInMiddleTest1() {
        String[] result = UpdateCorrectFiles.match(oldpat10, toks20);
        assert same(result, result20);
    }

    @Test
    public void twoArgsInMiddleTest2() {
        String[] result = UpdateCorrectFiles.match(oldpat10, toks30);
        assert result == null;
    }

    String[] oldpat11 = "GREAT is a BETTER saying".split(" ");
    String[] toks11 = "This is a foobar/longdale. saying".split(" ");
    String result11[] = new String[]{"This", "foobar/longdale."};
    
    String[] toks13 = "This is a great saying".split(" ");
    String result13[] = new String[]{"This", "great"};

    String[] toks15 = "Is not a great saying".split(" ");

    @Test
    public void twoArgsOneFirst11() {
        String[] result = UpdateCorrectFiles.match(oldpat11, toks11);
        assert same(result, result11);
    }
    @Test
    public void twoArgsOneFirst13() {
        String[]result = UpdateCorrectFiles.match(oldpat11, toks13);
        assert same(result, result13);
    }
    
    @Test
    public void twoArgsOneFirst23() {
        String[]result = UpdateCorrectFiles.match(oldpat11, toks15);
        assert result == null;
    }

    String[] oldpat12 = "Here is my MESSAGE".split(" ");
    String[] toks12 = "Here is my long but sweet message".split(" ");
    String result12[] = new String[]{"long but sweet message"};
    
    String result22[] = new String[]{"great saying"};
    String[] toks22 = "Here is my great saying".split(" ");
    
    String[] toks24 = "Is not a great saying".split(" ");
    
    String[] toks25 = "Here is my long but sweet MESSAGE".split(" ");
    String result25[] = new String[] { "long but sweet MESSAGE" };
            

    @Test
    public void oneAtEnd12() {
        String[] result = UpdateCorrectFiles.match(oldpat12, toks12);
        assert same(result, result12);
    }
    
    @Test
    public void oneAtEnd22() {
        String[]result = UpdateCorrectFiles.match(oldpat12, toks22);
        assert same(result, result22);
    }
    
    @Test
    public void oneAtEnd23() {
        String[]result = UpdateCorrectFiles.match(oldpat12, toks24);
        assert result == null;
    }
    
    @Test
    public void oneAtEnd24() {
        String[] result = UpdateCorrectFiles.match(oldpat12, toks25);
        assert same(result, result25);
    }
    
    String[] oldpat62 = "THIS is my MESSAGE".split(" ");
    String[] toks62 = "Here is my long but sweet message".split(" ");
    String result62[] = new String[]{"Here", "long but sweet message"};
    
    String[] toks64 = "Yoda is my great saying".split(" ");
    String result64[] = new String[]{"Yoda","great saying"};  
    
    String[] toks65 = "Is not a great saying".split(" ");
    
    String[] toks67 = "Alpha/beta. is my long but sweet MESSAGE".split(" ");
    String result67[] = new String[] { "Alpha/beta.","long but sweet MESSAGE" };
            

    @Test
    public void bothEnds62() {
        String[] result = UpdateCorrectFiles.match(oldpat62, toks62);
        assert same(result, result62);
    }
    
    @Test
    public void bothEnds64() {
        String[]result = UpdateCorrectFiles.match(oldpat62, toks64);
        assert same(result, result64);
    }
    
    @Test
    public void bothEnds65() {
        String[]result = UpdateCorrectFiles.match(oldpat62, toks65);
        assert result == null;
    }
    
    @Test
    public void bothEnds67() {
        String[] result = UpdateCorrectFiles.match(oldpat62, toks67);
        assert same(result, result67);
    }
}
