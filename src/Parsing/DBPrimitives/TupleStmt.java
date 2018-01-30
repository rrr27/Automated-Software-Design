package Parsing.DBPrimitives;

import Parsing.GeneralPrimitives.Token;
import Parsing.GeneralPrimitives.End;
import Parsing.GeneralPrimitives.Pattern;
import Parsing.GeneralPrimitives.Name;
import Parsing.Parsers.LineToParse;

/** tuple statement : name( TupleValues ). */
public class TupleStmt extends Pattern {

    /** tuple statement as name(value1, value2,...). */
    public static Token[] tupleStmt = {new Name(), new LeftParen(), 
        new TupleValues(), new RightParen(), new Dot(), new End()};

    /** Standard constructor */
    public TupleStmt() {
        super(tupleStmt);
    }
    
    /** Standard constructor with line to parse
     * @param l -- LineToParse
     */
    public TupleStmt(LineToParse l) {
        super(tupleStmt);
        setLineToParse(l);
    }
}
