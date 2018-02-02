import MDELite.Marquee4Conform;
import PrologDB.Constraints;
import PrologDB.DB;
import PrologDB.ErrorReport;
import PrologDB.Table;
import Violett.ClassConform;

public class ConformFamilies2 {

    //TODO remove this
    //static final String correct = "test/LectureExamples/Correct/";
    public static void main(String... args) {


        Marquee4Conform mark = new Marquee4Conform(ClassConform.class, ".families2.pl", args);
        String inputFileName = mark.getInputFileName();

        // Step 2: read database, tables, and create error handler
        DB db = DB.readDataBase(inputFileName);

        Table family = db.getTableEH("family");
        Table member = db.getTableEH("member");

        ErrorReport er = new ErrorReport();
        //Check if both sonOf and daughterOf is filled
        Constraints.iftest(member,
                t->(!t.isNull("sonOf") && !t.isNull("daughterOf")),
                "present as both son and daughter failure", er);

        //Check whether sonOf or daughterOf is present only for 1 family
        //Constraints.isUnique(member,"mid",er);

        //Same check for member present in multiple families - add this
        member.project("mid")
                .duplicates()
                .error(er,"same member '%s' present in multiple families",t->t.get("mid"));

        //Check every family has 1 father and mother
        Constraints.iftest(family,
                t->(t.isNull("fatherid") || t.isNull("motherid")),
                "every family must have father and mother failure", er);

        // Step 4: output report -- ignore error thrown for this illustration
        try {
            er.printReportEH(System.out);
        } catch (Exception e) {}
    }

}
