package LectureExamples;

import MDELite.Marquee1Argument;
import PrologDB.DB;
import PrologDB.Table;
import PrologDB.Tuple;
import java.util.LinkedList;

public class OwnersOfMostDogs {

    public static void main(String... args) {
        // Step 1: standard marquee processing
        //Marquee1Argument mark = new Marquee1Argument(OwnersOfMostDogs.class, ".do.pl", args);
        //String inputFileName = mark.getInputFileName();

        // Step 2: open the database and read the tables
        DB db = DB.readDataBase("test/LectureExamples/Correct/dogOwner.do.pl");
        Table dog = db.getTableEH("dog");
        Table when = db.getTableEH("when");
        Table owner = db.getTableEH("owner");
        
        // Step 3: initialize accumulators
        int most = 0;
        Table mostOwner = new Table(owner);
        
        // Step 4: for each owner, count the number of dogs and 
        //         update the result
        for (Tuple o : owner.tuples()) {
            Table oWhen = o.rightSemiJoin("oid", when, "oid");
            int m = oWhen.join("did", dog, "did").count();
            if (m == most) {
                mostOwner.add(o);
            }
            if (m > most) {
                mostOwner = new Table(owner);
                mostOwner.add(o);
                most = m;
            }
        }
        
        // Step 5: print result
        mostOwner.print(System.out);

//        // Step 3: initialize accumulators
//        int most = 0;
//        LinkedList<Tuple> mostOwner = new LinkedList<>();
//
//        // Step 4: for each owner, count the number of dogs and 
//        //         update the result
//        for (Tuple o : owner.tuples()) {
//            Table oWhen = o.rightSemiJoin("oid", when, "oid");
//            int m = oWhen.join("did", dog, "did").size();
//            if (m == most) {
//                mostOwner.add(o);
//            }
//            if (m > most) {
//                mostOwner = new LinkedList<>();
//                mostOwner.add(o);
//                most = m;
//            }
//        }
//
//        // Step 5: print result
//        mostOwner.stream().forEach(t -> t.print(System.out));
    }
}
