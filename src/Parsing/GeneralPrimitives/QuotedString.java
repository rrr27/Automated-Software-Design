package Parsing.GeneralPrimitives;

import MDELite.ParsE;
import Parsing.Parsers.LineToParse;

/** single quoted or double quoted string */
public class QuotedString extends Token {

    /** Standard constructor */
    public QuotedString() {
    }
    
    /** Standard constructor with line to parse
     * @param l -- line to parse
     */
    public QuotedString(LineToParse l) {
        super(l);
    }

    @Override
    public boolean canParse() {
        toBeParsed.AssertNotAtEnd(this);
        char c = toBeParsed.peek();
        return toBeParsed.isQuote(c);
    }
    
    @Override
    public void consumeToken() {
        int i;
        char e = toBeParsed.peek();
        boolean pastEnd = true;

        for (i = 1; i < toBeParsed.toParse.length(); i++) {
            char c = toBeParsed.toParse.charAt(i);
            if (e==c) {  // matching quote
                pastEnd = false;
                break;
            }
        }
        if (!pastEnd) {
           toBeParsed.returnSave(i + 1);
           return;
        }
        throw ParsE.toss(ParsE.runAwayString, toBeParsed.lineno, toBeParsed.toParse);
    }

    @Override
    public void parse() {
        toBeParsed.parseSave(this);
    }
    
    @Override
    public QuotedString klone(LineToParse l) {
        return new QuotedString(l);
    }
    
    @Override
    public String getEmsg() { return "quoted string"; }
}
