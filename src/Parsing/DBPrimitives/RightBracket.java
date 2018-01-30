package Parsing.DBPrimitives;

import Parsing.GeneralPrimitives.FixedSkip;
import Parsing.Parsers.LineToParse;

/** RightBracket is "]" */
public class RightBracket extends FixedSkip {

    /** Standard constructor */
    public RightBracket() {
        super();
    }
    
    /** Standard constructor with line to parse
     * @param l -- line to parse
     */
    public RightBracket(LineToParse l) {
        super(l);
    }

    @Override
    public String getPattern() {
        return "]";
    }

    @Override
    public String getEmsg() { return "right bracket"; }
        
    @Override
    public RightBracket klone(LineToParse l) {
        return new RightBracket(l);
    }
}
