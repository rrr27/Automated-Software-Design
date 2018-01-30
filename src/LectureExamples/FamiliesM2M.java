package LectureExamples;

import PrologDB.DBSchema;
import PrologDB.DB;
import PrologDB.Tuple;
import PrologDB.Table;
import MDELite.Marquee2Arguments;
import PrologDB.ColumnCorrespondence;

public class FamiliesM2M {

    static final String correct = "test/LectureExamples/Correct/";

    public static void main(String... args) {

///init
        // Step 1: standard marquee processing
        /*Marquee2Arguments mark = new Marquee2Arguments(FamiliesM2M.class,
                ".families.pl", ".persons.pl", args);
        String inputFileName = mark.getInputFileName();*/
        String outputSchema = correct + "persons.schema.pl";
///init

///readdb
        // Step 2: read the families database and their tables
        DB in = DB.readDataBase("test/LectureExamples/Correct/inria.families.pl");
        Table fam = in.getTableEH("family");
        Table mem = in.getTableEH("member");
///readdb

///createdb
        // Step 3: create an empty persons database with empty tables
        DBSchema outSchema = DBSchema.readSchema(outputSchema);
        DB inria = new DB(in.getName(), outSchema);
        Table male = inria.getTableEH("male");
        Table female = inria.getTableEH("female");
///createdb

///xform
        // Step 4: fill in father tuples, mother tuples, son, then daughter
        fam.join("fatherid", mem, "mid").forEach(t -> helper(male, t));
        fam.join("motherid", mem, "mid").forEach(t -> helper(female, t));
        fam.join("id", mem, "sonOf").forEach(t -> helper(male, t));
        fam.join("id", mem, "daughterOf").forEach(t -> helper(female, t));
///xform
        
///print
        // Step 5: print out database
        inria.print();
///print
        inria.deleteAll();
         
        // Step 4': define correspondence and print
///alternative
        ColumnCorrespondence c = new ColumnCorrespondence()
                .add("id",t->t.get("member.mid"))
                .add("fullName",t->t.get("member.firstName") + " " + t.get("family.lastName"));
        
        male.addTuples(fam.join("fatherid", mem, "mid"),c);
        female.addTuples(fam.join("motherid", mem, "mid"),c);
        male.addTuples(fam.join("id", mem, "sonOf"),c);
        female.addTuples(fam.join("id", mem, "daughterOf"),c);
///alternative
        
        inria.print();
    }

///helper
    static void helper(Table tab, Tuple t) {
        String id = t.get("member.mid");
        String name = t.get("member.firstName") + " " + t.get("family.lastName");
        tab.addTuple(id, name);
    }
///helper
}
