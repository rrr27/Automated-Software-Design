package PrologDB;

import Parsing.Parsers.DBLineParsers;
import MDELite.Error;
import MDELite.ParsE;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A DBSchema object defines the schema of a database
 *
 */
public class DBSchema extends DBLineParsers {

    /**
     * state of a DBSchema: a string name, a list of TableSchemas, a list of
     * SubTableSchemas, which encodes a forest of inheritance hierarchies, and a
     * boolean indicator if the schema has been finished (meaning ready to be
     * instantiated, no further changes to it can be made AND that columns of
     * supertables have been propagated to subtables).
     */
    private String name;
    private final LinkedList<TableSchema> tableSchemas;
    private LinkedList<SubTableSchema> subtables;
    private boolean finished; // flattened implies finished.

    // CONSTRUCTORS
    /**
     * create DBSchema that has SubTableSchema and a list of TableSchemas can
     * incrementally add subtable tableSchemas subsequently -- before schema is
     * instantiated as a database
     *
     * @param name of DBSchema
     * @param schemas comma-separated list of table tableSchemas
     */
    public DBSchema(String name, TableSchema... schemas) {
        this.name = name;
        this.tableSchemas = new LinkedList<>();
        this.tableSchemas.addAll(Arrays.asList(schemas));
        this.subtables = new LinkedList<>();
        this.finished = false;
    }

    /**
     * This constructor is used for the incremental construction of database
     * tableSchemas. First provide a SubTableSchema of the schema, then
     * incrementally add tableSchemas per table and then subtables. By default,
     * schema is not flattened.
     *
     * @param name -- SubTableSchema given to the DBSchema
     */
    public DBSchema(String name) {
        this.name = name;
        this.tableSchemas = new LinkedList<>();
        subtables = new LinkedList<>();
        finished = false;
    }

    /**
     * copy a database schema. Only unless it is unfinished can the copy be
     * unfinished.
     *
     * @return copied schema
     */
    public DBSchema copy() {
        DBSchema s = new DBSchema(name);
        s.tableSchemas.addAll(tableSchemas);
        s.subtables.addAll(subtables);
        s.finished = finished;
        return s;
    }

    /**
     * reads .ooschema and .schema files; throws Error when an error is
     * encountered. Returns DBSchema declaration in file; may flatten it if
     * .ooschema is read.
     *
     * @param schemafile -- SubTableSchema of schema file
     * @return DBSchema declaration of file
     */
    public static DBSchema readSchema(String schemafile) {
        File in = new File(schemafile);
        return readSchema(in);
    }

    /**
     * another version of readSchema
     *
     * @param schemafile -- File to read
     * @return DBSchema object
     */
    public static DBSchema readSchema(File schemafile) {
        String filename = schemafile.getAbsolutePath().replace("\\", "/");
        LineNumberReader br = readFileCheck(schemafile, filename);
        return readSchema(filename, br);
    }

    /**
     * helper method -- does basic checks on File schemafile
     *
     * @param schemafile -- file that contains database schema
     */
    private static LineNumberReader readFileCheck(File schemafile, String filename) {
        LineNumberReader br;

        if (filename.endsWith(".ooschema.pl") || filename.endsWith(".pl")) {
            try {
                br = new LineNumberReader(new InputStreamReader(new FileInputStream(filename)));
                return br;
            } catch (FileNotFoundException e) {
                throw Error.toss(Error.dbSchemaFileNoExist, filename);
            }
        }
        throw Error.toss(Error.wrongFileName, filename);
    }
    
    /** used when it is easier to pass in a string declaration of a DBSchema
     * rather than building it programmatically
     * @param nameOfSchema -- name of pseudo file
     * @param stringDefOfSchema -- string contents (typically multiline) of pseudo file
     * @return DBSchema -- of pseudo file
     */
    public static DBSchema readSchema(String nameOfSchema, String stringDefOfSchema) {
        InputStream is = new ByteArrayInputStream(stringDefOfSchema.getBytes());
        LineNumberReader br = new LineNumberReader(new InputStreamReader(is));
        return readSchema(nameOfSchema, br);
    }
    
    /**
     * reads prolog schema from File schemafile; can read .ooschema and .schema
     * files
     *
     * @param filename the value of filename
     * @param br - line number reader of file to read
     * @return the PrologDB.DBSchema
     */
    private static DBSchema readSchema(String filename, LineNumberReader br) {
        DBSchema dbs = null;
        List<String> tableNames = new ArrayList<>();
        try {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("") 
                        || line.startsWith("%")
                        || line.startsWith("/*") 
                        || line.startsWith("//") ) {
                    continue;
                }
                int lineno = br.getLineNumber();

                try {
                    if (line.startsWith("dbase(")) {
                        if (dbs != null) {
                            throw ParsE.toss(ParsE.dbaseDeclsTooMany, lineno, filename);
                        }
                        dbs = parseDBaseDecl(line, lineno, tableNames);
                    } else if (line.startsWith("table")) {
                        parseTableDecl(line, lineno, dbs, filename);
                    } else if (line.startsWith("subtable")) {
                        parseSubTableDecl(line, lineno, dbs, filename);
                    }
                    // ignore everything else
                } catch (RuntimeException e) {
                    throw ParsE.toss(ParsE.tableSchemaParseError, br.getLineNumber(), filename, e.getMessage());
                }
            }
            br.close();
            // we have parsed a database schema file
            // Let's make sure that the tables declared match what is in the dbase declaration

            if (dbs == null) {
                throw Error.toss(Error.dbSchemaNotValid, filename);
            }

            dbs.finished = !filename.endsWith(".ooschema.pl");

            // now basic verification.  tableNames is a list of names
            // produced when parsing a dbase declaration.  make sure that
            // schemas for these tables (and no others) have been created
            for (String s : tableNames) {
                dbs.getTableSchemaEH(s); // throw error if not found
            }
            for (TableSchema ts : dbs.getTableSchemas()) {
                if (!tableNames.contains(ts.getName())) {
                    throw Error.toss(Error.tableNoExist, ts.getName(), dbs.getName());
                }
            }
        } catch (IOException e) {
            throw Error.toss(Error.ioerror, filename, e.getMessage());
        }
        return dbs;

        // finally, depending on who is calling this, the flatten attribute is to be set.
    }

    // ACCESSORS AND INFORMATION
    /**
     * return the SubTableSchema of the schema
     *
     * @return the SubTableSchema of the schema
     */
    public String getName() {
        return name;
    }

    /**
     * full (file) SubTableSchema of a schema is (SubTableSchema).schema.pl
     *
     * @return manufactured file SubTableSchema of schema
     */
    public String getFullName() {
        return name + ".schema.pl";
    }

    /**
     * return the TableSchema with SubTableSchema tableName
     *
     * @param tableName of schema to return
     * @return the TableSchema with SubTableSchema tableName, null if not found
     */
    public TableSchema getTableSchema(String tableName) {
        for (TableSchema ts : tableSchemas) {
            if (ts.getName().equals(tableName)) {
                return ts;
            }
        }
        return null;
    }

    /**
     * return the TableSchema with SubTableSchema tableName with built-in error
     * handling
     *
     * @param tableName of schema to return
     * @return the TableSchema with SubTableSchema tableName, throw Error if not
     * found
     */
    public TableSchema getTableSchemaEH(String tableName) {
        TableSchema ts = getTableSchema(tableName);
        if (ts == null) {
            throw Error.toss(Error.tableNoExist, tableName, name);
        }
        return ts;
    }

    /**
     * return list of TableSchemas of this database schema
     *
     * @return list of TableSchemas of this database schema
     */
    public List<TableSchema> getTableSchemas() {
        return tableSchemas;
    }

    /**
     * return list of SubTableSchemas of this database schema
     *
     * @return list of SubTableSchemas of this database schema
     */
    public List<SubTableSchema> getSubTableSchemas() {
        return subtables;
    }

    /**
     * returns subtable object for given tableName, null if none exists
     *
     * @param tableName -- SubTableSchema of table whose subtables are to be
     * returned
     * @return subtable object for given tableName
     */
    public SubTableSchema getSubTableSchema(String tableName) {
        for (SubTableSchema s : subtables) {
            if (s.getName().equals(tableName)) {
                return s;
            }
        }
        return null;
    }

    /**
     * returns subtable object for given tableName; throws Error if no such
     * tableName exists
     *
     * @param tableName -- SubTableSchema of table whose subtables are to be
     * returned
     * @return subtable object for given tableName
     */
    public SubTableSchema getSubTableSchemaEH(String tableName) {
        SubTableSchema result = getSubTableSchema(tableName);
        if (result == null) {
            throw Error.toss(Error.tableNoExist, tableName, name);
        }
        return result;
    }

    /**
     * returns subtable object for given tableSchema ts, null if none exists
     *
     * @param ts -- TableSchema object for which subtable declaration is to be
     * returned
     * @return subtable object for given tableSchema ts
     */
    public SubTableSchema getSubTableSchema(TableSchema ts) {
        return getSubTableSchema(ts.getName());
    }

    /**
     * returns subtable object for given tableSchema ts; throws Error if no such
     * ts exists
     *
     * @param ts -- SubTableSchema of tableSchema whose subtables are to be
     * returned
     * @return subtable object for given tableSchema ts
     */
    public SubTableSchema getSubTableSchemaEH(TableSchema ts) {
        SubTableSchema result = getSubTableSchema(ts);
        if (result == null) {
            throw Error.toss(Error.tableNoExist, ts.getName(), name);
        }
        return result;
    }

    /**
     * return the TableSchema of the super/dbSchema table of TableSchema s
     *
     * @param s -- input TableSchema
     * @return TableSchema of super/dbSchema table
     */
    public TableSchema getSuperTable(TableSchema s) {
        for (SubTableSchema sub : subtables) {
            for (TableSchema ts : sub.getSubTableSchemas()) {
                if (ts.getName().equals(s.getName())) {
                    return sub.getSuper();
                }
            }
        }
        return null;
    }

    /**
     *
     * @return true if the schema is no longer changeable (and that its
     * supertable attributes have been propagated to its subtables.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * sets DB Schema ready for instantiation; flattens the schema if not
     * already flattened.
     */
    public void finishAndPropagateAttributes() {
        if (!subtables.isEmpty()) {
            flatten();
        }
        finishedAndIPropagatedAttributesMyself();
    }

    /**
     * you finished a schema where you swear that you propagated all attributes
     * in a table hierarchy.
     */
    public void finishedAndIPropagatedAttributesMyself() {
        finished = true;
        // now make sure that all tables have at least one attribute
        // it is possible in an inheritance hierarchy that a table
        // with no attributes may be declared a subtable of one that has
        // attributes
        for (TableSchema t : getTableSchemas()) {
            if (t.size() == 0) {
                throw Error.toss(Error.tableHasNoColumns, t.getName(), getName());
            }
        }
    }

    // UTILITIES
    /**
     * return number of TableSchemas (equivalently the number of Tables in a
     * schema instance) in this dbschema
     *
     * @return number of TableSchemas in this db schema
     */
    public int size() {
        return tableSchemas.size();
    }

    /**
     *
     * @param list
     * @return
     */
    private SubTableSchema dominant(LinkedList<SubTableSchema> list) {
        boolean foundit = true;
        SubTableSchema last = null;

        for (SubTableSchema outer : list) {
            last = outer;
            for (SubTableSchema inner : list) {
                if (inner.contains(outer.getSuper())) {
                    foundit = false;
                    break;
                }
            }
            if (foundit) {
                return outer;
            }
            foundit = true;
        }
        throw Error.toss(Error.dbSchemaInheritanceCycle, last.getName());
    }

    /**
     * add table schema tableschema to 'this' database schema
     *
     * @param tableschema to add to 'this' database schema
     */
    public void addTableSchema(TableSchema tableschema) {
        String tableSchemaName = tableschema.getName();
        if (finished) {
            throw Error.toss(Error.dbSchemaNoMoreTables, tableSchemaName, name);
        }
        if (getTableSchema(tableSchemaName) != null) {
            throw Error.toss(Error.dbSchemaMultipleTableDecls, tableSchemaName);
        }
        tableSchemas.add(tableschema);
        tableschema.dbSchema = this;
    }

    /**
     * for adding schemas in bulk
     *
     * @param schemas -- list of table schemas
     */
    public void addTableSchemas(TableSchema... schemas) {
        for (TableSchema s : schemas) {
            addTableSchema(s);
        }
    }

    /**
     * add subtable schema sts to 'this' database schema
     *
     * @param sts subtableschema to add to 'this' database
     */
    public void addSubTableSchema(SubTableSchema sts) {
        String tableName = sts.getName();
        if (finished) {
            throw Error.toss(Error.dbSchemaNoMoreSubTables, name);
        }
        if (getSubTableSchema(tableName) != null) {
            throw Error.toss(Error.dbSchemaMultipleSubTableDecls, tableName);
        }
        subtables.add(sts);
        sts.dbSchema = this;
    }

    /**
     * add subtableschema in bulk
     *
     * @param sts -- an array of subtable schemas
     */
    public void addSubTableSchemas(SubTableSchema... sts) {
        for (SubTableSchema s : sts) {
            addSubTableSchema(s);
        }
    }

    // UTILITIES
    /**
     * two DBSchemas are equal iff (a) they have the same SubTableSchema, (b)
     * they have the same set of table schemas, and (c) they have the same set
     * of subtableschemas; if so true is returned.
     *
     * @param dbschema -- the other dbschema
     * @return true if both DBSchemas are equal.
     */
    public boolean equals(DBSchema dbschema) {
        if (!dbschema.getName().equals(getName())) {
            return false;   // schema names are not the same
        }
        if (dbschema.size() != size()) {
            return false;  // # of table definitions are not the same
        }
        for (TableSchema s : dbschema.getTableSchemas()) {
            TableSchema r = getTableSchema(s.getName());
            if (r == null || !r.equals(s)) {
                return false; // tables don't match
            }
        }

        if (subtables.size() != dbschema.subtables.size()) {
            return false;
        }

        for (SubTableSchema sts : dbschema.subtables) {
            SubTableSchema s2 = getSubTableSchema(sts.getName());
            if (!sts.equals(s2)) {
                return false; // have different subtable entries
            }
        }
        return true;  // they're the same
    }

    /**
     * two DBSchemas are equal iff (a) they have the same SubTableSchema, (b)
     * they have the same set of table schemas, and (c) they have the same set
     * of subtable schemas; if so this method returns. Otherwise a Error is
     * thrown
     *
     * @param dbschema.
     */
    public void equalsEH(DBSchema dbschema) {
        if (!dbschema.getName().equals(getName())) {
            throw Error.toss(Error.tableSchemaNamesDifferent, dbschema.getName(), getName());
        }
        if (dbschema.size() != size()) {
            throw Error.toss(Error.numberTablesDiffer, dbschema.getName(), getName());
        }
        for (TableSchema s : dbschema.getTableSchemas()) {
            TableSchema r = getTableSchema(s.getName());
            if (r == null) {
                throw Error.toss(Error.tableNoExist, s.getName(), dbschema.getName());
            }
            r.equalsEH(s);
        }

        if (subtables.size() != dbschema.subtables.size()) {
            throw Error.toss(Error.numberSubTablesDiffer, dbschema.getName(), getName());
        }

        for (SubTableSchema sts : dbschema.subtables) {
            SubTableSchema s2 = getSubTableSchema(sts.getName());
            sts.equalsEH(s2);
        }
    }

    /**
     * rename DBschema
     *
     * @param name new DBSchema SubTableSchema
     */
    public void rename(String name) {
        this.name = name;
    }

    /**
     * propagate attributes of superTables to subTables (operation is performed
     * on schema defs)
     */
    private void flatten() {
        // Step 1: must order subtable list by dominance 
        //         (dbSchema dominates a child)
        LinkedList<SubTableSchema> list = subtables;
        subtables = new LinkedList<>();
        while (!list.isEmpty()) {
            SubTableSchema next = dominant(list);
            list.remove(next);
            subtables.add(next);
        }

        // Step 2: propagate dbSchema attributes to subattributes
        for (SubTableSchema st : subtables) {
            st.flatten();
        }
    }

    // PRINT
    String printDBaseDecl() {
        String db = "dbase(" + name + ",[";
        String comma = "";
        for (TableSchema ts : tableSchemas) {
            db = db + comma + ts.getName();
            comma = ",";
        }
        return db + "]).\n";
    }

    /**
     * converts DBSchema to a string. Works for ooschema and schema files
     *
     * @return print string of DBSchema
     */
    public String toString() {
        String result = String.format("%s\n", printDBaseDecl());
        for (TableSchema ts : tableSchemas) {
            result = result + ts.toString();
        }
        if (!subtables.isEmpty()) {
            result = result + "\n";
            for (SubTableSchema st : subtables) {
                result = result + st.toString();
            }
        }
        return result;
    }

    /**
     * print the database schema definition to PrintStream out; typically used
     * for demonstrations as it doesn't check the format of the filename; Use
     * print() or print(String).
     *
     * @param out PrintStream destination
     */
    public void print(PrintStream out) {
        out.print(this.toString());
    }

    /**
     * prints to System.out
     */
    public void print() {
        print(System.out);
    }

    /**
     * print DBSchema in file with filename
     *
     * @param filename -- name of file in which to print DBSchema
     */
    public void print(String filename) {
        String schemaName = getName();
        if (finished && filename.endsWith("." + schemaName + ".pl")) {
            throw Error.toss(Error.wrongSchemaNameFormat, filename);
        }
        if (!finished && filename.endsWith(".ooschema.pl")) {
            throw Error.toss(Error.wrongOOSchemaNameFormat, filename);
        }
        try {
            PrintStream ps = new PrintStream(filename);
            print(ps);
        } catch (IOException ex) {
            throw Error.toss(Error.ioerror, filename, ex.getMessage());
        }
    }
}
