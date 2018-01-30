package MDELite;

/** known parsing errors for MDELite */
public enum ParsE implements ErrInt<ParsE> {

    csvTableNameMismatch("CVS tuple table name TABLENAME1 does not match CSV file name TABLENAME2"),
    dbaseDeclsTooMany("more than one dbase declaration in FILENAME"),
    ioerror("In file FILENAME : ERROR"),
    parseError("parse error MESSAGE"),
    parseIllformedNumber("ill-formed number starting with STRING"),
    patternEmpty("pattern is empty"),
    runAwayEntry("unended yuml box entry STR"),
    runAwayString("unended quoted string STR"),
    streamEndReached("end of line reached: TOKEN expected"),
    subTableDefBeforeDBase("subtable defined before dbase"),
    tableDefBeforeDBase("table defined before dbase"),
    tableOnly1Allowed("only one table definition permitted in FILENAME"),
    tableSchemaParseError("unable to parse table in FILENAME \n ERROR"),
    tupleHasInsufficientValues("in file FILENAME : insufficient number of values for a tuple of table TABLENAME"),
    tupleHasInsufficientValues2("insufficient number of values for a tuple of table TABLENAME"),
    yumlDuplicateBoxDecl("duplicate declaration for class NAME"),
    yumlIntHasFields("yuml interface NAME has fields"),
    yumlLineParser("yuml line parser not initialized"),
    yumlNoteHasFields("yuml note NAME has fields"),
    yumlNoteHasMethods("yuml note NAME has methods"),;

    private final String msg;

    /** java required constructor */
    ParsE(String msg) {
        this.msg = msg;
    }

    /** get parse error message/template */
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

    public static String makeString(ParsE error, int lineno, Object... args) {
        String front = (lineno < 0) ? "" : "at line " + lineno + " ";
        String back = Utils.makeString(error.msg, args);
        return front + back;
    }

    /** method that is used to create a RuntimeException to throw
     * 
     * @param error -- which parse error
     * @param lineno -- at what line number (-1 if unknown(
     * @param args -- arguments of parse error message
     * @return RuntimeException object to throw
     */
    public static RuntimeException toss(ParsE error, int lineno, Object... args) {
        if (error.msg.contains("FILENAME")) { args[0] = Utils.shortFileName((String) args[0]); }
        String msg = makeString(error, lineno, args);
        return new RuntimeException(msg);
    }
}
