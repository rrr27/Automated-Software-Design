package Parsing.YumlPrimitives;

import Parsing.GeneralPrimitives.FixedSave;
import Parsing.Parsers.LineToParse;

/** Yuml token "^" */
public class Hat extends FixedSave {
    public static final String tok = "^";
    
    /** Standard constructor */
    public Hat() {
        super();
    }
    
    /** Standard constructor with line to parse
     * @param l -- LineToParse
     */
    public Hat(LineToParse l) {
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
    public Hat klone(LineToParse l) {
       return new Hat(l);
    }
}
