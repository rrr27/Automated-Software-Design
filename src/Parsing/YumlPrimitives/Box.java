package Parsing.YumlPrimitives;

import Parsing.GeneralPrimitives.Pattern;
import Parsing.Parsers.LineToParse;
import Parsing.DBPrimitives.LeftBracket;
import Parsing.DBPrimitives.RightBracket;
import Parsing.GeneralPrimitives.Choose1;
import Parsing.GeneralPrimitives.Nothing;
import Parsing.GeneralPrimitives.Token;

/** Yuml Production : Box = ['name [,qs] [,qs] ']'*/
public class Box extends Pattern {
    static private Token[] sequence = { new LeftBracket(), new BoxEntry(), 
        new Choose1( new BarrierBoxEntry(), new Nothing() ), 
        new Choose1( new BarrierBoxEntry(), new Nothing() ),
        new RightBracket(), };
    
    /** Standard constructor */
    public Box() {
        super(sequence);
    }
    
    /** Standard constructor with line to parse
     * @param l -- LineToParse
     */
    public Box(LineToParse l) {
        super(l);
    }
}
