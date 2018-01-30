package Parsing.DBPrimitives;

import Parsing.GeneralPrimitives.NumBer;
import Parsing.GeneralPrimitives.NonEmptyList;
import Parsing.GeneralPrimitives.QuotedString;
import Parsing.GeneralPrimitives.Name;
import Parsing.GeneralPrimitives.Choose1;
import Parsing.Parsers.LineToParse;

/** TupleValues is a sequence of I(,I)*, where I = single-quoted-string | name | number ; */
public class TupleValues extends NonEmptyList { // comma separated tuple values

    /** Standard constructor */
    public TupleValues() {
        super(new Choose1(new QuotedString(), new NumBer(), 
                new Name()), new Comma() );
    }

    /** Standard constructor with line to parse 
     * @param l -- line to parse
     */
    public TupleValues(LineToParse l) {
        super(new Choose1(new QuotedString(), new NumBer(), new Name()), new Comma(), l);
    }
    
    @Override
    public TupleValues klone(LineToParse l) {
        return new TupleValues(l);
    }
}
