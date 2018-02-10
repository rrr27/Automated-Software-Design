package Allegory;

import MDELite.Marquee2Arguments;
import PrologDB.Column;
import PrologDB.ColumnCorrespondence;
import PrologDB.DB;
import PrologDB.DBSchema;
import PrologDB.ErrorReport;
import PrologDB.SubTableSchema;
import PrologDB.Table;
import PrologDB.toTable;
import PrologDB.TableSchema;
import PrologDB.Tuple;

import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static MDELite.RunningBear.getOut;

public class Vpl2Schema {

    public static void main(String... args) {

        // Step 1: standard marquee processing
        Marquee2Arguments mark = new Marquee2Arguments(Vpl2Schema.class,
                ".vpl.pl", ".schema.pl", args);
        String inputFileName = mark.getInputFileName();
        DB in = DB.readDataBase(inputFileName);
        Table box = in.getTableEH("vBox");
        Table association = in.getTableEH("vAssociation");

        DBSchema dbSchema = new DBSchema(in.getName());

        //add all the box data to tables.
        box.forEach(t -> dbSchema.addTableSchema(addTable(t, "name", "fields")));


        // add association to table in case no 0..1 or 1
        // add attribute to table otherwise

        for(Tuple tuple : association.tuples()){
            String arrow1 = tuple.get("arrow1");
            String arrow2 = tuple.get("arrow2");
            String cid1 = tuple.get("cid1");
            String cid2 = tuple.get("cid2");
            String role1 = tuple.get("role1");
            String role2 = tuple.get("role2");

            //case when association has * so needs to be added as table
            if(arrow1.equalsIgnoreCase("") && arrow2.equalsIgnoreCase("")) {
                String tName = role1 + "_" + role2;
                TableSchema tableSchema = new TableSchema(tName);
                if(cid1.equalsIgnoreCase(cid2)){
                    Table newTable = box.filter(t -> t.get("id").equalsIgnoreCase(cid1));
                    newTable.tuples().forEach(t -> tableSchema.addColumn(role1 + ":" + t.get("name")));
                    newTable.tuples().forEach(t -> tableSchema.addColumn(role2 + ":" + t.get("name")));
                    dbSchema.addTableSchema(tableSchema);
                } else {
                    Table newTable = box.filter(t -> t.get("id").equalsIgnoreCase(cid1) || t.get("id").equalsIgnoreCase(cid2));
                    newTable.tuples().forEach(t -> tableSchema.addColumn(t.get("name")));
                    dbSchema.addTableSchema(tableSchema);
                }
                //case when association is of triangle that means subtable
            } else if (arrow1.equalsIgnoreCase("TRIANGLE") || arrow2.equalsIgnoreCase("TRIANGLE")){

                if(arrow1.equalsIgnoreCase("TRIANGLE")){
                    Table newTable1 = box.filter(t -> t.get("id").equalsIgnoreCase(cid1));
                    List<Tuple> tuples1 = newTable1.rightSemiJoin("id", box, "id").tuples();
                    Table newTable2 = box.filter(t -> t.get("id").equalsIgnoreCase(cid2));
                    List<Tuple> tuples2 = newTable2.rightSemiJoin("id", box, "id").tuples();

                    SubTableSchema sts = dbSchema.getSubTableSchema(tuples1.get(0).get("name"));
                    if( sts == null){
                        sts = new SubTableSchema(dbSchema.getTableSchemaEH(tuples1.get(0).get("name")));
                        dbSchema.addSubTableSchema(sts);
                    }
                    sts.addSubTableSchemas(dbSchema.getTableSchemaEH(tuples2.get(0).get("name")));
                    dbSchema.addSubTableSchema(sts);
                } else if (arrow2.equalsIgnoreCase("TRIANGLE")) {
                    Table newTable1 = box.filter(t -> t.get("id").equalsIgnoreCase(cid2));
                    List<Tuple> tuples1 = newTable1.rightSemiJoin("id", box, "id").tuples();
                    Table newTable2 = box.filter(t -> t.get("id").equalsIgnoreCase(cid1));
                    List<Tuple> tuples2 = newTable2.rightSemiJoin("id", box, "id").tuples();
                    SubTableSchema sts = dbSchema.getSubTableSchema(tuples1.get(0).get("name"));
                    if( sts == null){
                        sts = new SubTableSchema(dbSchema.getTableSchemaEH(tuples1.get(0).get("name")));
                        dbSchema.addSubTableSchema(sts);
                    }
                    sts.addSubTableSchemas(dbSchema.getTableSchemaEH(tuples2.get(0).get("name")));

                }

            } else {
                // case when 0..1 or 1 associations
                //assuming that either role or middle name will be present for a given association

                if(arrow1.equalsIgnoreCase("")){
                    //check role2

                    Table newTable1 = box.filter(t -> t.get("id").equalsIgnoreCase(cid1));
                    List<Tuple> tuples1 = newTable1.rightSemiJoin("id", box, "id").tuples();

                    TableSchema ts = dbSchema.getTableSchemaEH(tuples1.get(0).get("name"));
                    if(role2.equalsIgnoreCase("")){
                        ts.addColumn(tuple.get("middleLabel"));
                    } else {
                        ts.addColumn(role2);
                    }

                } else if (arrow2.equalsIgnoreCase("")) {
                    //check role1

                    Table newTable1 = box.filter(t -> t.get("id").equalsIgnoreCase(cid2));
                    List<Tuple> tuples1 = newTable1.rightSemiJoin("id", box, "id").tuples();

                    TableSchema ts = dbSchema.getTableSchemaEH(tuples1.get(0).get("name"));
                    if(role1.equalsIgnoreCase("")){
                        ts.addColumn(tuple.get("middleLabel"));
                    } else {
                        ts.addColumn(role1);
                    }
                }

            }
        }


        dbSchema.finishAndPropagateAttributes();

        PrintStream ps = getOut();    // get the RB PrintStream object
        dbSchema.print(ps);                // dbs is a DBSchema object which you will create; this line prints it out
        // to the RB PrintStream

    }

    private static TableSchema addTable(Tuple tuple, String columnName, String field) {
        TableSchema tableSchema = new TableSchema(tuple.get(columnName));
        String[] fieldEntries = tuple.get(field).split("%");
        for(String fEntry : fieldEntries){
            tableSchema.addColumn(fEntry);
        }
        return tableSchema;
    }

}
