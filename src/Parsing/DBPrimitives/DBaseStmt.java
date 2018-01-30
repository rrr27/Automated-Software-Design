package Parsing.DBPrimitives;

import Parsing.GeneralPrimitives.Token;
import Parsing.GeneralPrimitives.End;
import Parsing.GeneralPrimitives.Pattern;
import Parsing.GeneralPrimitives.Name;
import Parsing.Parsers.LineToParse;

/**
 * dbase statement: dbase(name,[name1,name2...]).
 */
public class DBaseStmt extends Pattern {

    /**
     * definition of dbase statement
     */
    public static Token[] dbaseStmt = {new DBase(), new LeftParen(), new Name(), new Comma(), new LeftBracket(), new NameList(), new RightBracket(), new RightParen(), new Dot(), new End()};

    /** Standard constructor */
    public DBaseStmt() {
        super(dbaseStmt);
    }

    /** Standard constructor with line to parse
     * @param l -- LineToParse
     */
    public DBaseStmt(LineToParse l) {
        super(dbaseStmt);
        this.setLineToParse(l);
    }
}
