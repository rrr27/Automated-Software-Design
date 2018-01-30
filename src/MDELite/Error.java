package MDELite;

/** all run-time errors in MDELite */
public enum Error implements ErrInt<Error> {

    //badCommandLineArgs("incorrect command line arguments"),
    consult("Check FILENAME for more details about termination"),
    databaseFileNoExist("Database file FILENAME does not exist or can't be opened"),
    dbSchemaFileNoExist("DbSchema file FILENAME does not exist or can't be opened"),
    dbSchemaInheritanceCycle("Table TABLENAME participates in inheritance cycle"),
    dbSchemaMultipleSubTableDecls("Multiple subtable declarations for table TABLENAME"),
    dbSchemaMultipleTableDecls("Multiple declarations for table TABLENAME"),
    dbSchemaNoMoreSubTables("Cannot add subtable schema declaration to a 'finished' dbSchema SCHEMANAME"),
    dbSchemaNoMoreTables("Cannot add table schema TABLESCHEMA to a 'finished' dbSchema SCHEMANAME"),
    dbSchemaNotFinished("Cannot instantiate dbSchema SCHEMANAME because it is not 'finished'"),
    dbSchemaNotValid("File FILENAME is not a valid dbSchema"),
    dirCreation("Unable to create directory FILENAME"),
    cantCopyFile("Cannot copy FILE : REASON"),
    columnFormatBad("Column format is bad REASON"),
    correspondenceNotFound("Cannot find correspondence LEFT"),
    csvReadError("csv read error on FILENAME : ERRORMSG see line LINENO"),
    errorReport("Error count COUNT"),
    fileNoExist("File FILENAME does not exist or can't be opened"),
    ioerror("In file FILENAME : ERROR"),
    noMDELiteHome("cannot determine directory location of MDELite's jar file"),
    numberColumnsDiffer("Tables with name TABLENAME do not have same number of columns"),
    numberSubTablesDiffer("Number of subtable definitions in schemas NAME1 and NAME2 are different"),
    numberTablesDiffer("Number of table definitions in dbSchemas NAME1 and NAME2 are different"),
    ooschemaFile("File FILENAME is believed to be an .ooschema file - should be a .schema.pl file"),
    subTableNoFind("Cannot find subTable declaration NAME in SCHEMANAME"),
    subTableNoSameSuperDifferentNames("Super table schemas NAME1, NAME2 are different"),
    subTableNoSameSuperSameNames("Super table schemas are different even though there name NAME is the same"),
    tableDuplicateColumn("Column COLUMNNAME already exists in table TABLENAME"),
    tableFileNoExist("Table file FILENAME does not exist or can't be opened"),
    tableHasNoColumns("Table TABLENAME in schema SCHEMANAME has no columns"),
    tableHasNoSchema("Table TABLE has no schema"),
    tableKeyed("Keyed table not implemented yet"),
    tableNoDeleteTuple("Attempt to delete tuple with COLUMN = VALUE, not found in table TABLENAME"),
    tableNoExist("Table TABLENAME not in dbSchema SCHEMANAME"),
    tableNoTupleFound("No tuple in table TABLENAME satisfies given predicate"),
    tableNoTupleWithKey("Cannot find tuple in table TABLE where KEY = VALUE"),
    tableNotInDataBase("Table TABLENAME does not belong to a database; it is free-standing"),
    tableSchemasNotTheSame("Table schemas SCHEMA1 and SCHEMA2 are different"),
    tableSchemaNamesDifferent("DbSchema names NAME1 and NAME2 are not the same"),
    tableSchemaNoShareColumn("Tables with TABLENAME do not share column COLUMNNAME"),
    tableSchemaNothingToRead("No table schema read in file FILENAME"),
    tableUnknownColumn("Unknown column COLUMNNAME in table TABLE"),
    tablesHaveDifferentNames("Tables TABLENAME1 and TABLENAME2 do not have same schema names"),
    tooManyArguments("Too many error arguments in EXPLANATION"),
    tupleMissingValue("Tuple in table TABLENAME missing value for COLUMNNAME"),
    tupleMissingValueS("Tuple in table TABLENAME missing values for COLUMNNAMES"),
    tupleSetValues("Setting NUMBER1 column values in tuple for table TABLENAME with NUMBER2 columns"),
    tupleTooManyColumns("Tuple of table TABLENAME has too many columns : COLUMNNAMES"),
    tupleWrongNumAttributes("Tuple from table TABLENAME does not have correct number of attributes"),
    violetWrongJavaVersion("Wrong Java version VERSION for Violet; expected CORRECTVERSION"),
    violetWrongClass("Wrong Java class CLASS for Violet; expected CORRECTCLASS"),
    violetWrongNumberObjects("Wrong number of objects in violet SPEC; expected NUMBER but found ANUMBER"),
    violetUnexpectedType("Wrong type TYPE for Violet; expected type FTYPE"),
    violetPropertyUnexpected("Property AAA found but BBB was expected"),
    violetMissingAttribute("Missing attribute for ATTRIBUTE"),
    violetParsingError("Parsing error in reading violet file FILENAME : ERROR"),
    wrongCSVNameFormat("Tsble file FILENAME does not end in .csv"),
    wrongDBNameFormat("Database file name FILENAME not in correct format: <dbname>.<schemaname>.pl"),
    wrongDatabaseNameFormat("Database file does not end in '.pl' : FILENAME"),
    wrongFileName("DB/Schema file FILENAME does not end in '.pl' or '.ooschema.pl'"),
    wrongFileNameFormatShort("File name FILENAME not in correct format: does not end in 'pl'"),
    wrongOOSchemaNameFormat("File name FILENAME not in correct format: <schemaname>.ooschema.pl"),
    wrongPathNameFormat("Path PATH has / and \\ in it - be consistent"),
    wrongSchemaNameFormat("Schema file name FILENAME does not end in '.schema.pl'"),
    wrongSchemaNameSFormat("Schema file name FILENAME does not end in '.schema.pl' or '.ooschema'.pl"),
    wrongTable("Adding tuple of wrong type NAME1 to table NAME2"),
    wrongTableSameName("Adding tuple of wrong type to table TABLE even though tables have same name"),
    yumlUnknownType("Yuml unknown type TYPE"),
    ;

    private final String msg;

    /** required constructor
     * 
     * @param msg -- string interpretation of error
     */
    Error(String msg) {
        this.msg = msg;
    }
    
    /** get error message of error */
    @Override
    public String getMsg() { 
        return msg;
    }
    
    /**
     * @return the enums of Error in an array
     */
    @Override
    public ErrInt<Error>[] vals() {
        return Error.values();
    }
    
    /** this is the constructor for throwing an error
     * 
     * @param error -- which error 
     * @param args -- arguments of error message
     * @return RuntimeException
     */
    public static RuntimeException toss(Error error, Object... args) {
        if (error.msg.contains("FILENAME")) { args[0] = Utils.shortFileName((String) args[0]); }
        return new RuntimeException(MDELite.Utils.makeString(error.msg,args));
    }  
}
