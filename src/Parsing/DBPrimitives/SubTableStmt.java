package Parsing.DBPrimitives;

import Parsing.GeneralPrimitives.Token;
import Parsing.GeneralPrimitives.End;
import Parsing.GeneralPrimitives.Pattern;
import Parsing.GeneralPrimitives.Name;
import Parsing.Parsers.LineToParse;

/** subtable statement: subtable(name,[ name1,name2... ]). */
public class SubTableStmt extends Pattern {

    /**
     * subTableStatement statement subtable,name,[name1,name2...]).
     */
    public static Token[] subTableStmt = {new SubTaBle(), new LeftParen(), 
        new Name(), new Comma(), new LeftBracket(), new NameList(), 
        new RightBracket(), new RightParen(), new Dot(), new End()};

    /** Standard constructor */
    public SubTableStmt() {
        super(subTableStmt);
    }

    /** Standard constructor with line to parse
     * @param l -- LineToParse
     */
    public SubTableStmt(LineToParse l) {
        super(subTableStmt);
        setLineToParse(l);
    }
}
