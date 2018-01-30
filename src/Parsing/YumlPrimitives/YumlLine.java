package Parsing.YumlPrimitives;

import Parsing.GeneralPrimitives.Token;
import Parsing.GeneralPrimitives.End;
import Parsing.GeneralPrimitives.Pattern;
import Parsing.Parsers.LineToParse;
import Parsing.GeneralPrimitives.Choose1;

/** Yuml Production : YumlLine: Box [connect] end */
public class YumlLine extends Pattern {
    static private Token[] sequence = { 
        new Box(), new Choose1( new End(), new Connect())};
    
    /** Standard constructor */
    public YumlLine() {
        super(sequence);
    }
    
    /** Standard constructor with line to parse
     * @param l -- LineToParse
     */
    public YumlLine(LineToParse l) {
        super(sequence);
        setLineToParse(l);
    }
}
