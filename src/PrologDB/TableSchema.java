package PrologDB;

import MDELite.Error;
import MDELite.ParsE;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import static Parsing.Parsers.DBLineParsers.parseTableDecl;
import Parsing.Parsers.LineToParse;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * a TableSchema defines the name of a table and its columns
 */
public class TableSchema {

    /**
     * schemaName of table schema
     */
    private final String name;

    /**
     * list of its columns
     */
    private final List<Column> columns;

    /**
     * DBSchema to which this table belongs. if null, tableSchema is not part of
     * any DBSChema
     */
    DBSchema dbSchema = null;

    // ACCESSORS AND INFORMATION
    /**
     * @return name of this tableSchema
     */
    public String getName() {
        return name;
    }

    /**
     * returns the ith Column definition of the table; throws RuntimeError if i
     * is out of bounds. Should not be exported from this package, but necessary
     * for parser (sigh)
     *
     * @param i column index
     *
     * @return returns the ith Column definition of 'this' table
     */
    public Column getColumn(int i) {
        return columns.get(i);
    }

    /**
     *
     * @return string name of ID field, the first coluumn
     */
    public String getIDName() {
        return getColumn(0).getName();
    }

    /**
     * returns list of columns of a TableSchema
     *
     * @return list of columns of 'this' tableSchema
     */
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * quickLookup is a cache for verifying legitimate field names quickly
     */
    private HashSet<String> quickLookup = new HashSet<>();

    /**
     * does schema have a column with columnName?
     *
     * @param columnName -- column name to verify existence
     */
    public void hasColumnEH(String columnName) {
        if (quickLookup.contains(columnName)) {
            return;
        }
        for (Column c : columns) {
            if (c.getName().equals(columnName)) {
                quickLookup.add(columnName);
                return;
            }
        }
        throw Error.toss(Error.tableUnknownColumn, columnName, name);
    }

    /**
     * does schema NOT have a column with columnName?
     *
     * @param columnName -- column name to verify nonexistence
     */
    public void doesNotHaveColumnEH(String columnName) {
        if (!quickLookup.contains(columnName)) {
            return;
        }
        for (Column c : columns) {
            if (c.getName().equals(columnName)) {
                throw Error.toss(Error.tableDuplicateColumn, columnName, getName());
            }
        }
    }

    /**
     * returns number of columns presently in 'this' TableSchema
     *
     * @return number of columns presently in 'this' TableSchema
     */
    public int size() {
        return columns.size();
    }

    /**
     * @return true if this schema is not attached to a DBSchema
     */
    public boolean isDetached() {
        return dbSchema != null;
    }

    // CONSTRUCTORS
    /**
     * create table schema with given schemaName and an empty list of columns
     *
     * @param name of new TableSchema
     */
    public TableSchema(String name) {
        this.name = name; // makeFirstLowerCase(name);
        columns = new ArrayList<>();
        dbSchema = null;    // set by DBSchema.addTableSchema()
    }

    /**
     * create table schema with given schemaName and given set of columns
     *
     * @param name of new TableScheam
     * @param cols is list of column declarations
     */
    public TableSchema(String name, List<Column> cols) {
        this.name = name;
        columns = new ArrayList<>();
        columns.addAll(cols);
        dbSchema = null;    // set by DBSchema.addTableSchema()
    }

    /**
     * create table schema with given schemaName and given set of columns
     *
     * @param name of new TableScheam
     * @param cnames is list of column names,
     */
    public TableSchema(String name, String... cnames) {
        this.name = name;
        columns = new ArrayList<>();
        for (String c : cnames) {
            this.addColumn(c);
        }
        dbSchema = null;    // set by DBSchema.addTableSchema()
    }

    /**
     * copies table schema (this) for self-joins. The difference between a copy
     * and the original the newTableName. Names of columns are unchanged
     *
     * @param newTableName -- string to newTableName to give a different name to
     * copy
     * @return copied and renamed table schema
     */
    public TableSchema CopyForSelfJoins(String newTableName) {
        TableSchema dup = new TableSchema(newTableName, columns);
        return dup;
    }

    /**
     * reads schema from File schemafileName that contains a single table
     * declaration; ParseExceptions and Errors are thrown
     *
     * read only .schema and database files
     *
     * @param schemafileName name of the file that contains a single Table
     * declaration
     * @return TableSchema of that file
     */
    public static TableSchema readSchema(String schemafileName) {
        File s = new File(schemafileName);
        return readSchema(s);
    }

    /**
     * reads prolog schema from File schemafile; errors are reported to out
     *
     * read only .schema and database files
     *
     * @param schemafile Java File of schema file to read (can also be a
     * database file, as it too embeds schema information)
     * @return DBSchema object
     */
    public static TableSchema readSchema(File schemafile) {
        // Step 1: simple error checking
        String filename = schemafile.getAbsolutePath().replace("\\", "/");
        if (filename.contains(".ooschema.")) {
            throw Error.toss(Error.ooschemaFile, filename);
        }
        
        // Step 2: now that we have the file name, create a LineNumberReader
        // for it
        LineNumberReader br;
        try {
            br = new LineNumberReader(new InputStreamReader(new FileInputStream(filename)));
        } catch (FileNotFoundException ex) {
            throw Error.toss(Error.tableFileNoExist, filename);
        }
        return readSchema(filename, br);
    }

    /** used when it is easier to pass in a string declaration of a TableSchema
     * rather than building it programmatically
     * @param nameOfSchema -- name of pseudo file
     * @param stringDefOfSchema -- string (multi line typically) contents of pseudo file
     * @return DBSchema -- of pseudo file
     */
    public static TableSchema readSchema(String nameOfSchema, String stringDefOfSchema) {
        InputStream is = new ByteArrayInputStream(stringDefOfSchema.getBytes());
        LineNumberReader br = new LineNumberReader(new InputStreamReader(is));
        return readSchema(nameOfSchema, br);
    }
    
    private static TableSchema readSchema(String filename, LineNumberReader br) {
        TableSchema ts = null;
        boolean parsedTable = false;
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

                if (line.startsWith("table")) {
                    if (parsedTable) {
                        throw ParsE.toss(ParsE.tableOnly1Allowed, lineno, filename);
                    }
                    LineToParse l = new LineToParse(line, lineno);
                    ts = parseTableDecl(l);
                }
                // ignore everything else
            }
            br.close();

        } catch (IOException e) {
            throw Error.toss(Error.ioerror, filename, e.getMessage());
        }
        if (ts == null) {
            throw Error.toss(Error.tableSchemaNothingToRead, filename);
        }
        return ts;
    }

    /**
     * form the cross product of 'this' schema with otherSchema; this is a
     * concatenation of the column lists and a manufactured table name
     * plus the addition of an id field as first attribute
     *
     * @param otherSchema -- to form cross product
     * @return composite schema
     */
    public TableSchema crossProduct(TableSchema otherSchema) {
        TableSchema newts = new TableSchema(name + "_x_" + otherSchema.getName());
        newts.addKols(this);
        newts.addKols(otherSchema);
        return newts;
    }

    /**
     * add the columns of ts to "this" tableschema. The names of the column go
     * from x to <ts.getName()>_x
     *
     * @param newts
     * @param ts
     */
    private void addKols(TableSchema ts) {
        String namePrefix;

        String schemaName = ts.getName();
        namePrefix = (schemaName.contains("_x_")) ? "" : schemaName + ".";

        for (Column c : ts.getColumns()) {
            String newColumnName = namePrefix + c.getName();
            this.addColumn(new Column(c, newColumnName));
        }
    }

    // CONSTRUCTOR UTILITIES
    /**
     * add a new column c to 'this' TableSchema; new column is appended to the
     * existing list of columns
     *
     * Error is thrown if columns with duplicate names are detected
     *
     * @param c is column to add
     * @return updated tableschema
     */
    public TableSchema addColumn(Column c) {
        doesNotHaveColumnEH(c.getName());
        columns.add(c); // append
        return this;
    }

    /**
     * add column c as the first of 'this' schema
     *
     * @param c column to add
     * @return updated tableschema
     */
    public TableSchema addColumnFirst(Column c) {
        doesNotHaveColumnEH(c.getName());
        columns.add(0, c); // put at front
        return this;
    }

    /**
     * adds a column whose name/type is encoded by k
     *
     * @param k -- either an identifier or 'identifier'
     * @return updated tableschema
     */
    public TableSchema addColumn(String k) {
        return addColumn(new Column(k));
    }

    /**
     * adds a column to the head of the list of columns
     *
     * @param k -- either an identifier or 'identifier'
     * @return updated tableschema
     */
    public TableSchema addColumnFirst(String k) {
        return addColumnFirst(new Column(k));
    }

    /**
     * adds a sequence of columns
     *
     * @param columns -- sequence of column-encoded names
     * @return updated tableschema
     */
    public TableSchema addColumns(String... columns) {
        for (String k : columns) {
            addColumn(k);
        }
        return this;
    }

    /**
     * add list of columns to the head of a schema
     *
     * @param columns -- encoded names of columns to add
     * @return updated tableschema
     */
    public TableSchema addColumnsFirst(String... columns) {
        Collections.reverse(Arrays.asList(columns));
        for (String k : columns) {
            addColumnFirst(k);
        }
        return this;
    }

    /**
     * place an array of columns before existing columns of 'this' schema
     *
     * @param columns is array of columns to add
     * @return updated tableschema
     */
    public TableSchema addColumnsFirst(Column... columns) {
        // reverse the order of columns to get the correct insertion order
        Collections.reverse(Arrays.asList(columns));
        for (Column k : columns) {
            addColumnFirst(k);
        }
        return this;
    }

    // UTILITIES
    /**
     * asserts if 'this' schema == otherSchema If false, Error is thrown
     * equality is based on the same number and set of columns
     *
     * @param otherSchema -- the schema to compare to
     */
    public void equalsEH(TableSchema otherSchema) {
        if (otherSchema == null) {
            throw Error.toss(Error.tableSchemasNotTheSame,getName(),"null");
        }
//        if (!name.equals(otherSchema.getName())) {
//            throw Error.toss(Error.tablesHaveDifferentNames, getName(), otherSchema.getName());
//        }
        if (columns.size() != otherSchema.columns.size()) {
            throw Error.toss(Error.numberColumnsDiffer, getName());
        }
        for (Column c : columns) {
            Column k = otherSchema.getColumn(c.getName());
            if (!c.equals(k)) {
                throw Error.toss(Error.tableSchemaNoShareColumn, getName(), c.getName());
            }
        }
    }

    /**
     * returns true if 'this' schema equals otherSchema, false otherwise
     * equality is based on the same number and sequence of columns
     * @param otherSchema -- other schema to compare
     *
     * @return true if both schemas are equal
     */
    public boolean equals(TableSchema otherSchema) {
        if (otherSchema == null) {
            return false;
        }
        if (columns.size() != otherSchema.columns.size()) {
            return false; // don't have the same # of columns
        }
        if (!name.equals(otherSchema.getName())) {
            return false; // dont have the same schema names
        }
        for (Column c : columns) {
            Column k = otherSchema.getColumn(c.getName());
            if (!c.equals(k)) {
                return false;  // no corresponding attribute
            }
        }
        return true;  // they match
    }

    // GET UTILITIES
    /**
     * @param name of column to findLocal
     * @return column with schemaName; Error thrown if not found
     */
    public Column getColumnEH(String name) {
        Column c = TableSchema.this.getColumn(name);
        if (c == null) {
            throw Error.toss(Error.tableUnknownColumn, name, this.name);
        } else {
            return c;
        }
    }

    /**
     *
     * @param name of column
     * @return column with schemaName; null if not found
     */
    public Column getColumn(String name) {
        for (Column c : columns) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    // PRINT 
    /**
     * returns string declaration of a TableSchema
     *
     * @return string declaration of a TableSchema
     */
    public String toString() {
        String q, kind;
        String tb = "table(" + name + ",[";
        String comma = "";
        for (Column c : columns) {
            tb = tb + comma + c;
            comma = ",";
        }
        tb = tb + "]).\n";
        return tb;
    }

    /**
     * print 'this' TableSchema to PrintStream out
     *
     * @param out where to print
     */
    public void print(PrintStream out) {
        out.print(this);
    }

    /** print to System.out
     */
    public void print() { 
        print(System.out);
    }
    
    private static String makeFirstLowerCase(String s) {
        String tail = s.substring(1);
        String head = s.substring(0, 1).toLowerCase();
        return head + tail;
    }

    /**
     * gets supertable of table schema s; throws Error if 'this' table schema
     * not part of a database
     *
     * @return schema of supertable of 'this'
     */
    public TableSchema getSuperTableOf() {
        DBSchema dbs = dbSchema;
        if (dbs == null) {
            throw Error.toss(Error.tableNotInDataBase, getName());
        }
        return dbs.getSuperTable(this);
    }

    /**
     * is 'this' schema a supertable (ancestor) of table schema s?
     *
     * @param s -- table schema
     * @return true if table schema s is an ancestor of 'this'
     */
    public boolean isSuperTableOf(TableSchema s) {
        TableSchema ts = s;
        while (ts != null) {
            if (this == ts) {
                return true;
            }
            ts = ts.getSuperTableOf();
        }
        return false;
    }
}
