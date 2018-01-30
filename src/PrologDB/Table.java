package PrologDB;

import MDELite.Error;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * a Table is an instance of a TableSchema. Both share the same names.
 */
public class Table {

    /**
     * each table points to its tablesSchema "schema"
     */
    protected final TableSchema schema;

    /**
     * each table has a TupleList of "tuples"
     */
    private TupleList tuples;

    /**
     * each table has a list of its subtables; this list is initialized by
     * Table.getSubTables upon first reference
     */
    public LinkedList<Table> subtables;  // initialized by Table.getSubTables()

    /**
     * each table points to its DBSchema "dbSchema". Normally, the value of this
     * field is set by a call to a DB.constructor. However, tables that are
     * created that do not belong to a DBSchema have this field null.
     */
    DB parent = null;

    // CONSTRUCTORS
    /** create an empty table for TableSchema schema
     * @param schema -- the schema to which the table should conform
     */
    public Table(TableSchema schema) {
        this(schema, new TupleList());
    }
    
    /** create an empty table using the schema of the given table
     * @param table -- the schema to which the table should conform
     */
    public Table(Table table) {
        this(table.getSchema(), new TupleList());
    }
    
    /** create an empty table using the schema of the given table
     * @param table -- the schema to which the table should conform
     * @param tl -- tuple list of table
     */
    public Table(Table table, TupleList tl) {
        this(table.schema,tl);
    }
    
    /** most primitive table constructor
     * @param schema -- of table
     * @param tl -- tuplelist of table
     */
    public Table(TableSchema schema, TupleList tl) {
        this.schema = schema;
        tuples = tl;
        subtables = null;
    }
    
    /**
     * this private constructor is used for self joins, where tuples are copied
     * from one schema to another.
     *
     * Precondition: schemas of TupleList tab and schema have identically
     * defined columns. Names of schemas could be different.
     *
     * @param schema -- schema of table to create
     * @param tab -- table tuples to copy into the new table
     */
    private Table(TableSchema schema, Table tab) {
        if (schema.isDetached()) {
            this.schema = schema;
        } else {
            this.schema = schema.CopyForSelfJoins(schema.getName());
        }
        tuples = tab.tuples.copy(schema);
        subtables = null;
    }

    /**
     * this method copies a table t for use in self-joins; the original table t
     * is copied and renamed to copyTableName
     *
     * @param copyTableName -- postfix SubTableSchema distinguisher
     * @return copied table
     */
    public Table copyForSelfJoins(String copyTableName) {
        TableSchema renamedSchema = this.schema.CopyForSelfJoins(copyTableName);
        Table tnew = new Table(renamedSchema, this);
        return tnew;
    }

    /**
     * reads and parses a prolog table from given string filename. may throw
     * parseExceptions or Errors, which terminates processing. Errors are
     * reported to PrintStream out.
     *
     * @param tableFilePath is the filename of the Prolog database
     * @return Table of tableFilePath
     */
    public static Table readTable(String tableFilePath) {
        if (tableFilePath.endsWith(".csv")) {
            return readCSVTable(tableFilePath);
        }

        LineNumberReader br;
        File infile;
        if (!tableFilePath.endsWith(".pl")) {
            throw Error.toss(Error.wrongFileNameFormatShort, tableFilePath);
        }

        // read the file in the first pass: get its schema
        try {
            infile = new File(tableFilePath);
        } catch (Exception e) {
            throw Error.toss(Error.ioerror, tableFilePath, e.getMessage());
        }
        TableSchema ts = TableSchema.readSchema(infile);

        // second pass - read the table itself
        Table t = new Table(ts);
        String line;
        try {
            // Step 1: Read infile by line
            br = new LineNumberReader(new InputStreamReader(new FileInputStream(infile)));

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("") || line.startsWith("table(") 
                        || line.startsWith("%")
                        || line.startsWith("/*") 
                        || line.startsWith("//") ) {
                    continue;
                }

                Tuple tuple = Tuple.parseTupleDecl(line, br.getLineNumber(), ts); // if not tuple line, ignore
                t.add(tuple);
            }
            br.close();
        } catch (IOException e) {
            throw Error.toss(Error.ioerror, infile.getName(), e.getMessage());
        }
        return t;
    }

    /**
     * read a csv file whose path-SubTableSchema is tablePathName 1st line of
     * this file defines the table schema attributes (all are assumed "quoted")
     * lines 2-$ of this fine each define a tuple of this table returns Table of
     * csv file. IMPORTANT NOTE: the following translation occurs in converting
     * csv files: "TRUE"->"true", "FALSE"->"false" for column values
     *
     * @param tablePathName -- pathname to csv file
     * @return Table of the read csv file
     */
    private static Table readCSVTable(String tablePathName) {
        TableSchema ts;
        Table table = null;
        LineNumberReader br = null;
        try {
            // Step 1: read the first line of the csv file and interpret it as a
            //         schema declaration
            String name = getCSVName(tablePathName);
            File schemafile = new File(tablePathName);
            br = new LineNumberReader(new InputStreamReader(new FileInputStream(schemafile)));
            String line = br.readLine();
            String[] splt = line.split(",");
            ts = new TableSchema(name);
            for (String i : splt) {
                // all attributes are quoted -- hence additon of "'"
                ts.addColumn(new Column("'"+i.trim())); 
            }

            // Step 2: subsequent lines define a single tuple, which is
            //         added to the newly created table "table"
            table = new Table(ts);
            while ((line = br.readLine()) != null) {
                line = line + " ,";
                String[] spl = line.split(",");
                for (int i = 0; i < spl.length; i++) {
                    spl[i] = TFSwap(spl[i].trim());
                }
                Tuple tuple = new Tuple(ts);
                tuple.setValues(spl);
                table.add(tuple);
            }
            br.close();
        } catch (IOException e) {
            String lnum = "";
            if (br != null) {
                lnum = "" + br.getLineNumber();
            }
            throw Error.toss(Error.ioerror, tablePathName, e.getMessage() + " on line " + lnum);
        } catch (RuntimeException e) {
            String lnum = "";
            if (br != null) {
                lnum = "" + br.getLineNumber();
            }
            throw Error.toss(Error.csvReadError, tablePathName, e.getMessage(), lnum);
        }
        return table;
    }

    /**
     * replace TRUE->true, FALSE->false
     */
    private static String TFSwap(String s) {
        String ss = s.toLowerCase();
        if (ss.equals("true")) {
            return "true";
        }
        if (ss.equals("false")) {
            return "false";
        }
        return s;
    }

    // RELATIONAL OPERATIONS
    /**
     * transform a table to an object of type S
     *
     * @param <S> -- return type, should not be Table or subclass of Table
     * @param f -- a function that maps a Table to S
     * @return -- object of type S
     */
    public <S> S map(Function<Table, S> f) {
        return f.apply(this);
    }

    /** works like forEach stream, except it takes in a table
     *
     * @param action -- to be performed
     */
    public void forEach(Consumer<Tuple> action) {
        if (count()==0)
            return;
        this.stream().forEach(t -> action.accept(t));
    }
    
    /** at least one tuple in 'this' table must satisfy pred
     * 
     * @param pred -- predicate to satisfy
     * @return true if at least one tuple in 'this' tables satisfies pred
     */
    public boolean anyMatch(Predicate<Tuple> pred) {
        return this.stream().anyMatch(pred);
    }
    
    /** all tuples in 'this' table must satisfy pred
     * 
     * @param pred -- predicate to satisfy
     * @return true if all tuples in 'this' table satisfy pred
     */
    public boolean allMatch(Predicate<Tuple> pred) {
        return this.stream().allMatch(pred);
    }

    /**
     * return a Table from a given Table that satisfies predicate p
     *
     * @param p -- predicate to satisfy
     * @return predicate reduced TupleList
     */
    public Table filter(Predicate<Tuple> p) {
        // Step 1: create empty table with same schema as t
        Table shorterTable = new Table(schema);

        // Step 2: dup over every tuple in t that satisfies p
        this.stream().filter(p).forEach(tt -> shorterTable.add(tt));

        // Step 3: output row-reduced table
        return shorterTable;
    }

    /**
     * does 'this' table have a tuple with field = value
     *
     * @param field -- SubTableSchema of field
     * @param value -- value of field
     * @return true if there exists a tuple with field=value
     */
    public boolean exists(String field, String value) {
        for (Tuple t : this.tuples()) {
            if (t.is(field, value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * does 'this' table have a tuple that satisfies the given predicate
     *
     * @param p -- predicate over tuple
     * @return true if there exists a tuple with field=value
     */
    public boolean exists(Predicate<Tuple> p) {
        return this.tuples().stream().anyMatch((t) -> (p.test(t)));
    }

    /**
     * projects a Table to the subset of columns of a sub table schema. if given
     * superTableSchema does not match any tuple in the list an Error is thrown
     *
     * @param subTableSchema -- schema of subset of columns to project to
     * @return column-projected Table
     */
    public Table project(TableSchema subTableSchema) {
        Table shorterTable = new Table(subTableSchema);
        tuples.stream().forEach(t -> shorterTable.add(t.project(subTableSchema)));
        return shorterTable;
    }

    /**
     * project Table to a list of cols
     *
     * @param cols - List of columns to retain
     * @return projected Table
     */
    public Table project(List<Column> cols) {
        TableSchema newSchema = new TableSchema(schema.getName(), cols);
        Table shorterTable = new Table(newSchema);
        for (Tuple o : tuples()) { ///
            Tuple tuple = new Tuple(newSchema);

            for (Column c : cols) {
                String name = c.getName();
                String value = o.get(name);
                tuple.set(name, value);
            }
            shorterTable.add(tuple);
        }
        return shorterTable;
    }

    /**
     * project Table to an array of column names. precondition: each
     * SubTableSchema must correspond to an existing column in schema
     *
     * @param colNames - List of column names to retain
     * @return projected TupleList
     */
    public Table project(String... colNames) {
        TableSchema newts = new TableSchema(schema.getName());
        for (String c : colNames) {
            Column k = schema.getColumnEH(c);
            newts.addColumn(k);
        }

        Table shorterTable = new Table(newts);
        for (Tuple o : tuples()) {  ///
            Tuple tuple = new Tuple(newts);

            for (String name : colNames) {
                String value = o.get(name);
                tuple.set(name, value);
            }
            shorterTable.add(tuple);
        }
        return shorterTable;
    }

    /**
     * remove from (this) Table the list of column names and their values.
     *
     * @param colNames - List of column names to remove
     * @return projected TupleList
     */
    public Table antiProject(String... colNames) {
        TableSchema newts = new TableSchema(schema.getName());
        List<String> rejects = Arrays.asList(colNames);
        for (Column c : schema.getColumns()) {
            if (rejects.contains(c.getName())) {
                continue;
            }
            newts.addColumn(c);
        }

        Table shorterTable = new Table(newts);
        for (Tuple o : tuples()) {  ///
            Tuple tuple = new Tuple(newts);

            for (Column c : newts.getColumns()) {
                String cname = c.getName();
                String value = o.get(cname);
                tuple.set(cname, value);
            }
            shorterTable.add(tuple);
        }
        return shorterTable;
    }

    /**
     * this is a table constructor that takes the equijoin of 'this' table on the
     * leftJoinCol with rightTable on the rightJoinCol. The crossproduct of both
     * schemas is formed, and the joined table is returned.
     *
     * @param leftJoinCol -- join key of inner table
     * @param rightTable -- outer table of join
     * @param rightJoinCol -- join key of outer table
     * @return equijoin of 'this' table and rightTable
     */
    public Table join(String leftJoinCol, Table rightTable, String rightJoinCol) {
        TableSchema is = getSchema();
        TableSchema os = rightTable.getSchema();
        TableSchema X = is.crossProduct(os);

        Table x = new Table(X);
        for (Tuple in : tuples()) { ///
            for (Tuple out : rightTable.tuples()) { ///
                if (in.is(leftJoinCol, out.get(rightJoinCol))) {
                    Tuple t = new Tuple(X);
                    t.setValues(in);
                    t.setValues(out);
                    x.add(t);
                }
            }
        }
        return x;
    }

    /**
     * takes the leftOuterJoin of 'this' table on the leftJoinCol with rightTable 
     * on the rightJoinCol. Every 'this' tuple is preserved; if there is no joining 
     * right tuple, nulls are added.
     *
     * @param leftJoinCol -- join column of inner table
     * @param rightTable -- outer table of join
     * @param rightJoinCol -- join column of outer table
     * @return leftouterjoin of 'this' table and and rightTable
     */
    public Table leftOuterJoin(String leftJoinCol, Table rightTable, String rightJoinCol) {
        TableSchema is = getSchema();
        TableSchema os = rightTable.getSchema();
        TableSchema X = is.crossProduct(os);

        Table x = new Table(X);
        for (Tuple left : tuples()) {
            boolean matched = false;
            for (Tuple right : rightTable.tuples()) {
                if (left.is(leftJoinCol, right.get(rightJoinCol))) {
                    Tuple t = new Tuple(X);
                    t.setValues(left);
                    t.setValues(right);
                    x.add(t);
                    matched = true;
                }
            }
            if (!matched) {
                Tuple t = new Tuple(X);
                t.setValues(left);
                t.setNulls(os);
                x.add(t);
            }
        }
        return x;
    }

    /**
     * this is a table constructor that takes the leftSemiJoin of 'this' table on the
     * leftJoinCol with rightTable on the right JoinKey. A table of type
     * 'this' is returned; its tuples are those that can join with tuples in
     * the rightTable.
     *
     * @param leftJoinCol -- join key of left table
     * @param rightTable -- inner table of join
     * @param rightJoinCol -- join key of inner table
     * @return a (proper) subset of the rows of 'this' table
     */
    public Table semiJoin(String leftJoinCol, Table rightTable, String rightJoinCol) {
        TableSchema LS = getSchema();
        Table left = new Table(LS);
        for (Tuple out : tuples()) {  ///
            for (Tuple in : rightTable.tuples()) {  ///
                if (in.is(rightJoinCol, out.get(leftJoinCol))) {
                    left.add(out);
                    break;
                }
            }
        }
        return left;
    }

    /**
     * this is a table constructor that takes the rightSemiJoin of 'this' table on
     * the leftJoinCol with rightTable on the right JoinKey. A table of type
     * rightTable is returned; its tuples are those that can join with tuples in
     * 'this' table.
     *
     * @param leftJoinCol -- join key of left table
     * @param rightTable -- inner table of join
     * @param rightJoinCol -- join key of inner table
     * @return a (proper) subset of the rows of the rightTable
     */
    public Table rightSemiJoin(String leftJoinCol, Table rightTable, String rightJoinCol) {
        TableSchema RS = rightTable.getSchema();
        Table right = new Table(RS);
        for (Tuple in : rightTable.tuples()) {  ///
            for (Tuple out : tuples()) {  ///
                if (in.is(rightJoinCol, out.get(leftJoinCol))) {
                    right.add(in);
                    break;
                }
            }
        }
        return right;
    }

    /**
     * this is a table constructor that takes the antiSemiJoin of 'this' table on
     * the leftJoinCol with rightTable on the right JoinKey. A table of type
     * 'this' table is returned; its tuples are those that can NOT join with the
     * rightTable.
     *
     * @param leftJoinCol -- join key of outer table
     * @param rightTable -- inner table of join
     * @param rightJoinCol -- join key of inner table
     * @return a (proper) subset of the rows of 'this' table
     */
    public Table antiSemiJoin(String leftJoinCol, Table rightTable, String rightJoinCol) {
        TableSchema LS = getSchema();
        Table left = new Table(LS);
        for (Tuple out : tuples()) {
            boolean matched = false;
            for (Tuple in : rightTable.tuples()) {
                if (in.is(rightJoinCol, out.get(leftJoinCol))) {
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                left.add(out);
            }
        }
        return left;
    }

    /** eliminates duplicate tuples from 'this' table producing new table
     * @return unique tuples of 'this' table -- removes duplicates
     */
    public Table unique() {
        Table unique = new Table(this);
        for (Tuple t : tuples()){
            if (!unique.contains(t))
                unique.add(t);
        }
        return unique;
    }
    
    /** returns table of duplicate tuples from 'this' table
     * @return returns the tuples of 'this' table that were duplicated
     */
    public Table duplicates() {
        Table dups = new Table(this);
        Table unique = new Table(this);
        for (Tuple t : tuples()){
            if (!unique.contains(t))
                unique.add(t);
            else if (!dups.contains(t)) {
                dups.add(t);
            }
        }
        return dups;
    }
    
    /** table error reporter
     * @param er -- ErrorReport object
     * @param format -- error formatting string
     * @param fun -- sequence of String funct(Tuple) functions to fill in format
     */
    public void error(ErrorReport er, String format, Function<Tuple,String>... fun){
        for (Tuple t: tuples()) {
            Object[] sargs = new String[fun.length];
            for (int i=0; i<fun.length; i++) {
                sargs[i] = fun[i].apply(t);
            }
            er.add(format, sargs);
        }
    }
    
    /**
     * dup Table by copying each tuple and returning the copied Table. Unchecked
     * Precondition: this.tableSchema must have an identical set of columns as
     * tableSchema. Only the names of their schemas may be different
     *
     * @param tableSchema -- table schema of the list
     * @return copied tuple list
     */
    public Table copy(TableSchema tableSchema) {
        Table dup = new Table(tableSchema);
        stream().forEach((t) -> {
            dup.add(t.copy(tableSchema));
        });
        return dup;
    }
  
    /** does 'this' table contain the same set of tuples as the given table?
     *  tables must be of the same schema
     * @param table -- to compare
     * @return true if 'this' table has all tuples of the given table
     */
    public boolean equals(Table table) {
        if (table.count() != count()) 
            return false;
        Table intr = this.intersect(table);
        return (intr.count()== count());
    }
    
    /** return intersection of two tables that have the same schema
     * @param tab -- other table, besides 'this'
     * @return table of tuples that are common (using ids).
     */
    public Table intersect(Table tab) {
        tab.schema.equalsEH(schema);
        Table result = new Table(schema);
        for (Tuple out : this.tuples()) {
            for (Tuple in : tab.tuples()) {
                if (out.getId().equals(in.getId())) {
                    result.add(out);
                }
            }
        }
        return result;
    }
    
    /**
     * sort 'this' Table on column with columnName
     *
     * @param columnName -- SubTableSchema of (string) column to sort
     * @param updateInputTable -- true if input table = output table
     * @return columnName sorted TupleList
     */
    public Table sort(String columnName, boolean updateInputTable) {
        schema.hasColumnEH(columnName);

        Comparator<Tuple> comp = (Tuple a, Tuple b) -> {
            return a.get(columnName).compareTo(b.get(columnName));
        };

        TupleList l = tuples();
        Collections.sort(l, comp);
        if (updateInputTable) {
            this.tuples = l;
            return this;
        }
        return new Table(schema,(TupleList) l);
    }
    
    /** group 'this' table into a stream of tables; each "sub"table will have
     * tuples with the same columnName value
     * @param columnName -- name of column to group by
     * @return stream of tables
     */
    public Stream<Table> groupBy(String columnName) {
        HashMap<String,Table> gb = new HashMap<>();
        this.forEach(t-> { 
            String key = t.get(columnName);
            Table tl = gb.get(key);
            if (tl == null) {
                tl = new Table(this);
                gb.put(key,tl);
            }
            tl.add(t);
        });
        Collection<Table> ctl = gb.values();
        return ctl.stream();
    }

    // CONSTRUCTOR UTILITIES
    /**
     * adds tuple t to 'this' table; throws error the schema of the tuple does
     * not match the schema of the table to which it is being added Untested
     * precondition: that the schema of the tuple conforms to the schema of the
     * table
     *
     * @param t is tuple to add
     * @return updated Table ('this');
     */
    public Table add(Tuple t) {
        TableSchema tschema = t.getSchema();
        if (!schema.isSuperTableOf(tschema)) {
            String tname = tschema.getName();
            String name = schema.getName();
            if (tname.equals(name)) {
                throw Error.toss(Error.wrongTableSameName, name);
            } else {
                throw Error.toss(Error.wrongTable, tname, name);
            }
        }
        tuples.add(t);
        return this;
    }

    /**
     * combination of t=new Tuple(schema);t.setValues(values); table.add(t);
     *
     * @param values -- of a tuple to insert
     * @return table that the tuple was inserted
     */
    public Table addTuple(String... values) {
        Tuple t = new Tuple(schema);
        t.setValues(values);
        add(t);
        return this;
    }
    
    /** add tuples of table tab to 'this' table
     * tab must have same set of columns (name,quote)
     * @param tab -- of tuples to add to 'this' table
     * @return updated table
     */
    public Table addTuples(Table tab) {
        this.schema.equalsEH(tab.getSchema());
        tab.forEach(t -> add(new Tuple(this).copy(t)));
        return this;
    }
    
    public Table addTuples(Table tab, ColumnCorrespondence cor) {
        tab.forEach((l) -> add(new Tuple(schema).copy(l, cor)));
        return this;
    }

    /**
     * finds first tuple with (identifier,value) pair and deletes the first
     *
     * @param identifier -- name of identifier column
     * @param value -- column value
     * @return -- true if tuple was deleted (generally ignored by users)
     */
    public boolean delete(String identifier, String value) {
        // Step 1: see if tuple is in this table; if so, remove it
        for (Tuple t : tuples) {  ///
            if (t.is(identifier, value)) {
                tuples.remove(t);
                return true;
            }
        }

        // Step 2: see if tuple is in a subtable; if so, remove it
        List<Table> tablist = getSubTables();
        for (Table tab : tablist) {
            if (tab.delete(identifier, value)) {
                return true;
            }
        }

        // Step 3: doesn't exist
        return false;
    }

    /**
     * finds first tuple with (identifier,value) pair and deletes the first
     * throws error if not found.
     *
     * @param identifier -- name of identifier column
     * @param value -- value of identifier
     */
    public void deleteEH(String identifier, String value) {
        if (delete(identifier, value)) {
            return;
        }
        throw Error.toss(Error.tableNoDeleteTuple, identifier, value, getName());
    }
    
    /** make 'this' table empty
     * 
     */
    public void deleteAll() {
        tuples = new TupleList();
    }
    
    /** does 'this' table contain tuple t?
     * @param t -- tuple
     * @return true if 'this' table contains t
     */
    public boolean contains(Tuple t) {
        for(Tuple s : this.tuples()) {
            if (s.equals(t))
                return true;
        }
        return false;
    }

    // ACCESSORS and INFORMATION
    /**
     * returns the DBSchema of this table. If there is no such DBSchema null is
     * returned.
     *
     * @return DBSchema of this table
     */
    public DBSchema getDBSchema() {
        return schema.dbSchema;
    }

    /**
     * returns the DBSchema of this table. if there is no DBSchema (i.e., it is
     * null) an Error is thrown
     *
     * @return DBSchema of this table
     */
    public DBSchema getDBSchemaEH() {
        if (schema.dbSchema == null) {
            throw Error.toss(Error.tableNotInDataBase, getName());
        }
        return schema.dbSchema;
    }

    /**
     *
     * returns SubTableSchema of table, which always equals the SubTableSchema
     * of its table schema
     *
     * @return SubTableSchema of table
     */
    public String getName() {
        return schema.getName();
    }

    /**
     *
     * returns schema of table
     *
     * @return TableSchema of 'this' table
     */
    public TableSchema getSchema() {
        return schema;
    }
    
    /** get identifier name of this table
     * @return identifier name of this table
     */
    public String getIDName() { 
        return schema.getIDName();
    }

    /**
     * returns list of columns of the table; delegates to getColumns of
     * TableSchema
     *
     * @return list of columns of this table
     */
    public List<Column> getColumns() {
        return schema.getColumns();
    }

    /**
     * return list of Tuples of this table and all of its subtables no
     * projection is done to ensure that all tuples have exactly and only the
     * columns of its parents.
     *
     * @return list of tuples of this table and all of its subtables
     */
    public TupleList tuples() {
        TupleList tl = new TupleList();

        if (parent == null) {
            // table that is computed, does not belong to a schema
            return tuplesLocal();
        }

        List<Table> tablist = getSubTables();
        for (Table t : tablist) {
            tl.addAll(t.tuplesLocal());
        }
        return tl;
    }

    /**
     * return only the tuples of this table, and none of its subtables Generally
     * not used; tuples() is preferred
     *
     * @return list of tuples of only 'this' table
     */
    public TupleList tuplesLocal() {
        return tuples;
    }

    /**
     * return first tuple in table that satisfies predicate p
     *
     * @param p - filter or query, usually over a key field
     * @return null if there is no such tuple, or the first tuple
     */
    public Tuple getFirst(Predicate<Tuple> p) {
        for (Tuple t : tuples()) { ///
            if (p.test(t)) {
                return t;
            }
        }
        return null;
    }

    /**
     * return first tuple in table that satisfies predicate p
     *
     * @param p - filter or query, usually over a key field
     * @return tuple ; throws error if not found
     */
    public Tuple getFirstEH(Predicate<Tuple> p) {
        Tuple t = getFirst(p);
        if (t == null) {
            throw Error.toss(Error.tableNoTupleFound, getName());
        }
        return t;
    }

    /**
     *
     * returns number of tuples in this table
     *
     * @return number of tuples in this table
     */
    public int count() {
        return tuples().size();  ///
    }

    /**
     * returns list of table + its subtables to search note: this method is
     * really executed once. By doing so, the "subtables" member of "this" is
     * set to the list of previously computed subtables.
     *
     * @return list of all tables for the given table, which includes the table
     * itself
     */
    public List<Table> getSubTables() {

        // Step 0: if we have computed this list before, return it
        if (subtables != null) {
            return subtables;
        }

        // Step 1: initialize list with root of inheritance hierarchy
        subtables = new LinkedList<>();
        subtables.add(this);

        // Step 2: find a subtable schema for this table schema
        //         if there is none, we're done
        SubTableSchema sts = parent.getSubTableSchema(schema);
        if (sts == null) {
            return subtables;
        }

        // Step 3: add the list of subtable names, recursively
        for (TableSchema ts : sts.getSubTableSchemas()) {
            Table t = parent.getTableEH(ts.getName());
            subtables.addAll(t.getSubTables());
        }
        return subtables;
    }

    /**
     * returns the SubTableSchema of a csv file, sans ".csv" and any directory
     * information linux example: "a/b/c.csv" returns "c" windoze example:
     * "a\b\c.csv" returns "c"
     *
     * @param pathName is the path-SubTableSchema to a csv file
     * @return cvs file SubTableSchema
     */
    public static String getCSVName(String pathName) {
        if (!pathName.endsWith(".csv")) {
            throw Error.toss(Error.wrongCSVNameFormat, pathName);
        }
        String name = pathName.replace(".csv", "");
        int slash = name.lastIndexOf('/');
        int bslash = name.lastIndexOf('\\');
        if (slash == -1 && bslash == -1) {
            return name;
        }
        if (slash != -1 && bslash != -1) {
            throw Error.toss(Error.wrongPathNameFormat, pathName);
        }
        if (slash != -1) {
            return name.substring(slash + 1);
        } else {
            return name.substring(bslash + 1);
        }
    }
    


    // PUBLIC UTILITIES AND PRINT
    
    /**
     * print tuples of this table only to PrintStream out used to print a
     * database
     *
     * @param out -- PrintStream to output print
     */
    public void printLocal(PrintStream out) {
        printEngine(out, tuples);
    }

    /**
     * print table (and all subtables) to PrintStream out
     *
     * @param out -- PrintStream to output print
     */
    public void print(PrintStream out) {
        printEngine(out, tuples());
    }
    
    /* print table (and all subtables) to System.out
     */
    public void print() {
        printEngine(System.out, tuples());
    }

    /**
     * engine for printing a table
     */
    private void printEngine(PrintStream out, TupleList l) {
        if (schema == null) {
            throw Error.toss(Error.tableHasNoSchema, getName());
        }
        schema.print(out);
        l.stream().forEach(t -> t.print(out));
        out.format("\n");
    }

    /**
     * general writeTable file utility -- if filename ends in ".csv" then the
     * table will be written in CSV format.
     *
     * @param tablePathName -- file path SubTableSchema
     */
    public void writeTable(String tablePathName) {
        try {
            PrintStream out = new PrintStream(tablePathName);
            if (tablePathName.endsWith(".csv")) {
                writeCSVTable(out);
            } else {
                print(out);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * writeTable table in CSV form to PrintStream out
     *
     * @param out -- PrintStream to writeTable CSV table
     */
    private void writeCSVTable(PrintStream out) {
        TableSchema ts = this.getSchema();
        String comma = "";
        for (Column c : ts.getColumns()) {
            out.format("%s%s", comma, c.getName());
            comma = ",";
        }
        out.format("\n");
        for (Tuple tup : tuples()) { ///
            comma = "";
            for (Column k : tup.getColumns()) {
                String val = tup.get(k.getName()).trim();
                out.format("%s%s", comma, val);
                comma = ",";
            }
            out.format("\n");
        }
    }

    /**
     * convert table into a stream of Tuples
     *
     * @return stream of table tuples
     */
    public Stream<Tuple> stream() {
        return tuples().stream();
    }
    
}
