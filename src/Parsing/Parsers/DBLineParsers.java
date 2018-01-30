package Parsing.Parsers;

import MDELite.ParsE;
import PrologDB.Column;
import PrologDB.DB;
import PrologDB.DBSchema;
import PrologDB.SubTableSchema;
import PrologDB.Table;
import PrologDB.TableSchema;
import PrologDB.Tuple;
import Parsing.DBPrimitives.DBaseStmt;
import Parsing.DBPrimitives.SubTableStmt;
import Parsing.DBPrimitives.TableStmt;
import Parsing.DBPrimitives.TupleStmt;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * this is a simple line-parser for database and schema specifications
 * Generally this class is internal to PrologDB package and should not
 * be called externally
 */
public class DBLineParsers {

    /**
     * turn Debug on if you want to see stack trace on Errors
     */
    static boolean Debug = false;
    

    /**
     * a dbase declaration is on a single line. this method parses the 
     * given line @ lineno returning the DBSchema given declared 
     * SubTableSchema, also returns a list of tableNames (not their schemas).  
     * ParseExceptions thrown if unable to parse
     *
     * @param line -- a string
     * @param lineno -- the line number associated with this line
     * @param tableNames -- an empty list which is to be filled with table names
     *         and returned
     * @return DBSchema object with SubTableSchema of schema initialized; 
     * also returns list of tableNames -- to be used later in parsing
     */
    public static DBSchema parseDBaseDecl(String line, int lineno, List<String> tableNames) {
        // Step 1: parse the line
        LineToParse l = new LineToParse(line, lineno);
        DBaseStmt st = new DBaseStmt(l);
        st.parse();

        // Step 2: unpack parsing (schemaName, list-of-tables)
        LinkedList<String> list = l.parseList;
        String schemaName = list.removeFirst();  
        DBSchema dbs = new DBSchema(schemaName);
        tableNames.addAll(list);
        return dbs;
        // note: no subtable declarations are parsed.  So the DBSchema is unfinished
    }

    /**
     * a subtable declaration is on a single line. this method parses the 
     * given line at lineno and adds the subtable declaration to DBSchema dbs. 
     * A ParseException is thrown if something wrong (parse or semantic error) 
     * is discovered.
     *
     * @param line to parse
     * @param lineno of line
     * @param dbs to add subtable definition
     * @param filename name of file from which this line was taken
     */
    public static void parseSubTableDecl(String line, int lineno, DBSchema dbs, String filename) {
        if (dbs == null) {
            throw ParsE.toss(ParsE.subTableDefBeforeDBase,lineno,filename);
        }

        // Step 1: parse the line
        LineToParse l = new LineToParse(line, lineno);
        SubTableStmt st = new SubTableStmt(l);
        st.parse();

        // Step 2: unpack parsing to get table SubTableSchema, and list of values, and create an empty tuple
        LinkedList<String> list = l.parseList;
        String tableName = list.removeFirst();  // this is the super table SubTableSchema
        TableSchema suptab = dbs.getTableSchemaEH(tableName);
        ArrayList<TableSchema> subTableSchemas = new ArrayList<>();
        for (String name : list) {
            TableSchema ts = dbs.getTableSchemaEH(name);
            subTableSchemas.add(ts);
        }

        // Step 3: assemble the subtableschema declaaration
        SubTableSchema sts = new SubTableSchema(suptab, subTableSchemas);
        dbs.addSubTableSchema(sts);
    }

    /**
     * a table declaration is on a single line. this method parses the given
     * line @ lineno and adds the table definition to schema dbs
     *
     * @param line to parse
     * @param lineno of line
     * @param dbs to add table declaration
     * @param filename name of file from which this line was read
     */
    public static void parseTableDecl(String line, int lineno, DBSchema dbs, String filename) {
        // Step 0: simple error checking;
        if (dbs == null) {
            throw ParsE.toss(ParsE.tableDefBeforeDBase,lineno, filename);
        }
        
        LineToParse l = new LineToParse(line, lineno);
        TableSchema ts = parseTableDecl(l);

        dbs.addTableSchema(ts);
    }
    
    /**
     * a table declaration is on a single line. this method parses the given
     * line @ lineno; returns tableschema
     *
     * @param l -- LineToParse
     * @return TableSchema of line input
     */
    public static TableSchema parseTableDecl(LineToParse l)  {

        // Step 1: parse line

        TableStmt st = new TableStmt(l);
        st.parse();

        // Step 2: unpack parsing to get table SubTableSchema, and list of values, and create an empty tuple
        LinkedList<String> list = l.parseList;
        String tableName = list.removeFirst();  // this is the table SubTableSchema
        TableSchema tableDef = new TableSchema(tableName);

        // Step 3: assemble the column defs
        for (String name : list) {
            // a column name can be: identifier or identifier:kind
            // a column name can be quoted or unquoted: cname or "cname" or 'cname'
            boolean isQuoted = name.startsWith("\"") || name.startsWith("'");
            String fleased = name.replaceAll("\"","").replaceAll("'","");
            Column newColumn = new Column((isQuoted?"'":"")+fleased);
            tableDef.addColumn(newColumn);
        }
        
        // Step 4: return tableDef
        return tableDef;
    }

    /**
     * parses a line at lineno, expecting to parse a legal prolog tuple
     * declaration. The extracted tuple is inserted into database db; the
     * possible table declarations are defined in db.getSchema() of the database
     *
     * @param line to parse
     * @param lineno line number of the line
     * @param db database in which to insert
     * @param fileName name of file from which this line was read
     *
     */
    protected static void parseTupleDecl(String line, int lineno, DB db, String fileName) {
        // Step 1: parse tuple
        LineToParse l = new LineToParse(line, lineno);
        TupleStmt st = new TupleStmt(l);
        st.parse();
        
        // Step 2: unpack parsing to get table and list of values, and create an empty tuple
        LinkedList<String> list = l.parseList;
        String tableName = list.removeFirst(); // this is the table SubTableSchema
        Table tbl = db.getTableEH(tableName);
        TableSchema tableDef = db.getTableSchema(tableName);
        Tuple newTuple = new Tuple(tableDef);
        if (list.size() != tableDef.size()) {
            throw ParsE.toss(ParsE.tupleHasInsufficientValues,lineno, fileName, tableDef.getName());
        }
        // Step 3: assemble the tuple
        for (int i = 0; i < tableDef.size(); i++) {
            String value = list.removeFirst(); // this is a column value
            if (value.startsWith("'") || value.startsWith("\"")) {
                value = value.substring(1,value.length()-1);
            } 
            Column target = tableDef.getColumn(i);
            String columnName = target.getName().replace("\"", "");
            newTuple.set(columnName, value);
        }
        // Step 4: add the tuple to the table
        tbl.add(newTuple);
    }
}
