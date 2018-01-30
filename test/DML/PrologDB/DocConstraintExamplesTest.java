package DML.PrologDB;

import java.util.HashMap;
import java.util.function.Predicate;
import org.junit.Test;
///Check
import PrologDB.*;

public class DocConstraintExamplesTest extends PrologDB.Constraints {

    public static void main(String... args) {
        // Step 1: standard command-line processing
        MDELite.Marquee4Conform mark = new MDELite.Marquee4Conform(DocConstraintExamplesTest.class, ".sc.pl", args);
        String inputFileName = mark.getInputFileName();
        String AppName = mark.getAppName(inputFileName);

        // Step 2: open database to be validated + get needed tables
        DB db = DB.readDataBase(inputFileName);
        Table course = db.getTableEH("course");
        Table student = db.getTableEH("student");
        Table takes = db.getTableEH("takes");
        ErrorReport er = new ErrorReport();

        // Step 3: now perform database checks
        //...
        // Step 4: finish by reporting collected errors
        er.printReportEH(System.out);
    }
///Check

    static final String testdata = "test/DML/PrologDB/TestData/";
    static final String correct = "test/DML/PrologDB/Correct/";
    static final String errorfile = "error.txt";

    public DocConstraintExamplesTest() {
    }

    ////@Test
    public void nonNullFieldTest() {
        RegTest.Utility.redirectStdOut(errorfile);
        try {
            NonNull();
        } catch (Exception e) {
        }
        RegTest.Utility.validate(errorfile, correct + "nonnull.txt", true);
    }

    void NonNull() {
        DB db = DB.readDataBase(testdata + "/StudentCourse.sc.pl");
        Table takes = db.getTableEH("takes");
        ErrorReport er = new ErrorReport();
///NonNull
        // Step 1: try MDELite built-in support
        Predicate<Tuple> cidIsNull = t -> t.isNull("cid");
        iftest(takes, cidIsNull, "cid is null", er);

        Predicate<Tuple> sidIsNull = t -> t.isNull("sid");
        iftest(takes, sidIsNull, "sid is null", er);

        // Step 2: or Manual way
        testNull(takes, "cid", er);
        testNull(takes, "sid", er);

        // finish
        er.printReportEH(System.out);
    }

    void testNull(Table tab, String fieldName, ErrorReport er) {
        tab.filter(t -> t.isNull(fieldName))
                .forEach(t -> er.add("%s(%s...) has null %s field",
                tab.getName(), t.get("tid"), fieldName));
    }
///NonNull

    //@Test
    public void existenceTest() {
        RegTest.Utility.redirectStdOut(errorfile);
        try {
            Existence();
        } catch (Exception e) {
        }
        RegTest.Utility.validate(errorfile, correct + "existence.txt", true);
    }

    void Existence() {
        DB db = DB.readDataBase(testdata + "/StudentCourse.sc.pl");
        Table takes = db.getTableEH("takes");
        Table course = db.getTableEH("course");
        ErrorReport er = new ErrorReport();
///Existence
        // Step 1: Easy way: MDELite built-in support
        isLegit(takes, "cid", course, "cid", er);

        // Step 2: Manual: one way -- a tuple at a time
        takes.filter(t -> (!course.exists("cid", t.get("cid"))))
                .forEach(t -> {
                    er.add("takes(%s,..) has invalid cid value (%s)",
                            t.get("tid"), t.get("cid"));
                });

        // Step 3: Manual: another way -- compute a table of offending tuples
        takes.antiSemiJoin("cid", course, "cid")
                .forEach(t -> er.add("takes(%s,..) has invalid cid value (%s)",
                t.get("tid"), t.get("cid")));
///Existence
        // finish
        try {
            er.printReportEH(System.out);
        } catch (Exception ex) {
        }

    }

    //@Test
    public void uniqueIDTest() {
        RegTest.Utility.redirectStdOut(errorfile);
        try {
            UniqueID();
        } catch (Exception e) {
        }
        RegTest.Utility.validate(errorfile, correct + "unique.txt", false);
    }

    void UniqueID() {
        DB db = DB.readDataBase(testdata + "/StudentCourse.sc.pl");
        Table course = db.getTableEH("course");
        ErrorReport er = new ErrorReport();
///UniqueID
        // Step 1: simplest way uses MDELite built-in support
        UniqueId(course, er);
        
        // Step 2: a more general way -- that works for any column
        Table dups = course.project(course.getIDName()).duplicates();
        //dups.print(System.out);
        dups.error(er,"course table has multiple tuples with id='%s'",t->t.getId());

        // Step 3: a more verbose way
        Unique u = new Unique(course, "cid", er);
        course.forEach(t -> u.add(t));
///UniqueID
        // finish
        er.printReportEH(System.out);
    }

    @Test
    public void uniqueNameTest() {
        RegTest.Utility.redirectStdOut(errorfile);
        try {
            UniqueName();
        } catch (Exception e) {
        }
        RegTest.Utility.validate(errorfile, correct + "uniqueName.txt", false);
    }

    void UniqueName() {
        DB db = DB.readDataBase(testdata + "/StudentCourse.sc.pl");
        Table student = db.getTableEH("student");
        ErrorReport er = new ErrorReport();
///UniqueName
        // Step 1: try Manual way
        Unique u = new Unique(student, "name", er);
        student.stream().forEach(t -> u.add(t));

        // Step 2: MDELite built-in support
        isUnique(student, "name", er);
        
        // Step 3: more general way
        student.project("name").duplicates()
                .error(er,"multiple tuples in student have name='%s'",t->t.get("name"));
///UniqueName
        // finally
        er.printReportEH(System.out);
    }

    //@Test
    public void uniqueComboTest() {
        RegTest.Utility.redirectStdOut(errorfile);
        try {
            UniqueCombo();
        } catch (Exception e) {
        }
        RegTest.Utility.validate(errorfile, correct + "uniqueCombo.txt", false);
    }

    void UniqueCombo() {
        DB db = DB.readDataBase(testdata + "/StudentCourse.sc.pl");
        Table takes = db.getTableEH("takes");
        ErrorReport er = new ErrorReport();
///UniqueCombo
        // Step 1: Easiest way -- first remove nulls
        Unique u = new Unique(takes, "combo", er);
        takes.filter(t -> !(t.isNull("cid") || t.isNull("sid")))
                .forEach(t -> u.add(t, combo(t)));

        // add artificial error for this example 
        er.add("------");

        // Step 2: more verbose way 
        Unique v = new Unique(takes, "combo", er);
        for (Tuple t : takes.tuples()) {
            if (t.isNull("cid") || t.isNull("sid")) {
                continue;
            }
            String c = combo(t);
            v.add(t, c);
        }

        // finally
        er.printReportEH(System.out);
    }

    String combo(Tuple t) {
        return "'" + t.get("cid") + "," + t.get("sid") + "'"; // quotes optional
    }
///UniqueCombo

    //@Test
    public void attendanceTest() {
        RegTest.Utility.redirectStdOut(errorfile);
        try {
            attendance();
        } catch (Exception e) {
        }
        RegTest.Utility.validate(errorfile, correct + "attendance.txt", false);
    }

    void attendance() {
        DB db = DB.readDataBase(testdata + "/StudentCourse.sc.pl");
        Table course = db.getTableEH("course");
        Table takes = db.getTableEH("takes");
        ErrorReport er = new ErrorReport();
///attendance
        // check constraint
        course.filter(t -> t.is("name", "compilers") || t.is("name", "databases"))
              .filter(t -> t.join("cid", takes, "cid").count() < 2)
              .forEach(t -> er.add("%s course does not have enough students", t.get("name")));
///attendance
        // finally
        er.printReportEH(System.out);
    }

    //@Test
    public void jConstraintTest() {
        RegTest.Utility.redirectStdOut(errorfile);
        try {
            jConstraint();
        } catch (Exception e) {
        }
        RegTest.Utility.validate(errorfile, correct + "jConstraint.txt", false);
    }

    void jConstraint() {
        DB db = DB.readDataBase(testdata + "/StudentCourse.sc.pl");
        Table student = db.getTableEH("student");
        Table takes = db.getTableEH("takes");
        Table course = db.getTableEH("course");
        ErrorReport er = new ErrorReport();
///jConstraint
        // check constraint
        student.forEach(s -> {
            Table shortTakes = s.rightSemiJoin("sid", takes, "sid");
            Table joined = shortTakes.join("cid", course, "cid");
            if (joined.count() == 0) {
                er.add("student %s is not taking a course", s.get("name"));
            }
        });
///jConstraint
        // finally
        er.printReportEH(System.out);
    }

    //@Test
    public void jConstraint2Test() {
        RegTest.Utility.redirectStdOut(errorfile);
        try {
            jConstraint2();
        } catch (Exception e) {
        }
        RegTest.Utility.validate(errorfile, correct + "jConstraint.txt", false);
    }

    void jConstraint2() {
        DB db = DB.readDataBase(testdata + "/StudentCourse.sc.pl");
        Table student = db.getTableEH("student");
        Table takes = db.getTableEH("takes");
        Table course = db.getTableEH("course");
        Table tc = takes.join("cid", course, "cid");
        ErrorReport er = new ErrorReport();
///jConstraint2
        // check constraint
        student.filter(s -> s.rightSemiJoin("sid", tc, "takes.sid").count() == 0)
                .forEach(s -> er.add("student %s is not taking a course",
                s.get("name")));
///jConstraint2
        // finally
        er.printReportEH(System.out);
    }

    //@Test
    public void joinTest() {
        RegTest.Utility.redirectStdOut(errorfile);
        try {
            HashMap<String, Integer> collection = new HashMap<>();
            DB db = DB.readDataBase(testdata + "/dogOwner.do.pl");
            ErrorReport er = new ErrorReport();

            Table dog = db.getTableEH("dog");
            Table dogc = dog.copyForSelfJoins("dogc");
            Table dxdc = dog.join("name", dogc, "name");
            for (Tuple t : dxdc.tuples()) {
                String key = t.get("dog.name");
                Integer i = collection.get(key);
                i = (i == null) ? 1 : i + 1;
                collection.put(key, i);
            }
            for (String k : collection.keySet()) {
                Integer i = collection.get(k);
                if (i != 1) {
                    er.add("%s has %d replicas", k, i / 2);
                }
            }
            System.out.println("dogs with the same name");
            er.printReportEH(System.out);

        } catch (Exception e) {
        }
        RegTest.Utility.validate(errorfile, correct + "jointest.txt", false);
    }
}
