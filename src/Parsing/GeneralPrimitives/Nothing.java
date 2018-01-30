package Parsing.GeneralPrimitives;

import Parsing.Parsers.LineToParse;

/** Yuml token "" */
public class Nothing extends FixedSave {
    public static final String tok = "_NoThInG_";
    
    /** Standard constructor */
    public Nothing() {
        super();
    }
    
    /** Standard constructor with line to parse
     * @param l -- LineToParse
     */
    public Nothing(LineToParse l) {
        super(l);
    }

    @Override
    public String getEmsg() {
        return "nothing";
    }

    @Override
    public String getPattern() {
        return "";
    }
    
    @Override
    public void parse() {
        toBeParsed.add2ParseList(tok);
    }

    @Override
    public Nothing klone(LineToParse l) {
       return new Nothing(l);
    }
}
