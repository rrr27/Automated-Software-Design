package Parsing.DBPrimitives;

import Parsing.GeneralPrimitives.Token;
import Parsing.GeneralPrimitives.End;
import Parsing.GeneralPrimitives.Pattern;
import Parsing.GeneralPrimitives.Name;
import Parsing.Parsers.LineToParse;

/** table Statement : table( Name, [ columnDecl }). */
public class TableStmt extends Pattern {

    /** table statement as table(name,[oqn1, oqn2, ...])., where oqn is optionally quoted name*/
    public static Token[] tableStmt = {new TaBle(), new LeftParen(), new Name(), 
        new Comma(), new LeftBracket(), new ColumnDecl(), new RightBracket(), 
        new RightParen(), new Dot(), new End()};
    
    /** Standard constructor */
    public TableStmt() {
        super(tableStmt);
    }
    
    /** Standard constructor with line to parse
     * @param l -- LineToPars
     */
    public TableStmt(LineToParse l) {
        super(tableStmt);
        setLineToParse(l);
    }
}
