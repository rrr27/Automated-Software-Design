package Parsing.YumlPrimitives;

import Parsing.GeneralPrimitives.FixedSave;
import Parsing.Parsers.LineToParse;

/** Yuml token less-than */
public class Lss extends FixedSave {
    public static final String tok = "<";
    
    /** Standard constructor */
    public Lss() {
        super();
    }
    
    /** Standard constructor with line to parse
     * @param l -- LineToParse
     */
    public Lss(LineToParse l) {
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
    public Lss klone(LineToParse l) {
       return new Lss(l);
    }
}
