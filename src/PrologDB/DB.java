package PrologDB;

import Parsing.Parsers.DBLineParsers;
import MDELite.Error;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DB objects are database instances of DBSchema objects
 */
public class DB extends DBLineParsers {

    /**
     * DB state = database name, a set of named tables, and a database schema
     */
    private String name;
    private final Map<String, Table> tables;
    private final DBSchema schema;

    // CONSTRUCTORS
    /**
     * Create an empty database with a given name and DBSchema type
     *
     * @param name of the database
     * @param schema of the database
     */
    public DB(String name, DBSchema schema) {
        if (!schema.isFinished())
            throw Error.toss(Error.dbSchemaNotFinished, schema.getFullName());
        this.name = name;
        this.schema = schema;
        tables = new HashMap<>();
        for (TableSchema t : schema.getTableSchemas()) {
            Table tab = new Table(t);
            tables.put(t.getName(), tab);
            tab.parent = this;
        }
    }

    /**
     * reads and parses a prolog database from given string filename. may throw
     * parseExceptions or Errors, which terminates processing. Errors are
     * reported to System.err.
     *
     * @param localFileName is the SubTableSchema of the file of the Prolog
     * database
     * @return Prolog database object of type DB
     */
    public static DB readDataBase(String localFileName) {
        DB.dbCheck(localFileName);

        File in = new File(localFileName);
        if (!in.exists()) {
            throw Error.toss(Error.databaseFileNoExist, localFileName);
        }
        return readDataBase(in);
    }

    /**
     * reads and parses a database from given string filename. may throw
     * parseExceptions or Errors, which terminates processing.
     *
     * @param infile is the File of the Prolog database
     * @return Prolog database object of type DB
     */
    public static DB readDataBase(File infile) {
        DB db;
        LineNumberReader br;
        String filename = infile.getAbsolutePath().replace("\\","/");
        String[] parts = filename.split("/");
        String[] subparts = parts[parts.length - 1].split("\\.");
        int last = subparts.length-1;
        if (!subparts[last].equals("pl")) {
            throw Error.toss(Error.wrongDBNameFormat, filename);
        }
        String dbname = subparts[0];
        // read the file in the first pass: get its schema
        DBSchema dbs = DBSchema.readSchema(infile);

        db = new DB(dbname, dbs);
        String line;
        try {
            // Step 1: Read infile by line
            br = new LineNumberReader(new InputStreamReader(new FileInputStream(infile)));

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("") || line.startsWith("tuple(") || line.startsWith("dbase(") || line.contains(":-") || line.startsWith("table(")
                        || line.startsWith("subtable(") 
                        || line.startsWith("/*") 
                        || line.startsWith("//") 
                        || line.startsWith("%")) {
                    continue;
                }
                parseTupleDecl(line, br.getLineNumber(), db, filename); // if not tuple line, ignore
            }
            br.close();
        } catch (IOException e) {
            throw Error.toss(Error.ioerror, infile.getName(), e.getMessage());
        }
        return db;
    }

    // ACCESSORS and INFORMATION
    /**
     * returns the name of this database
     *
     * @return name of this database
     */
    public String getName() {
        return name;
    }

    /**
     * the "full" name of a database is "databaseName"."schemaName"."pl";
     *
     * @return returns full name of this database
     */
    public String getFullName() {
        return String.format("%s.%s.pl", name, getSchema().getName());
    }

    /**
     * returns DBSchema that this database instantiates
     *
     * @return DBSchema of this database
     */
    public DBSchema getSchema() {
        return schema;
    }

    /**
     * returns name of 'this' DBSchema
     *
     * @return name of 'this' DBSchema
     */
    public String getSchemaName() {
        return schema.getName();
    }

    /**
     * returns a list of subtable declarations for this database's schema. this
     * method delegates to the schema object to get its subtables.
     *
     * @return list of subtable declarations for this database's schema a
     * delegate method
     */
    public List<SubTableSchema> getSubTableSchemas() {
        return schema.getSubTableSchemas();
    }

    /**
     * returns subtable declaration for a given table schema ts; if no such
     * declaration exists, null is returned; this declaration lists the
     * subtables of the given table.
     *
     * @param ts -- table schema for which subtable declaration is to be
     * returned
     * @return subtable declaration for a given table schema ts
     */
    public SubTableSchema getSubTableSchema(TableSchema ts) {
        for (SubTableSchema s : getSubTableSchemas()) {
            if (s.getSuper().getName().equals(ts.getName())) {
                return s;
            }
        }
        return null;
    }

    /**
     * returns a collection of tables that define this database
     *
     * @return a collection of tables that define this database
     */
    public Collection<Table> getTables() {
        return tables.values();
    }

    /**
     * returns the Table object for the table with SubTableSchema tableName;
     * null is returned if table does not exist
     *
     * @param tableName -- SubTableSchema of table to return
     * @return Table object or null
     */
    public Table getTable(String tableName) {
        return tables.get(tableName);
    }

    /**
     * error handler of getTable(String); return the Table object for the table
     * with SubTableSchema tableName; Error is thrown if no such table exists.
     *
     * @param tableName -- SubTableSchema of table to return
     * @return Table object or throws Error
     */
    public Table getTableEH(String tableName) {
        Table t = tables.get(tableName);
        if (t == null) {
            throw Error.toss(Error.tableNoExist, tableName, schema.getName());
        }
        return t;
    }

    /**
     * returns the TableSchema of the table with given SubTableSchema; null if
 table is not found
     *
     * @param name SubTableSchema of desired table
     * @return either TableSchema of this table or null
     */
    public TableSchema getTableSchema(String name) {
        return schema.getTableSchema(name);
    }

    /**
     * returns the TableSchema of the table with given SubTableSchema; Error is
 thrown otherwise
     *
     * @param name SubTableSchema of desired table
     * @return either TableSchema of this table or null
     */
    public TableSchema getTableSchemaEH(String name) {
        return schema.getTableSchemaEH(name);
    }

    // UTILITIES
    /** delete all tuples in the database 
     */
    public void deleteAll() {
        for (Table t : getTables())
            t.deleteAll();
    }
    
    /** does 'this' database equal another database
     * @param other database
     * @return true if both databases are equal
     */
    public boolean equals(DB other) {
        for(Table t : getTables()) {
            Table othert = other.getTable(t.getName());
            if (othert==null)
                return false;
            if (!t.equals(othert))
                return false;
        }
        return true;
    }
    
    /**
     * Returns true if there is a table with tableName in the database
     *
     * @param tableName of table
     * @return true if table is present, false otherwise
     */
    public boolean containsTable(String tableName) {
        return tables.containsKey(tableName);
    }

    /**
     * assigns a new SubTableSchema to this database
     *
     * @param name new SubTableSchema
     */
    public void rename(String name) {
        this.name = name;
    }

    /**
     * asserts that a file pathName ends in ".pl"; Error is thrown otherwise
     *
     * @param pathName -- filename to verify
     */
    static void dbCheck(String pathName) {
        if (!pathName.endsWith(".pl")) {
            throw Error.toss(Error.wrongDatabaseNameFormat, pathName);
        }
    }

    /**
     * print the database to PrintStream out;
     * typically used for demonstrations as it doesn't check the
     * format of the filename.  Use print() or print(String).
     *
     * @param out PrintStream destination
     */
    public void print(PrintStream out) {
        out.format("%s\n", schema.printDBaseDecl());

        for (TableSchema s : schema.getTableSchemas()) {
            Table t = tables.get(s.getName());
            t.printLocal(out);
        }

        for (SubTableSchema sts : schema.getSubTableSchemas()) {
            sts.print(out);
        }
    }
    
     /**
     * print DB to System.out
     */
    public void print() {
        print(System.out);
    }

    /**
     * print database in file with filename
     * @param filename -- name of file in which to print database
     */
    public void print(String filename) {
//        String ending = String.format("%s.pl",schema.getName());
//        if (!filename.endsWith(ending)) {
//            throw Error.toss(Error.wrongDBNameFormat, filename);
//        }
        try {
            PrintStream ps = new PrintStream(filename);
            print(ps);
        } catch (IOException ex) {
            throw Error.toss(Error.ioerror, filename, ex.getMessage());
        }
    }
}
