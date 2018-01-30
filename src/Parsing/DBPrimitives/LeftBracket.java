package Parsing.DBPrimitives;

import Parsing.GeneralPrimitives.FixedSkip;
import Parsing.Parsers.LineToParse;

/** left bracket '[' token */
public class LeftBracket extends FixedSkip {

    /** Standard constructor */
    public LeftBracket() {}
    
    /** Standard constructor with line to parse
     * @param l -- line to parse
     */
    public LeftBracket(LineToParse l) {
        super(l);
    }
     
    @Override
    public LeftBracket klone(LineToParse l) {
        return new LeftBracket(l);
    }

    @Override
    public String getPattern() {
        return "[";
    }

    @Override
    public String getEmsg() { return "left bracket"; }
}
