package Parsing.GeneralPrimitives;

import Parsing.Parsers.LineToParse;
import MDELite.ParsE;

/** a NumBer is a sequence of [-](digit)+[.(digit)+] */
public class NumBer extends Token {

    /** Standard constructor */
    public NumBer() {
    }
    
    /** Standard constructor with line to parse
     * @param l -- line to parse
     */
    public NumBer(LineToParse l) {
        super(l);
    }

    @Override
    public boolean canParse() {
        toBeParsed.AssertNotAtEnd(this);
        char first = toBeParsed.charAt(0);
        char second = toBeParsed.charAt(1);
        if (first=='-' && toBeParsed.isDigit(second))
            return true;
        return toBeParsed.isDigit(first);
    }

   
    @Override
    public void consumeToken() {
        boolean dotNotYetSeen = true;
        boolean oneDigitPastDot = false;
        int i, j;
        char c = toBeParsed.peek();
        boolean positive = !toBeParsed.isNegative(c);
        int start = positive ? 0 : 1;
        for (i = start; i < toBeParsed.toParse.length(); i++) {
            c = toBeParsed.charAt(i);
            if (toBeParsed.isDot(c) && dotNotYetSeen) {
                dotNotYetSeen = false;
                continue;
            }
            if (toBeParsed.isDigit(c)) {
                if (!dotNotYetSeen) {
                    oneDigitPastDot = true;
                }
                continue;
            }
            break;
        }
        if (!dotNotYetSeen && !oneDigitPastDot)
            throw ParsE.toss(ParsE.parseIllformedNumber,toBeParsed.lineno, toBeParsed.toParse );
        toBeParsed.returnSave(i);
    } 
    
    @Override
    public void parse() {
        toBeParsed.parseSave(this);
    }
    
    @Override
    public NumBer klone(LineToParse l) {
        return new NumBer(l);
    }
    
    @Override
    public String getEmsg() { return "number"; }
}
