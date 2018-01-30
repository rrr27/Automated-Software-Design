package LectureExamples;

import PrologDB.ErrorReport;
import PrologDB.DB;
import PrologDB.Tuple;
import PrologDB.Table;
import MDELite.Marquee4Conform;
import Violett.ClassConform;

public class ChildEmpConstraint {
    
    public static void main(String... args) {

         // Step 1: Standard marquee processing
        /*Marquee4Conform mark = new Marquee4Conform(ClassConform.class, ".ce.pl", args);
        String inputFileName = mark.getInputFileName();
        */
        // Step 2: read database and get tables
        DB db = DB.readDataBase("test/LectureExamples/Correct/ex.ce.pl");
        Table Person = db.getTable("Person");
        Table Employer = db.getTable("Employer");
        Table ChildOf = db.getTable("ChildOf");
        ErrorReport er = new ErrorReport();
        
        // Step 3: evaluate constraint: a child of an employee cannot
        // work for the same employer as his/her parent.  Or the
        // children of each employee cannot work for the same employer
        
        Person.forEach(p->{ Table employersOfp = p.rightSemiJoin("eid", Employer, "eid");
                            Table childIds = p.rightSemiJoin("pid",ChildOf,"pid").rightSemiJoin("cid",Person,"pid").rightSemiJoin("eid",Employer,"eid");
                            Table result = childIds.intersect(employersOfp);
                            if (result.count()!=0) {
                                er.add("person %s and at least one child work for %s",p.get("name"), helper(result) );
                            }
        });
        
        er.printReportEH(System.out);
    }
    
    static String helper(Table tab) {
        String result = "";
        for (Tuple t : tab.tuples()) 
            result = result + t.get("name") + " ";
        return result;
    }

}
