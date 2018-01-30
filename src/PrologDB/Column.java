package PrologDB;

import MDELite.Error;

/**
 * A column is a read-only, non-modifiable (String name, boolean isQuoted) pair
 */
public class Column {

    /**
     * State of a column name -- name of column isQuoted -- does the column
     * contain single-quoted values? and kind
     */
    private final String name;
    private final boolean isQuoted;
    private final String kind;  // Table name whose ids populate this column

    // CONSTRUCTOR
    /**
     * copy Column constructor
     *
     * @param c -- column to copy
     */
    public Column(Column c) {
        name = c.name;
        isQuoted = c.isQuoted;
        kind = c.kind;
    }

    /**
     * rename constructor
     *
     * @param c -- column to copy
     * @param newName -- new column name
     */
    public Column(Column c, String newName) {
        name = newName;
        isQuoted = c.isQuoted;
        kind = c.kind;
    }

    /**
     * if qname is a single-quoted string (or if you're lazy, if first char of
     * qname is '), then column is quoted
     *
     * @param qname - quoted or unquoted name
     */
    public Column(String qname) {
        String[] array = qname.trim().split(":");
        String cname = array[0];
        if (cname.equals("")) {
            throw Error.toss(Error.columnFormatBad, qname);
        }
        if (cname.startsWith("'")) {
            this.name = cname.replace("'", "");
            this.isQuoted = true;
        } else {
            this.name = cname;
            this.isQuoted = false;
        }
        if (array.length > 2) {
            throw Error.toss(Error.columnFormatBad, qname);
        }
        kind = (array.length == 1) ? "" : array[1].replace("'", "");
    }

// ACCESSOR AND INFO
    /**
     *
     * @return string name of column
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return true if column values are single quoted
     */
    public boolean isQuoted() {
        return isQuoted;
    }

    /**
     * return kind
     *
     * @return kind
     */
    public String getKind() {
        return kind;
    }

    // UTILITIES
    /**
     * returns true if 'this' column equals the given otherColumn. Column
     * equality means both columns have the same name and same isQuoted
     * property.
     *
     * @param otherColumn that should be equal to 'this' column
     * @return true if 'this' column equals the given otherColumn.
     */
    public boolean equals(Column otherColumn) {
        if (otherColumn == null) {
            return false;  // nothing to compare
        }
        String tempName = otherColumn.getName();
        if (!name.equals(tempName)) {
            return false;  // names do not match
        }
        if (isQuoted != otherColumn.isQuoted()) {
            return false; // quotation property does not match
        }
        return true;  // they match!
    }

    /**
     * returns true if 'this' column and given column are both quoted useful for
     * batch copying of tuples from one table to another
     *
     * @param otherColumn that should be equal to 'this' column
     * @return true if 'this' column equals the given otherColumn.
     */
    public boolean equalsType(Column otherColumn) {
        if (otherColumn == null) {
            return false;  // nothing to compare
        }
        if (isQuoted != otherColumn.isQuoted()) {
            return false; // quotation property does not match
        }
        return true;  // they match!
    }

    // PRINT
    /**
     * a String schema specification of this column is its name in/not-in double
     * quotes
     *
     * @return a String of a schema specification for 'this' column
     */
    @Override
    public String toString() {
        String q = isQuoted ? "\"" : "";
        String k = (kind.equals(""))?"":":"+kind;
        return q + name + k + q;
    }
}
