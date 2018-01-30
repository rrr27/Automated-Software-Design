package LectureExamples;

import MDELite.Marquee4Conform;
import PrologDB.Constraints;
import PrologDB.DB;
import PrologDB.ErrorReport;
import PrologDB.Table;
import Violett.ClassConform;


public class ConnConform {
    
    public static void main(String... args) {
        // Step 1: Standard marquee processing
        /*Marquee4Conform mark = new Marquee4Conform(ClassConform.class, ".con.pl", args);
        String inputFileName = mark.getInputFileName();
        */
        // Step 2: read database, tables, and create error handler
        DB db = DB.readDataBase("test/LectureExamples/Correct/ex.con.pl");
        Table company = db.getTableEH("company");
        Table contract = db.getTableEH("contract");
        Table person = db.getTableEH("contract");
        ErrorReport er = new ErrorReport();
        
        // Evaluate constraints
        // Step 3.1: if company has contract, value of contract > 500
        contract//.stream()
                .filter(t->!t.isNull("cid") && t.getInt("value")<=500)
                .forEach(t->er.add("company contract value violated in kid="+ t.get("kid")));
        
        // Step 3.2: if person has contract, value of contract is <100
        contract//.stream()
                .filter(t->!t.isNull("pid") && t.getInt("value")>=100)
                .forEach(t->er.add("person contract value violated in kid="+ t.get("kid")));
        
        // Step 3.3: Contract must be owned by a persor or company but not both
        contract//.stream()
                .filter(t-> (t.isNull("pid") && t.isNull("cid")) ||
                            (!t.isNull("pid") && !t.isNull("cid") ))
                .forEach(t->er.add("ownership failure in kid="+ t.get("kid")));
        
        // Step 3.4: every person referenced in a contract must correspond to a
        //          person tuple
       contract//.stream()
               .filter(t->!t.isNull("pid"))
               .filter(t->!t.joinExists("pid", person, "pid"))
               .forEach(t->er.add("bogus person reference in kid="+ t.get("kid")));
       
       // Step 3.5: every company referenced in a contract must correspond to a
       //           company tuple
       contract//.stream()
               .filter(t->!t.isNull("cid"))
               .filter(t->!t.joinExists("cid", company, "cid"))
               .forEach(t->er.add("bogus company reference in kid="+ t.get("kid")));
        
       // Step 4: output report -- ignore error thrown for this illustration
       try {
           er.printReportEH(System.out);
       } catch (Exception e) {}
       
       // now, we do the same constraints using built-in MDELite Constraints
       ErrorReport er1 = new ErrorReport();
       
       // Step 5.1: if company has contract, value of contract > 500
       Constraints.implies(contract, t->!t.isNull("cid"), t->t.getInt("value")>500, 
               "company contract value violated", er1);
       
       // Step 5.2: if person has contract, value of contract is <100
       Constraints.implies(contract, t->!t.isNull("pid"), t->t.getInt("value")<100, 
               "person contract value violated", er1);
       
       // Step 5.3: Contract must be owned by a persor or company but not both
       Constraints.iftest(contract, t->(t.isNull("pid")&& t.isNull("cid")) ||
               (!t.isNull("pid")&& !t.isNull("cid")), "ownership failure", er1);
       
       // Step 5.4: every person referenced in a contract must correspond to a
       //      person tuple
       Table personContracts = contract.filter(t->!t.isNull("pid"));
       Constraints.isLegit(personContracts, "pid", person, "pid", er1);
       
       // Step 5.5: every company referenced in a contract must correspond to a
       //      company tuple
       Table companyContracts = contract.filter(t->!t.isNull("cid"));
       Constraints.isLegit(companyContracts, "cid", company, "cid", er1);
       
       // Step 6: output report -- ignore error thrown for this illustration
       try {
           er1.printReportEH(System.err);
       } catch (Exception e) {}
    }
    
}
