package LectureExamples;

import MDELite.Marquee1Argument;
import PrologDB.DB;
import PrologDB.Table;
import PrologDB.Tuple;
import PrologDB.toTable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class DogStream {
    public static void main(String... args) {
        // Step 1: standard marquee processing
        //Marquee1Argument mark = new Marquee1Argument(OwnersOfMostDogs.class, ".do.pl", args);
        //String inputFileName = mark.getInputFileName();
        
        // Step 2: open the database and read the dog table
        DB db = DB.readDataBase("test/LectureExamples/Correct/dogOwner.do.pl");
        Table dog = db.getTableEH("dog");
        
        // Step 3: convert between streams and lists
        Stream<Tuple> dogStream = dog.stream();
        List<Tuple> dogList = dogStream.collect(Collectors.toList());
        Stream<Tuple> dogStream2 = dogList.stream();
        
        // Step 4: print tuples -- should be the same result
        dog.stream().forEach(d->d.print(System.out));
        System.out.println("---");
        dogStream2.forEach(d->d.print(System.out));
        
        // Step 5: convert dog stream into a table and print table
        dog.stream().collect(new toTable(dog)).print(System.out);
    }
}
