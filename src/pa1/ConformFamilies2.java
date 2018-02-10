package pa1;

import MDELite.Marquee4Conform;
import PrologDB.DB;
import PrologDB.ErrorReport;
import PrologDB.Table;
import Violett.ClassConform;

public class ConformFamilies2 {
    @SuppressWarnings("unchecked")
    public static void main(String... args) throws Exception {
        Marquee4Conform mark = new Marquee4Conform(ClassConform.class, ".families2.pl", args);
        String inputFileName = mark.getInputFileName();
        ErrorReport er = new ErrorReport();

        // Step 2: read database, tables, and create error handler
        DB db = DB.readDataBase(inputFileName);

        Table family = db.getTable("family");
        Table member = db.getTable("member");

        //Every family must have a father Member
        family.filter(t->t.isNull("fatherid"))
                .error(er, "Missing a legal father member in %s", t->t.getId());
        //Every family must have a mother Member
        family.filter(t->t.isNull("motherid"))
                .error(er, "Missing a legal mother member in %s", t->t.getId());
        //Every family must have only one father and mother Member
        family.project("id")
                .duplicates()
                .error(er, "%s has two sets of parents", t->t.getId());

        //A member can never be both son and daughter
        member.filter(t->!t.isNull("sonOf"))
                .filter(t->!t.isNull("daughterOf"))
                .error(er, "%s is a son and a daughter", t->t.getId());
        //A member can be a  sonOf  or  daughterOf  precisely one family
        member.project("mid")
                .duplicates()
                .error(er, "%s is in two families", t->t.getId());

        //Every family must have a LEGAL father Member
        family.filter(t->!t.isNull("fatherid"))
                .antiSemiJoin("fatherid", member, "mid")
                .forEach(t -> er.add("Father member does not exist in " + t.get("id")));
        //Every family must have a LEGAL mother Member
        family.filter(t->!t.isNull("motherid"))
                .antiSemiJoin("motherid", member, "mid")
                .forEach(t -> er.add("Mother member does not exist in " + t.get("id")));

        //Every member who has a family points to a LEGAL family
        member.filter(t->!t.isNull("sonOf"))
                .antiSemiJoin("sonOf", family, "id")
                .forEach(t -> er.add("Family does not exist for son " + t.get("mid")));
        member.filter(t->!t.isNull("daughterOf"))
                .antiSemiJoin("daughterOf", family, "id")
                .forEach(t -> er.add("Family does not exist for daughter " + t.get("mid")));

        // Step 4: output report
        System.out.println(inputFileName);
        try {
            er.printReportEH(System.out);
            System.out.println("No Errors");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
