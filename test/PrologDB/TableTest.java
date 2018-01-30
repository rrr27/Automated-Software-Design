package PrologDB;

import RegTest.Utility;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.*;

public class TableTest {
    static String correct = "test/PrologDB/Correct/";
    static String outputFile = "out.txt";
    
    static DB db;
    static Table student, course, enroll;
    
    public TableTest() {
    }
    
    @BeforeClass
    static public void setUp() {
        db = DB.readDataBase(correct+"school.sc.pl");
        student = db.getTable("student");
        course = db.getTable("course");
        enroll = db.getTable("enroll"); 
    }
    
    @Before
    public void init() {
        Utility.init();
        Utility.redirectStdOut(outputFile);
    }

    @Test
    public void foreachtest() {
        student.forEach(t->t.print(System.out));
        Utility.validate(outputFile, correct+"foreach.txt", false);
    }
    
    @Test
    public void anymatch() {
        System.out.println(student.anyMatch(t->t.is("name","F")));
        System.out.println(student.anyMatch(t->t.is("name","R")));
        Utility.validate(outputFile, correct+"anyMatch.txt", false);
    }
    
    @Test
    public void allmatch() {
        System.out.println(student.allMatch(t->{ int v = t.getInt("age"); return 10<v && v<20; }));
        System.out.println(student.allMatch(t->{ int v = t.getInt("age"); return !(10<v && v<20); }));
        Utility.validate(outputFile, correct+"allMatch.txt", false);
    }
    
    @Test
    public void filter() {
        course.filter(c->c.is("prof","xp")).print(System.out);
        Utility.validate(outputFile, correct+"filter.txt", false);
    }
    
    @Test
    public void exists() {
        System.out.println(course.exists(c->c.is("prof","yp")));
        System.out.println(course.exists(c->c.is("prof","qp")));
        System.out.println(course.exists("prof","yp"));
        System.out.println(course.exists("prof","qp"));
        Utility.validate(outputFile, correct+"exists.txt", false);
    }
    
    @Test
    public void antiproject() {
        enroll.antiProject("sid","cid").print(System.out);
        enroll.antiProject("eid").print(System.out);
        Utility.validate(outputFile, correct+"antiproject.txt", false);
    }
    
    @Test
    public void leftOuterJoin() {
        student.leftOuterJoin("sid", enroll, "sid").print(System.out);
        Utility.validate(outputFile, correct+"leftOuterJoin.txt", false);
    }
    
    @Test
    public void semijoin() {
        student.semiJoin("sid", enroll, "sid").print(System.out);
        Utility.validate(outputFile, correct+"semiJoin.txt", false);
    }
    
    @Test
    public void rightSemiJoin() {
        student.rightSemiJoin("sid", enroll, "sid").print(System.out);
        course.rightSemiJoin("cid", enroll, "cid").print(System.out);
        Utility.validate(outputFile, correct+"rightSemiJoin.txt", false);
    }
    
    @Test
    public void antiSemiJoin() {
        student.antiSemiJoin("sid", enroll, "sid").print(System.out);
        course.antiSemiJoin("cid", enroll, "cid").print(System.out);
        Utility.validate(outputFile, correct+"antiSemiJoin.txt", false);
    }
    
    @Test
    public void equal() {
        Table shortStudent = student.filter(s->s.is("name","D"));
        System.out.println(student.equals(student));
        System.out.println(shortStudent.equals(student));
        Utility.validate(outputFile, correct+"equals.txt", false);
    }
    
    @Test
    public void intrsct() {
        Table shortStudent = student.filter(s->s.is("name","D"));
        Table i = shortStudent.intersect(student);
        String subset = (i.count()==shortStudent.count())?"is subset of":"overlaps with";
        System.out.format("shortStudent %s student\n",subset);
        i.print(System.out);
        Utility.validate(outputFile,correct+"intersect.txt",false);
    }

    @Test
    public void containsNone() {
        initAny();
        String result = ((aStudent.intersect(student)).count()==0)?" no":" ";
        System.out.format("aStudent has%s tuples in common with student\n",result);
        Utility.validate(outputFile,correct+"containsNone.txt",false);
    }
    private Table aStudent;
    
    private void initAny() {
        aStudent = new Table(student.getSchema());
        Tuple stud = new Tuple(aStudent);
        stud.setValues("e12","s1","c1");
        aStudent.add(stud);
    }
   
    @Test
    public void sort() {
        student.sort("age",false).print(System.out);
        Utility.validate(outputFile, correct+"sorted.txt", false);
    }
}
