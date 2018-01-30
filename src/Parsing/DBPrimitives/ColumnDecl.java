package Parsing.DBPrimitives;

import Parsing.GeneralPrimitives.QuotedString;
import Parsing.GeneralPrimitives.Name;
import Parsing.GeneralPrimitives.Optional;
import Parsing.GeneralPrimitives.Choose1;
import Parsing.GeneralPrimitives.ColumnName;
import Parsing.Parsers.LineToParse;
import Parsing.GeneralPrimitives.NonEmptyList;

/** ColumnDecl is Name (, Name)* */
public class ColumnDecl extends Optional {
    
    /** Standard constructor */
    public ColumnDecl() {
        super(new NonEmptyList(new Choose1(new ColumnName(), new QuotedString()), new Comma()));
    }
    
    /** Standard constructor with line to parse
     * @param l -- line to parse
     */
    public ColumnDecl(LineToParse l) {
        super(new NonEmptyList(new Choose1(new ColumnName(), new QuotedString()), new Comma()),l);
    }
    
    @Override
    public ColumnDecl klone(LineToParse l) {
        return new ColumnDecl(l);
    }

    @Override
    public String getEmsg() { return "column decl"; }
}
