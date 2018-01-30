package Misc.MDELite;

import java.io.File;
import java.util.LinkedList;
import static org.junit.Assert.*;


public class UpdateCorrectFiles {

    public UpdateCorrectFiles() {
    }

    String[] ans = {
        "test.ClassYumlParser.Correct",
        "test.ClassYumlUnParser.Correct",
        "test.MDELite.Correct",
        "test.MDL.Correct",
        "test.PrologDB.Correct",
        "test.PrologDBCSV.Correct",
        "test.RunningBear.Correct",
        "test.Scanner.Correct",
        "test.SchemaTests.Correct",
        "test.Utils.Correct",
        "test.VioletClassParser.Correct",
        "test.VioletClassUnParser.Correct",
        "test.VioletRoundTrip.Correct",};

    public void findCorDirs() {
        LinkedList<File> l = CorrectResultsAfterChainingErrorMsgs.UpdateCorrectFiles.findCorrectDirectories();
        for (File f : l) {
            String n = f.getAbsolutePath();
            int i = n.lastIndexOf("test");
            n = n.substring(i);
            n = n.replaceAll("/", ".");
            n = n.replace('\\', '.');
            boolean match = false;
            for (String s : ans) {
                if (n.equals(s)) {
                    match = true;
                    break;
                }   
            }
            if (!match) {
                        fail("The test case is a prototype.");
            }
        }
        assert(true);
    }
}
