package Parsing.GeneralPrimitives;

import Parsing.Parsers.LineToParse;

/** end of parse designator */
public class End extends Token {
    
    /** Standard constructor */
    public End() {
    }
    
    /** Standard constructor with line to parse
     * @param l  -- line to parse
     */
    public End(LineToParse l) {
        super(l);
    }

    @Override
    public boolean canParse() {
        return toBeParsed.atEnd();
    }

    @Override
    public void consumeToken() {
    }
    
    @Override
    public void parse() {
    }
    
    @Override
    public End klone(LineToParse l) {
        return new End(l);
    }
    
    @Override
    public String getEmsg() { return "end of line"; }
}
