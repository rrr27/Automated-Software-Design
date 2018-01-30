package Parsing.YumlPrimitives;

import Parsing.GeneralPrimitives.FixedSave;
import Parsing.Parsers.LineToParse;

/** Yuml token greater-than */
public class Gtr extends FixedSave {
    public static final String tok = ">";
    
    /** Standard constructor */
    public Gtr() {
        super();
    }
    
    /** Standard constructor with line to parse
     * @param l -- LineToParse
     */
    public Gtr(LineToParse l) {
        super(l);
    }

    @Override
    public String getEmsg() {
        return tok;
    }

    @Override
    public String getPattern() {
        return tok;
    }

    @Override
    public Gtr klone(LineToParse l) {
       return new Gtr(l);
    }
}
