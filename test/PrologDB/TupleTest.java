package PrologDB;

import RegTest.Utility;
import org.junit.*;
import org.junit.Test;
import static org.junit.Assert.*;


public class TupleTest {
    static String correct = "test/PrologDB/Correct/";
    static String outputFile = "out.txt";
    
    static DB db;
    static Table student, course, enroll;
    static Tuple aStudent, aCourse, nonStudent;
    
    public TupleTest() {
    }
    
    @BeforeClass
    static public void setUp() {
        db = DB.readDataBase(correct+"school.sc.pl");
        student = db.getTable("student");
        course = db.getTable("course");
        enroll = db.getTable("enroll"); 
        aStudent = getFirst(student);
        aCourse = getFirst(course);
        nonStudent = new Tuple(student).setValues("s12","Z","22");
    }
    
    @Before
    public void init() {
        Utility.init();
        Utility.redirectStdOut(outputFile);
    }
    
    static private Tuple getFirst(Table tab) {
        for (Tuple t : tab.tuples()) {
            return t;
        }
        return null; // pacify whiney compiler
    }

    @Test
    public void join() {
       aStudent.join("sid", enroll, "sid").print(System.out);
       nonStudent.join("sid", enroll, "sid").print(System.out);
       Utility.validate(outputFile, correct+"tuplejoin.txt", true);
    }
    
    @Test
    public void rsj() {
       aStudent.rightSemiJoin("sid", enroll, "sid").rightSemiJoin("cid",course,"cid").print(System.out);
       nonStudent.rightSemiJoin("sid", enroll, "sid").rightSemiJoin("cid",course,"cid").print(System.out);
       Utility.validate(outputFile, correct+"tuplersj.txt", true);
    }
    
    @Test
    public void jexists() {
       System.out.println(aStudent.joinExists("sid", enroll, "sid"));
       System.out.println(nonStudent.joinExists("sid", enroll, "sid"));
       Utility.validate(outputFile, correct+"jexists.txt", true);
    }
    
    @Test
    public void jfirst() {
       aStudent.joinFirst("sid", enroll, "sid").print(System.out);
       System.out.println("---");
       Tuple t = nonStudent.joinFirst("sid", enroll, "sid");
       if (t==null)
           System.out.println("no join result");
       else
           System.out.format("this tuple is returned -- shouold be null: %s",t);
       System.out.println("---");
       Utility.validate(outputFile, correct+"jfirst.txt", true);
    }
    
    
    @Test
    public void jfirstEH() {
       Tuple t =null;
  
       aStudent.joinFirst("sid", enroll, "sid").print(System.out);
       System.out.println("---");
       try {
           t = nonStudent.joinFirstEH("sid", enroll, "sid");
       } catch (RuntimeException e) {
           System.out.println( e.getMessage());
       }
       if (t!=null)
           System.out.format("this was returned -- should be null : %s",t.toString());
       System.out.println("---");
       Utility.validate(outputFile, correct+"jfirstEH.txt", true);
    }

       
    @Test
    public void in() {
       System.out.println(aStudent.in(student));
       System.out.println(nonStudent.in(student));
       Utility.validate(outputFile, correct+"in.txt", true);
    }
    
       
    @Test
    public void notin() {
       System.out.println(nonStudent.notIn(student));
       System.out.println(aStudent.notIn(student));
       Utility.validate(outputFile, correct+"in.txt", true);
    }
}
