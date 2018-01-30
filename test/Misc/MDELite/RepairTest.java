package Misc.MDELite;

import CorrectResultsAfterChainingErrorMsgs.UpdateCorrectFiles;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.*;
import java.util.HashMap;
import org.junit.BeforeClass;
import org.junit.Test;


public class RepairTest {
    HashMap<String,String> newMsg;
    HashMap<String,String[]> oldMsg;
    
    @BeforeClass
   public static void beforeClass() {
      File directory = new File("trash");
      if(!directory.exists()){
          directory.mkdir();
      }
   }
    
    void init1(String stopAt) {
      newMsg = new HashMap<>();
      oldMsg = new HashMap<>();
      
      if (stopAt.equals("one"))
          return;
      newMsg.put("one", "This is FIRST message");
      oldMsg.put("one", "My FIRSTMSG message".split(" "));
      newMsg.put("oneLINE", "at line XX This is FIRST message");
      oldMsg.put("oneLINE", "at line XX My FIRSTMSG message".split(" "));
      
      if (stopAt.equals("two"))
          return;
      newMsg.put("two", "This has ONEMSG and TWO messages");
      oldMsg.put("two", "ONE + TWO are my answers".split(" "));
      newMsg.put("twoLINE", "at line XX This has ONEMSG and TWO messages");
      oldMsg.put("twoLINE", "at line XX ONE + TWO are my answers".split(" "));
              
      if (stopAt.equals("three"))
          return;   
      newMsg.put("three","ONE and TWO are what I have to say!");
      oldMsg.put("three","My answers are ONE and TWO".split(" "));
      newMsg.put("threeLINE","at line XX ONE and TWO are what I have to say!");
      oldMsg.put("threeLINE","at line XX My answers are ONE and TWO".split(" "));
      
      if (stopAt.equals("four"))
          return;   
      newMsg.put("four", "Frodo LIVES is king of the RINGS");
      oldMsg.put("four", "LORD of the RINGS".split(" "));
      newMsg.put("fourLINE", "at line XX Frodo LIVES is king of the RINGS");
      oldMsg.put("fourLINE", "at line XX LORD of the RINGS".split(" "));
      
}
   
    public RepairTest() {
    }

    public void doit(String stopAt, String data) throws IOException {
        init1(stopAt);
        File src = new File("test/Misc/MDELite/TestData/"+data+".txt");
        String fname = "trash/"+data+stopAt+".txt";
        File dst = new File(fname);
        Files.copy(src.toPath(), dst.toPath(),REPLACE_EXISTING);
        UpdateCorrectFiles.repair(dst, newMsg, oldMsg);
        RegTest.Utility.validate(fname, "./test/Misc/MDELite/Correct/"+data+stopAt+".txt", false);
    }
    
    @Test
    public void Testone() throws IOException {
        doit("one","data1");
    }
    
    @Test
    public void Testtwo() throws IOException {
        doit("two","data1");
    }
    
    @Test
    public void Testthree() throws IOException {
        doit("three","data1");
    }
    
    @Test
    public void Testfour() throws IOException {
        doit("four","data1");
    }
    
    @Test
    public void Testfive() throws IOException {
        doit("five","data1");
    }
    
    @Test
    public void TestoneB() throws IOException {
        doit("one","data2");
    }
    
    @Test
    public void TesttwoB() throws IOException {
        doit("two","data2");
    }
    
    @Test
    public void TestthreeB() throws IOException {
        doit("three","data2");
    }
    
    @Test
    public void TestfourB() throws IOException {
        doit("four","data2");
    }
    
    @Test
    public void TestfiveB() throws IOException {
        doit("five","data2");
    }
    
    @Test
    public void TestoneC() throws IOException {
        doit("one","data3");
    }
    
    @Test
    public void TesttwoC() throws IOException {
        doit("two","data3");
    }
    
    @Test
    public void TestthreeC() throws IOException {
        doit("three","data3");
    }
    
    @Test
    public void TestfourC() throws IOException {
        doit("four","data3");
    }
    
    @Test
    public void TestfiveC() throws IOException {
        doit("five","data3");
    }
}
