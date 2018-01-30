package Parsing.GeneralPrimitives;

import MDELite.ParsE;
import Parsing.Parsers.LineToParse;

/** Choose1 is ( node1 | node2 | nodeN ) */
public class Choose1 extends Token { // comma separated tuple values
    Token[] opts;
    
    /** Standard constructor */
    protected Choose1() {
    }

    /** normal call that specifies possible tokens to see
     * @param opts -- tokens that could follow at this point in a parse
     */
    public Choose1(Token... opts) {
        this.opts = opts;
    }

    /** Standard constructor with line to parse
     * @param l -- line to parse
     * @param opts -- tokens that could follow at this point in a parse
     */
    public Choose1(LineToParse l, Token... opts) {
        super(l);
        this.opts = opts;
        if (opts.length<2) {
            throw ParsE.toss(ParsE.parseError, l.lineno, " Choose1 must have 2+ options");
        }
    }

    private Token next;

    @Override
    public boolean canParse() {
       // toBeParsed.AssertNotAtEnd(this);
        for (Token t : opts) {
            t.setLineToParse(toBeParsed);
            if (t.canParse()) {
                next = t;
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void consumeToken() {
        next.consumeToken();
    }

    @Override
    public void parse() {
        next.parse();
    }

    @Override
    public Choose1 klone(LineToParse l) {
        return new Choose1(l, opts);
    }

    @Override
    public String getEmsg() {
        String emsg = opts[0].getEmsg();
        String comma = " or ";
      
        for(int i = 1; i<opts.length; i++) {
            emsg =  opts[i].getEmsg() + comma + emsg;
            comma = ", ";
        }
        return emsg;
    }
}
