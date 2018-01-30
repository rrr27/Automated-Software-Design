package PrologDB;

import MDELite.Error;
import MDELite.ParsE;
import Parsing.Parsers.LineToParse;
import Parsing.DBPrimitives.TupleStmt;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * a tuple is an instance of a TableSchema
 */
public class Tuple {

    /**
     * Every tuple points to its TableSchema of origin this field should be
     * ignored once a table is computed
     *
     */
    protected final TableSchema schema;

    /**
     * tuple contents are a map that pairs (column name, value)
     */
    protected final Map<String, String> values;

    // ACCESSORS AND INFORMATION
    /**
     * returns value of given columnName; if columnName unrecognized, Error is
     * thrown
     *
     * @param columnName -- name of column to read
     * @return value of given columnName
     */
    public String get(String columnName) {
        if (values.containsKey(columnName)) {
            return values.get(columnName);
        } else {
            schema.hasColumnEH(columnName);
            throw Error.toss(Error.tupleMissingValue, schema.getName(), columnName);
        }
    }

    /**
     * return value of first column of tuple
     *
     * @return id of tuple
     */
    public String getId() {
        List<Column> all = getColumns();
        String id = all.get(0).getName();
        return get(id);
    }

    /**
     * return int value of column with columnName; no checking is done to know
     * if column value can be converted into an int
     *
     * @param columnName -- name of column to read
     * @return int value of column with columnName
     */
    public int getInt(String columnName) {
        return Integer.parseInt(get(columnName));
    }

    /**
     * return double value of a column with columnName; no checking is done to
     * know if column value can be converted into an double
     *
     * @param columnName -- name of column to read
     * @return double value of a column with columnName
     */
    public double getDouble(String columnName) {
        return Double.parseDouble(get(columnName));
    }

    /**
     * return float value of a column with columnName; no checking is done to
     * know if column value can be converted into a float
     *
     * @param columnName -- name of column to read
     * @return float value of a column with columnName
     */
    public float getFloat(String columnName) {
        return Float.parseFloat(get(columnName));
    }

    /**
     * return boolean value of a column with columnName; no checking is done to
     * know if column value can be converted into a boolean
     *
     * @param columnName -- name of column to read
     * @return boolean value of a column (with columnName)
     */
    public boolean getBool(String columnName) {
        return Boolean.parseBoolean(get(columnName));
    }

    /**
     * returns null if string content of columnName is "null" otherwise string
     * column value is returned
     *
     * @param columnName -- name of column to read
     * @return 'null' if string content of columnName is "null" otherwise string
     * column value is returned
     */
    public String getNull(String columnName) {
        String result = get(columnName);
        return result.equals("null") ? null : result;
    }

    /**
     * is value of columnName "null"? Remember: nulls in an MDELite database are
     * "null" strings, not java null or ""
     *
     * @param columnName -- name of column to check
     * @return true if null value
     */
    public boolean isNull(String columnName) {
        String result = get(columnName);
        return result.equals("null") || result.equals("");
    }

    /**
     * does columnName have value?
     *
     * @param columnName -- name of column to check
     * @param value -- value column should have
     * @return true if column has this value
     */
    public boolean is(String columnName, String value) {
        String result = get(columnName);
        return result.equals(value);
    }

    /**
     * does columnName have any of these values
     *
     * @param columnName -- name of column to check
     * @param values -- set of values the column might have
     * @return does valueOf(colunnName) in {values}?
     */
    public boolean is(String columnName, String... values) {
        String result = get(columnName);
        for (String s : values) {
            if (result.equals(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * does columnName have integer value?
     *
     * @param columnName -- name of column to check
     * @param value -- integer value column should have
     * @return true if column has this value
     */
    public boolean is(String columnName, int value) {
        int result = getInt(columnName);
        return result == value;
    }

    /**
     * does columnName have float value?
     *
     * @param columnName -- name of column to check
     * @param value -- float value column should have
     * @return true if column has this value
     */
    public boolean is(String columnName, float value) {
        float result = getFloat(columnName);
        return result == value;
    }

    /**
     * does columnName have double value?
     *
     * @param columnName -- name of column to check
     * @param value -- double value column should have
     * @return true if column has this value
     */
    public boolean is(String columnName, double value) {
        double result = getDouble(columnName);
        return result == value;
    }

    /**
     * does columnName have boolean true?
     *
     * @param columnName -- name of column to check
     * @return true if column has true value
     */
    public boolean is(String columnName) {
        boolean result = getBool(columnName);
        return result;
    }

    /**
     * sets a column with columnName to have the given string value
     *
     * @param columnName -- name of column to update
     * @param value -- value to replace
     * @return updated tuple
     */
    public Tuple set(String columnName, String value) {
        schema.hasColumnEH(columnName);
        values.put(columnName, value);
        return this;
    }

    /**
     * sets a column with columnName to have the given int value
     *
     * @param columnName -- name of column to update
     * @param value -- value to replace
     * @return updated tuple
     */
    public Tuple set(String columnName, int value) {
        return set(columnName, value + "");
    }

    /**
     * sets a column with columnName to have the given double value
     *
     * @param columnName -- name of column to update
     * @param value -- value to replace
     * @return updated tuple
     */
    public Tuple set(String columnName, double value) {
        return set(columnName, value + "");
    }

    /**
     * sets a column with columnName to have the given float value
     *
     * @param columnName -- name of column to update
     * @param value -- value to replace
     * @return updated tuple
     */
    public Tuple set(String columnName, float value) {
        return set(columnName, value + "");
    }

    /**
     * sets a column with columnName to have the given boolean value
     *
     * @param columnName -- name of column to update
     * @param value -- value to replace
     * @return updated tuple
     */
    public Tuple set(String columnName, boolean value) {
        return set(columnName, value + "");
    }

    /**
     * sets a column with columnName to have "null" value
     *
     * @param columnName -- name of column to update
     * @return updated tuple
     */
    public Tuple setNull(String columnName) {
        return set(columnName, "null");
    }

    /**
     * copies selected column values from existing tuple
     *
     * @param from -- tuple whose values are to be copied
     * @param columns -- columns to copy
     * @return updated tuple
     */
    public Tuple copy(Tuple from, List<Column> columns) {
        for (Column c : columns) {
            String cname = c.getName();
            set(cname, from.get(cname));
        }
        return this;
    }

    /**
     * copies all column values from tuple into 'this' tuple
     *
     * @param from -- tuple from which
     * @return updaed tuple
     */
    public Tuple copy(Tuple from) {
        return copy(from, getColumns());
    }

    /**
     * copies column values in 'from' tuple to 'this' tuple
     *
     * @param from -- tuple to copy values from
     * @param cor -- correspondence of 'from' tuple and 'this' tuple columns
     * @return --  correspondence updated tuple
     */
    public Tuple copy(Tuple from, ColumnCorrespondence cor) {
        for (ColumnCorrespondence.Pair p : cor.correspondence) {
            if (p.which) {
                this.set(p.left, from.get(p.right));
            } else {
                this.set(p.left, p.fun.apply(from));
            }
        }
        return this;
    }

    /**
     * returns a (deep) copy of a tuple; precondition: newSchema and tableSchema
     * must have identically defined sets of columns; the schemas may have
     * different names
     *
     * @param newSchema -- returned tuple copy must be instance of this schema
     * @return -- copy of tuple
     */
    public Tuple copy(TableSchema newSchema) {
        Tuple cpy = new Tuple(newSchema);
        cpy.values.putAll(values);
        return cpy;
    }

    /**
     * returns TableSchema of 'this' tuple
     *
     * @return TableSchema of 'this' tuple
     */
    public TableSchema getSchema() {
        return schema;
    }

    /**
     * returns current number of (column,value) pairs in this tuple. When tuple
     * isComplete(), this should be the same number as the number of columns in
     * its tuple schema
     *
     * @return current number of (column,value) pairs in this tuple
     */
    public int size() {
        return values.size();
    }

    /**
     *
     * Method delegates to TableSchema.getColumns()
     *
     * @return list of all columns for this tuple
     */
    public List<Column> getColumns() {
        return schema.getColumns();
    }

    /**
     * projects 'this' tuple to the set of columns of a super table schema; if
     * table schemas do not match, tuple.getName() throws an Error projection
     * retains the first set of columns -- columns that are removed are at the
     * end of a tuple
     *
     * @param superTableSchema -- schema of supertable to project to
     * @return superTableSchema projected tuple
     */
    public Tuple project(TableSchema superTableSchema) {
        Tuple t = new Tuple(superTableSchema);
        for (Column c : superTableSchema.getColumns()) {
            String colName = c.getName();
            String value = get(colName);
            t.set(colName, value);
        }
        t.isComplete();  // just to make sure
        return t;
    }

    /**
     * asserts that a tuple has its full complement of values. If not, an Error
     * is thrown
     */
    public void isComplete() {
        String errors = "";

        // Step 1: make sure that the tuple has AT LEAST its required set of columns
        for (Column c : schema.getColumns()) {
            String columnName = c.getName();
            if (!values.containsKey(columnName)) {
                errors += " " + columnName;
            }
        }
        if (!errors.equals("")) {
            throw Error.toss(Error.tupleMissingValueS, schema.getName(), errors);
        }

        // Step 2: make sure that the tuple has NO MORE than its required set of columns
        for (String colName : values.keySet()) {
            if (schema.getColumn(colName) == null) {
                errors += " " + colName;
            }
        }
        if (!errors.equals("")) {
            throw Error.toss(Error.tupleTooManyColumns, schema.getName(), errors);
        }
    }

    /**
     * does 'this' tuple equal the given tuple requires 'this' and given tuple
     * to belong to the same schema and have the same column values
     *
     * @param t -- tuple to compare
     * @return true if this tuple = t tuple
     */
    public boolean equals(Tuple t) {
        if (t.schema != schema) {
            return false;
        }
        return hasSameValuesAs(t);
    }

    /**
     * are all (col,val) pairs in 'this' tuple also in the given tuple?
     *
     * @param t -- tuple to compare
     * @return true if this tuple = t tuple
     */
    public boolean hasSameValuesAs(Tuple t) {
        for (Column c : getColumns()) {
            String cn = c.getName();
            if (!t.get(cn).equals(this.get(cn))) {
                return false;
            }
        }
        return true;
    }

    // CONSTRUCTORS
    /**
     * create an empty tuple for the given schema
     *
     * @param schema tuple schema to instantiate
     */
    public Tuple(TableSchema schema) {
        this.schema = schema;
        values = new HashMap<>();
    }

    /**
     * create an empty tuple for the given table
     *
     * @param table -- table to which tuple should conform
     */
    public Tuple(Table table) {
        this(table.getSchema());
    }

    /**
     * Add the (column,value) pairs of tuple t to the existing tuple. can be a
     * dangerous method -- if order of attributes change in table schema, set
     * values will not assign values to attributes correctly
     *
     * @param t -- tuple whose (column, value) pairs are to be added to tuple
     * 'this' used in table joins; not exported outside of package
     * @return -- tuple that is created
     */
    public Tuple setValues(Tuple t) {
        String tableSchemaName = t.schema.getName();
        if (tableSchemaName.contains(".") || tableSchemaName.contains("_x_")) {
            tableSchemaName = "";
        } else {
            tableSchemaName = tableSchemaName + ".";
        }

        for (Entry<String, String> entry : t.values.entrySet()) {
            String colName = tableSchemaName + entry.getKey();
            set(colName, entry.getValue());
        }
        return this;
    }

    /**
     * Add the (column,null) pairs of tuple t to the existing tuple. null is the
     * "" value. can be a dangerous method -- if order of attributes change in
     * table schema, set values will not assign values to attributes correctly
     *
     * @param ts -- table schema to which tuple t conforms.
     */
    public void setNulls(TableSchema ts) {
        String tableSchemaName = ts.getName();
        if (tableSchemaName.contains(".") || tableSchemaName.contains("_x_")) {
            tableSchemaName = "";
        } else {
            tableSchemaName = tableSchemaName + ".";
        }

        for (Column c : ts.getColumns()) {
            String colName = tableSchemaName + c.getName();
            setNull(colName);
        }
    }

    // CONSTRUCTOR UTILITIES
    /**
     * adds a list of values, in order in which their columns are defined, to an
     * empty tuple. an Error is thrown if not all columns have values.
     *
     * @param vals is list of values to add to an empty tuple
     * @return -- tuple that is updated
     */
    public Tuple setValues(String... vals) {
        if (vals.length != schema.size()) {
            throw Error.toss(Error.tupleSetValues, vals.length, schema.getName(), schema.size());
        }
        int i = 0;
        for (Column c : schema.getColumns()) {
            values.put(c.getName(), vals[i]);
            i++;
        }
        isComplete();
        return this;
    }

    /**
     * parses a line at lineno, expecting to parse a legal prolog tuple
     * declaration that conforms to table schema ts
     *
     * @param line to parse
     * @param lineno line number of the line
     * @param ts table schema for tuple to conform
     * @return Tuple
     *
     */
    public static Tuple parseTupleDecl(String line, int lineno, TableSchema ts) {
        // Step 1: parse tuple
        LineToParse ds = new LineToParse(line, lineno);
        // ds.parser(TupleStmt.TupleStmt);  // ignore output
        TupleStmt s = new TupleStmt(ds);
        s.parse();

        // Step 2: unpack parsing to get table name, and list of values, and create an empty tuple
        LinkedList<String> list = ds.parseList;
        String tableName = list.removeFirst();  // this is the table name
        Tuple newTuple = new Tuple(ts);
        if (!tableName.equals(ts.getName())) {
            throw ParsE.toss(ParsE.csvTableNameMismatch, lineno, ts.getName(), tableName);
            //new ParseException(lineno, "tuple name does not equal table name");
        }

        if (list.size() != ts.size()) {
            throw ParsE.toss(ParsE.tupleHasInsufficientValues2, lineno, ts.getName());
        }
        // Step 3: assemble the tuple
        for (int i = 0; i < ts.size(); i++) {
            // Get the value delimited by "@@@"
            String value = list.removeFirst();  // this is a column value
            value = value.replace("'", "");     // remove single quotes
            Column target = ts.getColumn(i);
            String columnName = target.getName().replace("\"", "");
            newTuple.set(columnName, value);
        }

        // Step 4: return tuple
        return newTuple;
    }

    // PRINT 
    /**
     * print tuple to PrintStream out
     *
     * @param out where to print
     */
    public void print(PrintStream out) {
        out.print(this);
    }

    /** print tuple to System.out
     */
    public void print() {
        System.out.print(this);
    }
    
    /**
     * converts tuple into a string; throws error if # of columns of tuple do
     * not match # of columns in table schema
     *
     * @return string of tuple
     */
    @Override
    public String toString() {
        if (values.size() != schema.size()) {
            throw Error.toss(Error.tupleWrongNumAttributes, schema.getName());
        }
        String q;
        String tuple = schema.getName() + "(";
        String comma = "";
        for (Column c : schema.getColumns()) {
            q = c.isQuoted() ? "'" : "";
            String val = values.get(c.getName());
            if (val == null && !c.isQuoted()) {
                val = "null";
            }
            tuple = tuple + comma + q + val + q;
            comma = ",";
        }
        tuple = tuple + ").\n";
        return tuple;
    }

    // Singleton table (table with 1 tuple) operations -- counterpart
    // to table operations
    /**
     * this is a table constructor that takes the join of one Lefttable tuple on
     * the leftJoinCol with rightTable on the rightJoinCol. The crossproduct of
     * both schemas is formed, and the joined table is returned.
     *
     * @param leftJoinCol -- join key of inner table
     * @param rightTable -- outer table of join
     * @param rightJoinCol -- join key of outer table
     * @return join of inner and rightTable
     */
    public Table join(String leftJoinCol, Table rightTable, String rightJoinCol) {
        TableSchema is = getSchema();
        TableSchema os = rightTable.getSchema();
        TableSchema X = is.crossProduct(os);

        //TableSchema LS = leftTable.getSchema().crossProduct(rightTable.getSchema())
        Table x = new Table(X);
        String match = get(leftJoinCol);

        for (Tuple out : rightTable.tuples()) {
            if (out.is(rightJoinCol, match)) {
                Tuple t = new Tuple(X);
                t.setValues(this);
                t.setValues(out);
                x.add(t);
            }
        }
        return x;
    }

    /**
     * this takes the RightSemiJoin of 'this' left tuple on the leftJoinCol with
     * rightTable on the rightJoinCol. A table of type rightTable is returned;
     * its tuples are those that would join with the left (this) tuple.
     *
     * @param leftJoinCol -- join key of inner table
     * @param rightTable -- outer table of join
     * @param rightJoinCol -- join key of outer table
     * @return RightSemiJoin of inner and rightTable
     */
    public Table rightSemiJoin(String leftJoinCol, Table rightTable, String rightJoinCol) {
        TableSchema RS = rightTable.getSchema();
        Table right = new Table(RS);
        String leftJoinValue = get(leftJoinCol);
        for (Tuple r : rightTable.tuples()) {
            if (r.is(rightJoinCol, leftJoinValue)) {
                right.add(r);
            }
        }
        return right;
    }

    /**
     * returns true if there exists a tuple that satisfies "this".thisJoinkey ==
     * rightTable.rightJoinCol; returns false otherwise
     *
     * @param rightTable -- the other table to which 'this' is to be joined
     * @param leftJoinCol -- the join key of 'this' table
     * @param rightJoinCol -- the join key of rightTable
     * @return -- the first tuple of rightTable that joins with "this", null
     * otherwise
     */
    public boolean joinExists(String leftJoinCol, Table rightTable, String rightJoinCol) {
        String value = get(leftJoinCol);
        for (Tuple tb : rightTable.tuples()) {
            if (tb.is(rightJoinCol, value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * returns the first tuple of rightTable that joins with 'this', where
     * "this".thisJoinkey == rightTable.rightJoinCol; returns null otherwise;
     * technically this is a rightSemiJoinFirst operation
     *
     * @param rightTable -- the other table to which 'this' is to be joined
     * @param leftJoinCol -- the join key of 'this' table
     * @param rightJoinCol -- the join key of rightTable
     * @return -- 1st tuple of rightTable that joins with "this", else null
     */
    public Tuple joinFirst(String leftJoinCol, Table rightTable, String rightJoinCol) {
        String value = get(leftJoinCol);
        for (Tuple tb : rightTable.tuples()) {
            if (tb.is(rightJoinCol, value)) {
                return tb;
            }
        }
        return null;
    }

    /**
     * same as joinFirst, except an Error is thrown if no tuple of otherTable is
     * found.
     *
     * @param otherTable -- the other table to which 'this' is to be joined
     * @param thisJoinKey -- the join key of 'this' table
     * @param otherJoinKey -- the join key of otherTable
     * @return -- the first tuple of otherTable that joins with "this"
     */
    public Tuple joinFirstEH(String thisJoinKey, Table otherTable, String otherJoinKey) {
        Tuple result = joinFirst(thisJoinKey, otherTable, otherJoinKey);
        if (result == null) {
            throw Error.toss(Error.tableNoTupleWithKey, otherTable.getName(), otherJoinKey, get(otherJoinKey));
        }
        return result;
    }

    /**
     * is 'this' tuple in the given table?
     *
     * @param table -- to search
     * @return true if 'this' tuple is in table.
     */
    public boolean in(Table table) {
        return table.stream().anyMatch(t -> this.hasSameValuesAs(t));
    }

    /**
     * is 'this' tuple NOT in the given table?
     *
     * @param table -- to search
     * @return true if 'this' tuple is NOT in table.
     */
    public boolean notIn(Table table) {
        return !in(table);
    }
}
