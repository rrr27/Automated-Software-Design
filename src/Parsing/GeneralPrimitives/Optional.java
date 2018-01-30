package Parsing.GeneralPrimitives;

import Parsing.Parsers.LineToParse;

/** optional token [t] */
public class Optional extends Token {

    Token opt;

    /** Standard constructor 
     * @param opt -- token that could be optional
     */
    public Optional(Token opt) {
        this.opt = opt;
    }

    /** Standard constructor with line to parse
     * @param opt -- token that could be optional
     * @param l -- line to parse
     */
    public Optional(Token opt, LineToParse l) {
        super(l);
        this.opt = opt;
    }

    private Token o;
    private boolean realAnswer;

    @Override
    public boolean canParse() { 
        toBeParsed.AssertNotAtEnd(this);
        o = opt.klone(toBeParsed);
        realAnswer = o.canParse(); // consume opt token
        return true;
    }

    @Override
    public void consumeToken() {
        o.consumeToken();
    }

    @Override
    public void parse() {
        if (realAnswer == false) {
            return;
        }
        o.parse();
    }

    @Override
    public Optional klone(LineToParse l) {
        return new Optional(opt, l);
    }

    @Override
    public String getEmsg() {
        return "optional";
    }

}
