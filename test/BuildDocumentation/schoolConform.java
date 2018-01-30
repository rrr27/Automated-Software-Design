package BuildDocumentation;

import PrologDB.*;

public class schoolConform {

    public static void main(String... args) {
        // Step 1: standard command-line processing
        MDELite.Marquee4Conform mark = new MDELite.Marquee4Conform(schoolConform.class, ".school.pl", args);
        String inputFileName = mark.getInputFileName();
        String AppName = mark.getAppName(inputFileName);  
             // if "X.school.pl" is input database name
             // then AppName = "X"

        // Step 2: open database to be validated + get needed tables
        DB db = DB.readDataBase(inputFileName);
        Table person = db.getTableEH("person");
        ErrorReport er = new ErrorReport();

        // Step 3: now perform database checks
        // Person Name Constraint
        person.filter(t -> checkCharacter(t))
                .forEach(t -> er.add("Person name not capitalized: %s", t.get("name")));

        // Name Uniqueness Contraint
        person.filter(t -> person.filter(g -> g.get("name").equals(t.get("name"))).count() > 1)
                .forEach(t -> er.add("Persons with duplicate name : %s" + t.get("name")));

        // print error report 
        er.printReport(System.err);
    }
    
    static boolean checkCharacter(Tuple t) {
        String n = t.get("name");
        if (n.length() == 0) {
            return true;
        }
        Character c = n.charAt(0);
        return Character.isLowerCase(c);
    }
}
