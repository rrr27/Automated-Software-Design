package CorrectResultsAfterChainingErrorMsgs;

import MDELite.*;

public enum ParsE implements ErrInt<ParsE> {

    csvTableNameMismatch("CVS tuple table name TABLENAME1 does not match CSV file name TABLENAME2"),
    dbaseDeclsTooMany("more than one dbase declaration in FILE"),
    parseError("parse 6error MESSAGE"),
    parseIllformedNumber("ill-formed number starting with STRING"),
    streamEndReached("end of string reached -- full statement not parsed"),
    subTableDefBeforeDBase("subtable defined before dbase"),
    tableDefBeforeDBase("table defined before dbase"),
    tableOnly1Allowed("only one table definition permitted in FILENAME"),
    tableSchemaParseError("unable to parse table in FILENAME \n ERROR"),
    tupleHasInsufficientValues("in file FILE : insufficient number of values for a tuple of table TABLENAME"),
    tupleHasInsufficientValues2("insufficient number of values for a tuple of table TABLENAME"),;

    private String msg;

    ParsE(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
    
     /**
     * @return the enums of ParsE in an array
     */
    @Override
    public ErrInt<ParsE>[] vals() {
        return ParsE.values();
    }

    private static String makeString(ParsE error, int lineno, Object... args) {
        String front = (lineno < 0) ? "" : "at line " + lineno + " ";
        String back = Utils.makeString(error.msg, args);
        return front + back;
    }

    public static RuntimeException toss(ParsE error, int lineno, Object... args) {
        return new RuntimeException(makeString(error, lineno, args));
    }
}
