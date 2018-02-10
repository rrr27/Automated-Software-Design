package pa1;

import PrologDB.Table;
import MDELite.Marquee2Arguments;
import PrologDB.DB;

public class M2T extends MDELite.RunningBear{
    public static void main(String... args) throws Exception {

        Marquee2Arguments mark = new Marquee2Arguments(M2T.class, "families1.pl", ".txt", args);
        RBSetup(mark, args);

        DB db = DB.readDataBase(args[0]);

        Table family = db.getTable("family");
        Table member = db.getTable("member");
        Table dad = member.copyForSelfJoins("family_dad");
        Table mom = member.copyForSelfJoins("family_mom");
        member.join("fid", family, "fid")
                .join("family.momid", mom, "mid")
                .join("family.dadid", dad, "mid")
                .forEach(t -> l(t.get("member.firstName") + " " + t.get("family.lastName") +
                        " has parents " +  t.get("family_dad.firstName") + " and " + t.get("family_mom.firstName")));

        Table needToPrintDad = member.antiSemiJoin("fid", family, "fid").copyForSelfJoins("member_father");
        Table needToPrintMom = member.antiSemiJoin("fid", family, "fid").copyForSelfJoins("member_mother");
        family.join("dadid", needToPrintDad, "mid")
                .join("family.momid", needToPrintMom, "mid")
                .forEach(t -> {
                    l(t.get("member_father.firstName") + " " + t.get("family.lastName") + " has <unknown> parents");
                    l(t.get("member_mother.firstName") + " " + t.get("family.lastName") + " has <unknown> parents");
                });

        /* Another approach
        //Step 3: generate file
        openOut(inputFileName + ".txt");
        for(Tuple c : member.tuples()){
            StringBuffer sb = new StringBuffer();
            sb.append(c.get("firstName") + " ");
            // fid is not null means is son or daughter
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
         */

    }
}