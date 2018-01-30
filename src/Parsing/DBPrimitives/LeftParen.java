package Parsing.DBPrimitives;

import Parsing.GeneralPrimitives.FixedSkip;
import Parsing.Parsers.LineToParse;

/** LeftParen '(' token */
public class LeftParen extends FixedSkip {

    /** Standard constructor */
    public LeftParen() {
        super();
    }

    /** Standard constructor with line to parse
     * @param l -- line to parse
     */
    public LeftParen(LineToParse l) {
        super(l);
    }

    @Override
    public String getPattern() {
        return "(";
    }

    @Override
    public String getEmsg() {
        return "left paren";
    }

    @Override
    public LeftParen klone(LineToParse l) {
        return new LeftParen(l);
    }
}
