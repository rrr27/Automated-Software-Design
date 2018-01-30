package LectureExamples;

import PrologDB.DB;
import PrologDB.ErrorReport;
import PrologDB.Table;
import PrologDB.Unique;


public class DogConstraints {
    
    public static void main(String... args) {
        DB db = DB.readDataBase("test/LectureExamples/Correct/dogOwner.do.pl");
        Table dog = db.getTableEH("dog");
        ErrorReport er = new ErrorReport();
        
        // example 0: print all Australian Shepherds
        dog.filter(t->t.is("breed","aussie")).print(System.out);
        
        // example 0: another way using streams
        dog.stream().filter(t->t.is("breed","aussie")).forEach(t->t.print(System.out));
        
        barrier();
        
        // example 1: find all dogs whose fnames (first names) begin with
        //            'd' or 'h'
        dog.filter(t->{ String n = t.get("name");
                        return n.startsWith("d")||n.startsWith("h"); }) 
                .print(System.out);
        
        // example 1: stream version
        dog.stream()
                .filter(t->{ String n = t.get("name");
                        return n.startsWith("d")||n.startsWith("h"); }) 
                .forEach(t->t.print(System.out));
        
        barrier();
        
        // example 2: no 2 dogs sholuld have the same name -- nested loops
        Unique u = new Unique("name",er);
        dog.filter(t->dog.filter(x->x.is("name",t.get("name"))).count()>1)
                .forEach(r->u.add(r));
        
        // add artificial error for error report barrier
        er.addBarrier();
        
        // example 2: using streams
        Unique u1 = new Unique("name",er);
        dog.stream().filter(t->dog.stream().filter(x->x.is("name",t.get("name"))).count()>1)
                .forEach(r->u1.add(r));
        
        // add artificial error for error report barrier
        er.addBarrier();
        
        // example 2: another way
        dog.project("name")
                .duplicates()
                .error(er,"multiple dogs have name='%s'",t->t.get("name"));
        
        er.printReport(System.out);
    }
    
    static void barrier() {
        System.out.println("=======");
    }
    
}
