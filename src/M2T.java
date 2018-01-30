import MDELite.Marquee2Arguments;
import PrologDB.DB;
import PrologDB.Table;
import PrologDB.Tuple;

public class M2T extends MDELite.RunningBear{

    public static void main(String... args) {

        // Step 1: standard marquee processing
        Marquee2Arguments mark = new Marquee2Arguments(M2M.class,
                ".pl", ".txt", args);
        String inputFileName = mark.getInputFileName();

        // Step 2: read the families database and their tables
        DB in = DB.readDataBase(inputFileName);
        Table family = in.getTableEH("family");
        Table member = in.getTableEH("member");

        //Step 3: generate file
        openOut(inputFileName + ".txt");
        for(Tuple c : member.tuples()){
            StringBuffer sb = new StringBuffer();
            sb.append(c.get("firstName") + " ");
            if(!c.get("fid").equalsIgnoreCase("null")) {
                Table attr = c.rightSemiJoin("fid", family, "fid");
                sb.append(attr.tuples().get(0).get("lastName") + " has parents ");
                Table attr1 = attr.tuples().get(0).rightSemiJoin("dadid", member, "mid");
                sb.append(attr1.tuples().get(0).get("firstName") + " and ");
                Table attr2 = attr.tuples().get(0).rightSemiJoin("momid", member, "mid");
                sb.append(attr2.tuples().get(0).get("firstName"));
                l(sb.toString());
            }  else {
                if(c.getBool("isMale")){
                    Table attr = c.rightSemiJoin("mid", family, "dadid");
                    l(c.get("firstName") + " " + attr.tuples().get(0).get("lastName") + " has <unknown> parents");


                } else {
                    Table attr = c.rightSemiJoin("mid", family, "momid");
                    l(c.get("firstName") + " " +  attr.tuples().get(0).get("lastName") + " has <unknown> parents");
                }
            }
        }

        closeOut();

    }
}
